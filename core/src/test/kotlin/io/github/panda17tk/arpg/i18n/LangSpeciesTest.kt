package io.github.panda17tk.arpg.i18n

import io.github.panda17tk.arpg.config.GameConfig
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/** v2.142: species and item names render in EN mode - the HUD, banners and shop use them. */
class LangSpeciesTest {
    @AfterEach fun reset() { Lang.en = false }

    @Test fun `species names translate to their id-derived English`() {
        Lang.en = true
        assertEquals("Tyrant Shark", Lang.tr("暴君鮫"))
        assertEquals("Isle Whale", Lang.tr("島鯨"))
        assertEquals("Fang Wolf", Lang.tr("牙オオカミ"))
        // the kill banner composes its count onto the name slot
        assertEquals("Isle Whale", Lang.tr("島鯨"))
    }

    @Test fun `every roster species name has an EN mapping`() {
        Lang.en = true
        val stillJa = GameConfig().enemies.values
            .map { it.name }
            .filter { name -> Lang.tr(name).any { it.code >= 0x2E80 && it != '　' } }
        assertTrue(stillJa.isEmpty(), "species names still Japanese in EN mode: $stillJa")
    }

    @Test fun `item names translate too`() {
        Lang.en = true
        val stillJa = io.github.panda17tk.arpg.item.ItemCatalog.ALL
            .map { it.name }
            .filter { name -> Lang.tr(name).any { it.code >= 0x2E80 && it != '　' } }
        assertTrue(stillJa.isEmpty(), "item names still Japanese in EN mode: $stillJa")
    }
}
