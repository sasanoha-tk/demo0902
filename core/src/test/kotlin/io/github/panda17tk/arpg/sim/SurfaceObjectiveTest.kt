package io.github.panda17tk.arpg.sim

import io.github.panda17tk.arpg.planet.PlanetBiome
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * The one surface HUD line is facts in a fixed order: the biome, the gravest deed already done to
 * this world, what is still left to do, then every society gauge that is non-zero. No sentence, no
 * greeting, so the same builder serves a first landing and a return.
 */
class SurfaceObjectiveTest {
    private fun line(s: PlanetSocietyState, elites: Int = 1) =
        SurfaceObjective.hudLine(PlanetBiome.NATURE, elites, s)

    @Test fun `while masters live the objective names the planet and the count`() {
        val s = SurfaceObjective.hudLine(PlanetBiome.NATURE, 2)
        assertTrue(s.contains("自然惑星"), s)
        assertTrue(s.contains("残り 2"), s)
    }

    @Test fun `a subdued planet points back to the escape pad`() {
        val s = SurfaceObjective.hudLine(PlanetBiome.MAGMA, 0)
        assertTrue(s.contains("火山惑星"), s)
        assertTrue(s.contains("脱出パッド"), s)
    }

    @Test fun `every biome yields a non-blank line in both states`() {
        for (b in PlanetBiome.entries) {
            assertTrue(SurfaceObjective.hudLine(b, 3).isNotBlank(), "$b active")
            assertTrue(SurfaceObjective.hudLine(b, 0).isNotBlank(), "$b cleared")
        }
    }

    @Test fun `a fresh society asks for the master and prints no gauge`() {
        val s = line(PlanetSocietyState(), elites = 3)
        assertTrue(s.contains("主を倒せ"), s)
        assertFalse(s.contains("敵対 "), s)
        assertFalse(s.contains("好意 "), s)
        assertFalse(s.contains("生態攪乱 "), s)
    }

    @Test fun `a leader down with the relic still on the ground asks for materials`() {
        val s = line(PlanetSocietyState().apply { leaderDefeated = true }, elites = 0)
        assertTrue(s.contains("素材を回収せよ"), s)
        assertFalse(s.contains("主を倒せ"), s)
    }

    @Test fun `relic claimed closes the objective back to the escape pad`() {
        val s = line(PlanetSocietyState().apply { leaderDefeated = true; relicClaimed = true }, elites = 0)
        assertTrue(s.contains("脱出パッドへ戻れ"), s)
        assertFalse(s.contains("素材を回収せよ"), s)
    }

    @Test fun `every deed that moves a gauge prints that gauge with its label`() {
        val harmed = line(PlanetSocietyState().apply { onChildHarmed() })
        assertTrue(harmed.contains("幼体を傷害"), harmed)
        assertTrue(harmed.contains("敵対 "), harmed)

        val repelled = line(PlanetSocietyState().apply { onPredatorRepelledNearChild() })
        assertTrue(repelled.contains("捕食者を排除"), repelled)
        assertTrue(repelled.contains("好意 "), repelled)

        val apex = line(PlanetSocietyState().apply { onApexKilled() })
        assertTrue(apex.contains("首位捕食者を殺害"), apex)
        assertTrue(apex.contains("生態攪乱 "), apex)

        val nest = line(PlanetSocietyState().apply { onHatchlingKilled() })
        assertTrue(nest.contains("巣を破壊"), nest)
        assertTrue(nest.contains("生態攪乱 "), nest)
    }

    @Test fun `repeat deeds stack and a saturated gauge prints one hundred`() {
        val twice = line(PlanetSocietyState().apply { onChildHarmed(); onChildHarmed() })
        assertTrue(twice.contains("敵対 60"), twice)
        val capped = line(PlanetSocietyState().apply { onChildHarmed(); onChildKilled() })
        assertTrue(capped.contains("敵対 100"), capped)
    }
}
