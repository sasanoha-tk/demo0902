package io.github.panda17tk.arpg.i18n

import io.github.panda17tk.arpg.planet.PlanetBiome
import io.github.panda17tk.arpg.sim.GoalState
import io.github.panda17tk.arpg.sim.PlanetContext
import io.github.panda17tk.arpg.sim.PlanetSocietyState
import io.github.panda17tk.arpg.sim.PlanetStorySeed
import io.github.panda17tk.arpg.sim.PlanetTemperament
import io.github.panda17tk.arpg.sim.SacredThing
import io.github.panda17tk.arpg.sim.SurfaceGoalKind
import io.github.panda17tk.arpg.sim.SurfaceGoals
import io.github.panda17tk.arpg.sim.SurfaceObjective
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/** v2.159 英語化第4弾(前半): the objective line and the goal chips. */
class LangObjectivesTest {
    private fun assertEnglish(s: String) {
        val t = Lang.tr(s)
        assertTrue(t.none { it.code >= 0x2E80 && it != '　' }, "still CJK: 「$s」 -> 「$t」")
    }

    private val childrenCtx = PlanetContext(PlanetTemperament.GENTLE, SacredThing.CHILDREN, PlanetStorySeed.NONE)
    private val apexCtx = PlanetContext(PlanetTemperament.ANCIENT, SacredThing.APEX, PlanetStorySeed.NONE)

    @Test fun `every surface objective line speaks English on every biome`() {
        Lang.en = true
        try {
            for (biome in PlanetBiome.entries) {
                assertEnglish(SurfaceObjective.hudLine(biome, 3))
                assertEnglish(SurfaceObjective.hudLine(biome, 0))
                assertEnglish(SurfaceObjective.hudLine(biome, 0, PlanetSocietyState(leaderDefeated = true)))
                assertEnglish(SurfaceObjective.hudLine(biome, 0, PlanetSocietyState(childKilled = true), childrenCtx))
                assertEnglish(SurfaceObjective.hudLine(biome, 0, PlanetSocietyState(childKilled = true)))
                assertEnglish(SurfaceObjective.hudLine(biome, 0, PlanetSocietyState(childHarmed = true), childrenCtx))
                assertEnglish(SurfaceObjective.hudLine(biome, 0, PlanetSocietyState(childHarmed = true)))
                assertEnglish(SurfaceObjective.hudLine(biome, 0, PlanetSocietyState(hostility = 0.7f)))
                assertEnglish(SurfaceObjective.hudLine(biome, 0, PlanetSocietyState(mercy = 0.6f)))
                assertEnglish(SurfaceObjective.hudLine(biome, 0, PlanetSocietyState(apexKilled = true), apexCtx))
                assertEnglish(SurfaceObjective.hudLine(biome, 0, PlanetSocietyState(apexKilled = true)))
                assertEnglish(SurfaceObjective.hudLine(biome, 0, PlanetSocietyState(predatorKilledNearChild = true)))
                assertEnglish(SurfaceObjective.hudLine(biome, 0, PlanetSocietyState(hatchlingKilled = true)))
            }
        } finally { Lang.en = false }
    }

    @Test fun `every goal chip speaks English in both faiths`() {
        Lang.en = true
        try {
            for (kind in SurfaceGoalKind.entries) for (st in GoalState.entries) {
                assertEnglish(SurfaceGoals.chip(kind, st, PlanetContext.NEUTRAL))
                assertEnglish(SurfaceGoals.chip(kind, st, apexCtx))
            }
        } finally { Lang.en = false }
    }

}
