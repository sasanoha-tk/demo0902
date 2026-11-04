package io.github.panda17tk.arpg.sim

import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PlanetLexiconTest {
    @Test fun `every temperament has a word`() {
        for (t in PlanetTemperament.values()) {
            assertTrue(PlanetLexicon.temperament(t).isNotBlank(), "blank word for $t")
        }
    }

    @Test fun `every sacred thing has a word`() {
        for (s in SacredThing.values()) {
            assertTrue(PlanetLexicon.sacred(s).isNotBlank(), "blank word for $s")
        }
    }
}
