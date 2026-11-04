package io.github.panda17tk.arpg.sim

import io.github.panda17tk.arpg.planet.PlanetBiome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PlanetScanTest {
    private fun planet(id: Long = 42L, biome: PlanetBiome = PlanetBiome.NATURE, context: PlanetContext? = null) =
        PlanetBody(0f, 0f, 100f, 120f, 484f, biome, id, context ?: PlanetContext.contextFor(id, biome))

    @Test fun `the same planet always scans the same`() {
        val a = PlanetScan.cardFor(planet(), known = false, memory = PlanetSocietyState())
        val b = PlanetScan.cardFor(planet(), known = false, memory = PlanetSocietyState())
        assertEquals(a, b)
    }

    @Test fun `an unvisited planet reads 未訪問`() {
        val card = PlanetScan.cardFor(planet(), known = false, memory = PlanetSocietyState())
        assertEquals(PlanetScan.UNVISITED, card.memoryLine)
    }

    @Test fun `a known planet prints its remembered gauges as numbers`() {
        val memory = PlanetSocietyState(hostility = 0.4f, mercy = 0.2f)
        val card = PlanetScan.cardFor(planet(), known = true, memory = memory)
        assertEquals("再訪　敵対 40　好意 20", card.memoryLine)
    }

    @Test fun `an empty remembered visit still prints zeros`() {
        val card = PlanetScan.cardFor(planet(), known = true, memory = PlanetSocietyState())
        assertEquals("再訪　敵対 0　好意 0", card.memoryLine)
    }

    @Test fun `the card carries exactly three body lines`() {
        val ctx = PlanetContext(PlanetTemperament.GENTLE, SacredThing.CHILDREN, PlanetStorySeed.LOST_CHILD)
        val card = PlanetScan.cardFor(planet(context = ctx), known = false, memory = PlanetSocietyState())
        assertEquals(3, card.lines.size) // trait + danger + memory — no omen line any more
    }

    @Test fun `the card is composed from the lexicon and the biome name`() {
        val ctx = PlanetContext(PlanetTemperament.VENGEFUL, SacredThing.APEX, PlanetStorySeed.LOST_CHILD)
        val card = PlanetScan.cardFor(planet(biome = PlanetBiome.MAGMA, context = ctx), known = false, memory = PlanetSocietyState())
        assertEquals(PlanetBiome.MAGMA.displayName, card.title)
        assertTrue(card.traitLine.contains(PlanetLexicon.temperament(PlanetTemperament.VENGEFUL)))
        assertTrue(card.traitLine.contains(PlanetLexicon.sacred(SacredThing.APEX)))
    }
}
