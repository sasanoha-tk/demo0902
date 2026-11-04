package io.github.panda17tk.arpg.sim

import io.github.panda17tk.arpg.planet.PlanetBiome
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * The deed tag: one noun phrase naming the gravest thing this run has already done to the world,
 * chosen in the planet's own value order (FR-6.4 / FR-7.2), so a world that holds children or the
 * apex sacred words the same deed as sacrilege.
 */
class SurfaceObjectiveDeedTagTest {
    private fun line(
        s: PlanetSocietyState,
        ctx: PlanetContext = PlanetContext.NEUTRAL,
        elites: Int = 0,
    ) = SurfaceObjective.hudLine(PlanetBiome.NATURE, elites, s, ctx)

    private val children = PlanetContext(PlanetTemperament.SILENT, SacredThing.CHILDREN, PlanetStorySeed.NONE)
    private val apex = PlanetContext(PlanetTemperament.SILENT, SacredThing.APEX, PlanetStorySeed.NONE)

    @Test fun `a child kill outranks every other deed and still shows the count`() {
        val s = PlanetSocietyState().apply { onChildHarmed(); onChildKilled() }
        val l = line(s, elites = 5)
        assertTrue(l.contains("幼体を殺害"), l)
        assertTrue(l.contains("残り 5"), l)
    }

    @Test fun `a world that holds children sacred words both deeds as sacrilege`() {
        val killed = line(PlanetSocietyState().apply { onChildKilled() }, children)
        assertTrue(killed.contains("聖なる子を殺害"), killed)
        val harmed = line(PlanetSocietyState().apply { onChildHarmed() }, children)
        assertTrue(harmed.contains("聖なる子を傷害"), harmed)
    }

    @Test fun `a world that does not hold children sacred uses the plain tags`() {
        val killed = line(PlanetSocietyState().apply { onChildKilled() })
        assertTrue(killed.contains("幼体を殺害"), killed)
        assertFalse(killed.contains("聖なる子"), killed)
        val harmed = line(PlanetSocietyState().apply { onChildHarmed() })
        assertTrue(harmed.contains("幼体を傷害"), harmed)
        assertFalse(harmed.contains("聖なる子"), harmed)
    }

    @Test fun `a world that holds the apex sacred names the divine beast`() {
        val l = line(PlanetSocietyState().apply { onApexKilled() }, apex)
        assertTrue(l.contains("神獣を殺害"), l)
        val plain = line(PlanetSocietyState().apply { onApexKilled() })
        assertTrue(plain.contains("首位捕食者を殺害"), plain)
        assertFalse(plain.contains("神獣"), plain)
    }

    @Test fun `a harm deed never outranks the kill it came from`() {
        val both = PlanetSocietyState().apply { onChildHarmed(); onNestMotherKilled() }
        val l = line(both)
        assertTrue(l.contains("幼体を傷害"), l)
        assertFalse(l.contains("巣を破壊"), l)
    }

    @Test fun `driving a predator off a child tags the removal only`() {
        val l = line(PlanetSocietyState().apply { onPredatorRepelledNearChild() })
        assertTrue(l.contains("捕食者を排除"), l)
        assertFalse(l.contains("殺害"), l)
    }

    @Test fun `either kind of nest loss shows as nest destruction`() {
        val hatchling = line(PlanetSocietyState().apply { onHatchlingKilled() })
        assertTrue(hatchling.contains("巣を破壊"), hatchling)
        val mother = line(PlanetSocietyState().apply { onNestMotherKilled() })
        assertTrue(mother.contains("巣を破壊"), mother)
    }

    @Test fun `an untouched world carries no deed tag at all`() {
        val l = line(PlanetSocietyState(), elites = 2)
        assertTrue(l.contains("主を倒せ"), l)
        for (tag in listOf("殺害", "傷害", "排除", "破壊")) assertFalse(l.contains(tag), "$tag :: $l")
    }

    @Test fun `every society deed has a tag in the value order`() {
        val s = PlanetSocietyState().apply {
            onChildHarmed(); onPredatorRepelledNearChild(); onHatchlingKilled(); onApexKilled()
        }
        val l = line(s)
        assertTrue(l.contains("幼体を傷害"), l)
        for (tag in listOf("捕食者を排除", "巣を破壊", "首位捕食者を殺害")) assertFalse(l.contains(tag), "$tag :: $l")
    }
}
