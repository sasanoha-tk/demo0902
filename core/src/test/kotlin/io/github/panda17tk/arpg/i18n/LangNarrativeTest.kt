package io.github.panda17tk.arpg.i18n

import io.github.panda17tk.arpg.sim.Endgame
import io.github.panda17tk.arpg.sim.WaveEvent
import io.github.panda17tk.arpg.sim.WaveEvents
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/** v2.162/2.192: the functional spine in EN - banners, the numeric close, the toasts. */
class LangNarrativeTest {
    private fun assertEnglish(s: String) {
        val t = Lang.tr(s)
        assertTrue(t.none { it.code >= 0x2E80 && it != '　' }, "still CJK: 「$s」 -> 「$t」")
    }

    @Test fun `the close reads in English, composed number lines included`() {
        Lang.en = true
        try {
            // v2.192: the ending is run numbers + a choice label, so the composed lines translate too
            Endgame.pages(12, 76, 90, 1).flatten().forEach(::assertEnglish)
            Endgame.epilogue(Endgame.CHOICE_DRIFT, 12, 76, 1).forEach(::assertEnglish)
            for (c in listOf(Endgame.CHOICE_SLEEP, Endgame.CHOICE_DRIFT, Endgame.CHOICE_UNBIND, Endgame.CHOICE_RECKON)) {
                assertEnglish(c)
                assertEnglish("選択 $c") // the composed line the closing screen draws
            }
            assertEnglish(Endgame.DRIFT_LINE)
            assertEnglish("タップで続ける")
            assertEnglish("タップで記録を閉じる")
        } finally { Lang.en = false }
    }

    @Test fun `the wave calls on the banner speak English`() {
        Lang.en = true
        try {
            // BOUNTY carries a proper name (kept in its own script) — the frame is tested composed
            for (e in WaveEvent.entries) if (e != WaveEvent.BOUNTY && e != WaveEvent.NONE) {
                WaveEvents.announceFor(e, null)?.let(::assertEnglish)
            }
            assertEnglish("⚠ 強大な気配が近づく　大群が接近している") // the boss line joins the event line
            assertEnglish("強敵の気配")
        } finally { Lang.en = false }
    }
}
