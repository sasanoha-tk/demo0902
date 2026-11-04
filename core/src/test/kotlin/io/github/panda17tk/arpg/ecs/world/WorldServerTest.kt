package io.github.panda17tk.arpg.ecs.world

import io.github.panda17tk.arpg.config.GameConfig
import io.github.panda17tk.arpg.input.InputState
import io.github.panda17tk.arpg.planet.PlanetBiome
import io.github.panda17tk.arpg.sim.WaveEvents
import io.github.panda17tk.arpg.sim.WorldMode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/** v2.48 惑星サーバー: the custodial roster, the purge roster, and every surface's memory core. */
class WorldServerTest {
    @Test fun `the preservation machinery joined the space roster`() {
        val enemies = GameConfig().enemies
        for (key in listOf("custodian", "indexer", "quarantine")) {
            val def = enemies[key]
            assertNotNull(def, "$key must exist")
            assertEquals("normal", def!!.tier)
            assertNull(def.biome, "$key patrols space")
        }
        val auditor = enemies["auditor"]!!
        assertEquals("midboss", auditor.tier)
        assertTrue(auditor.intelligence >= 0.85f, "the audit process has read every fight")
        // Its summon references a real roster key (the custodian drone).
        val minion = auditor.attacks.firstOrNull { it.type == "summon" }?.minion
        assertNotNull(enemies[minion], "auditor summons a real unit, got $minion")
    }

    @Test fun `every purge key resolves to a space normal`() {
        val enemies = GameConfig().enemies
        for (key in WaveEvents.PURGE_KEYS) {
            val def = enemies[key]
            assertTrue(def != null && def.tier == "normal" && def.biome == null, "$key must be a space normal")
        }
    }

    @Test fun `every surface places its memory core, deterministically`() {
        val a = WorldFactory.create(InputState(), seed = 7L, mode = WorldMode.SURFACE, biome = PlanetBiome.ICE)
        val b = WorldFactory.create(InputState(), seed = 7L, mode = WorldMode.SURFACE, biome = PlanetBiome.ICE)
        assertNotNull(a.worldState.memoryCore)
        assertEquals(a.worldState.memoryCore, b.worldState.memoryCore)
        // A fresh visit has no record yet: the visit flag is down and the approach latch is armed.
        assertTrue(!a.worldState.coreVisited, "a fresh visit records nothing")
        assertTrue(a.worldState.coreArmed, "a fresh visit is armed for the core")
    }

    @Test fun `space has no memory core`() {
        val gw = WorldFactory.create(InputState(), seed = 7L)
        assertNull(gw.worldState.memoryCore)
    }
}
