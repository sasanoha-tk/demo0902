package io.github.panda17tk.arpg.sim

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/** v2.93/2.192: the ending as a system - the threshold, the core's rest, the numeric close. */
class EndgameTest {
    @Test fun `the core surfaces only when the sync tops out`() {
        assertFalse(Endgame.ready(89))
        assertTrue(Endgame.ready(90)) // v2.155 結末への道: the door opens where runs actually reach
        assertEquals(90, Endgame.THRESHOLD, "the cap stays 99 - the last percent is never earned")
    }

    @Test fun `the core rests just off the gate's shoulder`() {
        val (cx, cy) = Endgame.corePos(1000f to 800f)
        assertEquals(1150f, cx)
        assertEquals(710f, cy)
    }

    @Test fun `the close is the run numbers and four choice labels`() {
        // v2.192: no story text survives - pages and the close are built from run numbers
        val pages = Endgame.pages(wave = 12, stardust = 76, syncPercent = 90, clears = 0)
        assertEquals(3, pages.size, "fixed shape - the draw and the tap handler must never disagree")
        assertTrue(pages.all { it.isNotEmpty() && it.all { line -> line.isNotBlank() } })
        assertEquals("到達波 12", pages[0][0])
        assertEquals("所持 星屑 76", pages[0][1])
        assertEquals("外部同期 90%", pages[0][2])
        assertEquals("クリア回数 0", pages[1][0])
        assertEquals("NG+ 未解放", pages[1][1], "the first clear has not happened yet")
        assertTrue(Endgame.pages(12, 76, 90, 1)[1][1].contains("解放"), "NG+ reads unlocked once a clear is on record")
        assertEquals("外部同期 残り 1%", pages[2][0], "the percent still owed is what the choice is about")
        assertEquals("選択", pages[2][1])
        // the four doors are labels, not sentences
        for (c in listOf(Endgame.CHOICE_SLEEP, Endgame.CHOICE_DRIFT, Endgame.CHOICE_UNBIND, Endgame.CHOICE_RECKON))
            assertTrue(c.isNotBlank())
        // v2.93/2.185/2.189: the close names the choice taken, then counts this run
        val epi = Endgame.epilogue(Endgame.CHOICE_DRIFT, wave = 12, stardust = 76, clears = 1)
        assertEquals("同期完了", epi[0])
        assertEquals("選択 ${Endgame.CHOICE_DRIFT}", epi[1])
        assertEquals("到達波 12", epi[2])
        assertEquals("所持 星屑 76", epi[3])
        assertEquals("クリア回数 1", epi[4], "the clear just counted is already in the summary")
        assertTrue(Endgame.DRIFT_LINE.isNotBlank(), "the drift toast still names what happened")
    }

    @Test fun `the gentle path opens only to a clean, trusted record`() { // v2.185 第3の結末
        assertFalse(Endgame.gentlePathOpen(emptyList()), "no journey, no third door")
        val clean = List(4) { PlanetSocietyState().apply { mercy = 0.6f } }
        assertTrue(Endgame.gentlePathOpen(clean), "clean hands + trust opens it")
        val bloodied = List(4) { i -> PlanetSocietyState().apply { mercy = 0.6f; if (i == 0) childKilled = true } }
        assertFalse(Endgame.gentlePathOpen(bloodied), "one harmed child closes it")
        val untrusted = List(4) { PlanetSocietyState() } // no trust earned
        assertFalse(Endgame.gentlePathOpen(untrusted), "no trust, no door")
    }

    @Test fun `the dark path opens on blood, and never together with the gentle one`() { // v2.189
        assertFalse(Endgame.bloodPathOpen(emptyList()), "no journey, no fourth door")
        val cruel = List(4) { PlanetSocietyState().apply { childKilled = true; hostility = 0.7f } }
        assertTrue(Endgame.bloodPathOpen(cruel), "slain young + turned worlds opens it")
        assertFalse(Endgame.gentlePathOpen(cruel), "the two doors are exclusive — blood shuts the gentle one")
        val calm = List(4) { PlanetSocietyState().apply { childKilled = true } } // slain, but worlds not turned
        assertFalse(Endgame.bloodPathOpen(calm), "no turned worlds, no reckoning")
        val clean = List(4) { PlanetSocietyState().apply { mercy = 0.6f } }
        assertFalse(Endgame.bloodPathOpen(clean), "clean hands never face the reckoning")
    }

    @Test fun `the sync arithmetic can actually reach the threshold`() {
        // 8 systems in + 8 planets visited + 5 trusted → (7*12) + 40 + 40 = 99+ (capped at 99)
        val states = List(8) { i -> PlanetSocietyState().apply { if (i < 5) mercy = 0.6f } }
        assertTrue(SyncRestoration.percent(8, states) >= Endgame.THRESHOLD, "the ending is reachable")
    }
}
