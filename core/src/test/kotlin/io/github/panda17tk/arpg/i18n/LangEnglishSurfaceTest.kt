package io.github.panda17tk.arpg.i18n

import io.github.panda17tk.arpg.config.GameConfig
import io.github.panda17tk.arpg.config.TuningCatalog
import io.github.panda17tk.arpg.config.TuningExport
import io.github.panda17tk.arpg.config.UpgradesConfig
import io.github.panda17tk.arpg.item.ItemCatalog
import io.github.panda17tk.arpg.item.Market
import io.github.panda17tk.arpg.map.Stages
import io.github.panda17tk.arpg.math.Rng
import io.github.panda17tk.arpg.planet.PlanetBiome
import io.github.panda17tk.arpg.save.SaveSlots
import io.github.panda17tk.arpg.sim.Difficulty
import io.github.panda17tk.arpg.sim.Endgame
import io.github.panda17tk.arpg.sim.PlanetBody
import io.github.panda17tk.arpg.sim.PlanetContext
import io.github.panda17tk.arpg.sim.PlanetLexicon
import io.github.panda17tk.arpg.sim.PlanetSocietyState
import io.github.panda17tk.arpg.sim.PlanetScan
import io.github.panda17tk.arpg.sim.PlanetStorySeed
import io.github.panda17tk.arpg.sim.PlanetTemperament
import io.github.panda17tk.arpg.sim.SacredThing
import io.github.panda17tk.arpg.sim.SurfaceObjective
import io.github.panda17tk.arpg.sim.TakeoffReward
import io.github.panda17tk.arpg.sim.TutorialController
import io.github.panda17tk.arpg.sim.TutorialStep
import io.github.panda17tk.arpg.sim.WaveEvent
import io.github.panda17tk.arpg.sim.WaveEvents
import io.github.panda17tk.arpg.ui.Modals
import io.github.panda17tk.arpg.ui.Onboarding
import io.github.panda17tk.arpg.upgrade.Upgrades
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

/**
 * v2.192.0 drift cut · the systems-only English surface. Every string a player can see must come
 * out as English once [Lang.en] is on, and no line may keep a half-translated fragment of the
 * source (the `ice 女王king` failure mode). This gate walks the catalogs themselves — items, guns,
 * cards, enemies, planets, waves, difficulty, the knob table, HUD chips, dialogs — so a CJK string
 * that never reaches [Lang]'s tables fails here even when a static scan cannot see it.
 *
 * The checks are collected, not thrown: one run reports every dirty line of the surface instead of
 * stopping at the first. [assertClean] closes each test with the full tally, and each defect is
 * printed as an escaped `DEFECT` line so a script can read the whole list out of one gradle run.
 */
class LangEnglishSurfaceTest {

    @AfterEach fun restore() { Lang.en = false }

    private val defects = ArrayList<String>()

    /** What the player sees with English on: no CJK glyph, no full-width space, no typographic quote. */
    private fun see(s: String, where: String): String {
        Lang.en = true
        val out = Lang.tr(s)
        // A line that never held source CJK has nothing to translate, so it cannot be a defect.
        if (!s.any { it.code >= 0x2E80 }) return out
        val bad = out.filter { it.code >= 0x2E80 && it != '　' }
        if (bad.isNotEmpty()) report("$where keeps \"${esc(bad)}\"", out)
        if (out.contains('　')) report("$where keeps a full-width space", out)
        if (out.contains('·')) report("$where renders a token boundary", out)
        if (out.any { it == '’' || it == '‘' || it == '“' || it == '”' }) report("$where uses a typographic quote", out)
        if (!out.any { it.isLetter() || it.isDigit() }) report("$where emptied itself", out)
        return out
    }

    private fun report(msg: String, out: String) {
        defects += "$msg || ${esc(out)}"
    }

    private fun esc(s: String): String = buildString {
        for (c in s) if (c.code > 127) append("\\u%04x".format(c.code)) else append(c)
    }

    /** A clean surface is one where every `see` in this test found nothing. */
    private fun assertClean() {
        assertTrue(defects.isEmpty(), "the English surface is not clean: ${defects.size} defect(s): " +
            defects.take(70).joinToString(" ;; ") + (if (defects.size > 70) " ;; ...(truncated)" else ""))
    }

    @Test fun `every catalog name reads as an English game label`() {
        for (def in ItemCatalog.ALL) {
            val en = see(def.name, "item ${def.id} name")
            if (en.contains('_')) report("item ${def.id} name is not an English label", en)
        }
        for (w in GameConfig().weapons) see(w.name, "weapon ${w.id} name")
        for (u in Upgrades.ALL) {
            see(u.name, "card ${u.id} name")
            assertTrue(u.label.all { it.code < 0x2E80 }, "card ${u.id} keeps CJK in its code name")
        }
        val enemies = GameConfig().enemies
        assertTrue(enemies.size >= 100, "the shipped universe must field its kinds, saw ${enemies.size}")
        for ((id, def) in enemies) {
            val en = see(def.name, "enemy $id name")
            if (en.contains('_') || en.contains('.')) report("enemy $id name is not an English label", en)
        }
        for (stage in Stages.ALL) see(stage.name, "stage name")
        for (gen in Stages.SPACE_GEN) see(gen.name, "space stage name")
        assertClean()
    }

    @Test fun `item and card copy is functional English, not story`() {
        for (def in ItemCatalog.ALL) {
            val en = see(def.desc, "item ${def.id} desc")
            for (banned in listOf("legend", "myth", "rite", "storyteller", "planet master", "remembers", "gratitude"))
                if (en.contains(banned, true)) report("item ${def.id} desc drifts into story: $banned", en)
        }
        val cfg = UpgradesConfig()
        for (u in Upgrades.ALL) {
            val en = see(Upgrades.desc(u, cfg), "card ${u.id} desc")
            if (en.contains("per kill") != (u.id == "lifesteal")) report("card ${u.id} kill wording", en)
        }
        assertClean()
    }

    @Test fun `market stock is legible in English`() {
        val stock = Market.stockFor(7L)
        assertTrue(stock.isNotEmpty(), "the trader has nothing to sell")
        for (item in stock) {
            see(item.name, "stall ${item.id} name")
            see(item.desc, "stall ${item.id} desc")
            assertTrue(Market.priceFor(item, 0.5f) >= 0, "a stall price must be a number: ${item.id}")
        }
        assertEquals(Market.SLOTS, stock.size, "the stall is a fixed grid of slots")
        assertClean()
    }

    @Test fun `planets are labelled by what they are`() {
        for (b in PlanetBiome.values()) {
            val en = see(b.displayName, "biome ${b.name}")
            if (Regex("(?i)planet planet").containsMatchIn(en)) report("biome ${b.name} repeats its own word", en)
        }
        for (t in PlanetTemperament.values()) see(PlanetLexicon.temperament(t), "temperament ${t.name}")
        for (s in SacredThing.values()) see(PlanetLexicon.sacred(s), "sacred ${s.name}")
        see(PlanetLexicon.traitLine(PlanetContext(PlanetTemperament.HUNGRY, SacredThing.KING, PlanetStorySeed.HUNGRY_FOREST)), "trait line")
        val body = PlanetBody(0f, 0f, 90f, 120f, 320f, PlanetBiome.NATURE, 42L,
            PlanetContext(PlanetTemperament.PROUD, SacredThing.CHILDREN, PlanetStorySeed.LOST_CHILD))
        val card = PlanetScan.cardFor(body, known = true, memory = PlanetSocietyState(hostility = 0.2f, mercy = 0.4f))
        see(card.title, "scan title")
        see(card.traitLine, "scan trait line")
        see(card.memoryLine, "scan memory line")
        see(card.dangerLine ?: "", "scan danger line")
        Lang.en = true
        val blank = PlanetScan.cardFor(body.copy(context = PlanetContext.NEUTRAL), known = false, memory = PlanetSocietyState())
        assertEquals(Lang.tr(PlanetScan.UNVISITED), Lang.tr(blank.memoryLine), "an unknown world says the same thing")
        assertTrue(card.memoryLine.contains("0") || Lang.tr(card.memoryLine).contains("20"), "the remembered gauges must survive as numbers")
        assertClean()
    }

    @Test fun `surface goals tell the player what to do`() {
        val plain = see(SurfaceObjective.hudLine(PlanetBiome.ICE, 0), "surface goal chip")
        val hunting = see(SurfaceObjective.hudLine(PlanetBiome.NATURE, 3), "surface goal chip")
        val wounded = see(SurfaceObjective.hudLine(PlanetBiome.GAS, 0, PlanetSocietyState(childKilled = true, hostility = 0.6f)), "surface goal chip")
        val mixed = see(SurfaceObjective.hudLine(PlanetBiome.MAGMA, 2, PlanetSocietyState(hostility = 0.4f, mercy = 0.2f, ecologicalDisruption = 0.1f)), "surface goal chip")
        if (plain.contains("0")) report("a zero gauge must not print", plain)
        if (!hunting.contains("3")) report("the apex count must survive", hunting)
        if (wounded.length <= plain.length) report("a wounded world says more", wounded)
        if (!(mixed.contains("40") && mixed.contains("10"))) report("the gauges print bare 0-100 numbers", mixed)
        assertClean()
    }

    @Test fun `waves speak as mechanics`() {
        val schedule = (1..40).map { WaveEvents.eventFor(it) }
        assertTrue(schedule.any { it == WaveEvent.NONE }, "some waves are ordinary")
        for (e in WaveEvent.values()) {
            val line = WaveEvents.announceFor(e, WaveEvents.bountyName(Rng(1L)))
            if (e == WaveEvent.NONE) { assertEquals(null, line, "an ordinary wave says nothing"); continue }
            see(line ?: "", "wave announce $e")
        }
        for (i in 0 until 8) see(WaveEvents.bountyName(Rng(i.toLong())), "bounty name $i")
        assertClean()
    }

    @Test fun `difficulty reads as an operating mode`() {
        assertEquals(3, Difficulty.values().size, "difficulty is a three-way contract")
        for (d in Difficulty.values()) {
            see(d.label, "difficulty ${d.name} label")
            see(d.hint, "difficulty ${d.name} hint")
        }
        assertEquals(1f, Difficulty.NORMAL.dmgTakenMul, "NORMAL is the universe as designed")
        assertClean()
    }

    @Test fun `the knob table is legible to a tool`() {
        val params = TuningCatalog.paramsFor(GameConfig())
        assertTrue(params.isNotEmpty(), "the tuner has no knobs")
        for (p in params) {
            see(p.name, "tuning param name")
            see(p.display(), "tuning param value")
            see(p.displayDef(), "tuning param default")
        }
        val out = see(TuningExport.render("knobs", params), "tuning export")
        assertTrue(out.contains("Claude"), "the export is made for the tool that reads it: \"$out\"")
        assertTrue(out.contains("="), "the export is a knob table: \"$out\"")
        assertClean()
    }

    @Test fun `endgame and takeoff lines are English`() {
        val pages = Endgame.pages(12, 340, 90, 1)
        assertEquals(3, pages.size, "the closing screens are a fixed three pages")
        pages.forEach { page -> page.forEach { line -> see(line, "endgame page") } }
        see(Endgame.CHOICE_SLEEP, "choice sleep")
        see(Endgame.CHOICE_DRIFT, "choice drift")
        see(Endgame.CHOICE_UNBIND, "choice unbind")
        see(Endgame.CHOICE_RECKON, "choice reckon")
        see(TakeoffReward.TOAST_GIFT, "takeoff toast")
        see(TakeoffReward.TOAST_NOTHING, "takeoff toast nothing")
        assertClean()
    }

    @Test fun `dialog and control labels survive the switch`() {
        val labels = listOf(
            "もう一度", "タイトルへ", "II", "再開", "最初からやり直す", "操作説明", "この星の記憶",
            "訓練を終了", "旧式戦闘訓練", "写真モード",
            "前へ", "次へ", "閉じる", "書き出し", "全て既定へ", "消", "決",
            "離れる", "売る", "棚へ戻る", "戻す",
        )
        for (l in labels) {
            val en = see(l, "dialog label")
            if (en == l && l != "II") report("label was left untranslated", en)
        }
        assertClean()
    }

    @Test fun `save slot summaries are numbers and English words`() {
        val en = see("星系 #127657　宇宙　同期汚染 9", "save slot summary")
        if (!en.contains("127657") || !en.contains("9")) report("the seed and wave must survive", en)
        if (en.contains('第')) report("the slot summary still uses 第", en)
        assertEquals(3, SaveSlots.COUNT, "slots are a three-way contract")
        assertClean()
    }

    @Test fun `society events speak as pressures, not prose`() {
        // v2.192.0 drift cut · a society event line is assembled at runtime from a gauge
        // name and a rise verb joined by a full-width space, so the SUB table has to carry
        // the join as well as both halves or the line comes out as token pidgin.
        val lines = listOf(
            "遺物持ち出し　敵対上昇", "子殺し　敵対上昇",
            "子に危害　敵対上昇", "最上位捕食者の死　生態攪乱上昇",
            "捕食者撃退　好意上昇", "指導者撃破　素材回収",
            "敵対度　臨界越え", "好意度　臨界越え",
            "危険度　", "再訪　",
        )
        for (l in lines) {
            val en = see(l, "society event")
            if (en == l) report("event line was left untranslated", en)
        }
        assertClean()
    }

    /**
     * v2.192.0: the boot diagnostic is the first screen of the game, and the onboarding crawl is
     * the other place that teaches a control — both are UI copy like any other. This test pumps the
     * state machine itself (not the retyped lines), for both control schemes, and pins that each
     * lesson names the control the player actually has: v2.179 retired the desktop's phantom mouse.
     */
    @Test fun `the boot diagnostic teaches its controls in English`() {
        // v2.172: the two buttons ARE the question, so the boot step draws no text at all
        see(Modals.tutorialBootButtons(1280f, 720f)[0].label, "boot run button")
        see(Modals.tutorialBootButtons(1280f, 720f)[1].label, "boot skip button")
        see(Modals.tutorialSkipButton(1280f, 720f).label, "always-there skip button")
        val beaten = listOf(
            TutorialStep.MOVE, TutorialStep.SHOOT, TutorialStep.PICKUP_DUST, TutorialStep.DASH,
            TutorialStep.FIND_PLANET, TutorialStep.LAND, TutorialStep.OBSERVE, TutorialStep.RETURN_PAD,
            TutorialStep.COMPLETE,
        )
        for (touch in listOf(false, true)) {
            val role = if (touch) "stick" else "keyboard"
            val t = TutorialController()
            val trail = ArrayList<TutorialStep>()
            val panels = HashMap<TutorialStep, List<String>>()
            fun teach() {
                val said = t.prompt(touch).map { s ->
                    val en = see(s, "$role diagnostic line")
                    if (en == s) report("diagnostic line was left untranslated", en)
                    if (en.lowercase().contains("mouse")) report("a $role line teaches a mouse", en)
                    en
                }
                panels[t.step] = said
                trail += t.step
            }
            assertEquals(listOf<String>(), t.prompt(touch), "the boot step says nothing but its buttons")
            t.begin(); teach()
            t.onMoved(TutorialController.MOVE_GOAL); teach()
            t.onKill(); teach()
            t.onDustPicked(); teach()
            t.onDash(); teach()
            t.onScan(); teach()
            t.onLanded(); teach()
            t.onSurfaceTick(TutorialController.OBSERVE_TIME); teach()
            t.onTakeoff(); teach()
            assertEquals(beaten, trail, "pumping its own beats walks the diagnostic in order")
            assertTrue(t.done, "the beats must carry the player all the way to the end")
            // the aim and move beats must name THIS player's control, and only that one
            val taught = (panels.getValue(TutorialStep.SHOOT) + panels.getValue(TutorialStep.MOVE))
                .joinToString(" ")
            for (w in if (touch) listOf("stick", "release") else listOf("arrow", "WASD", "K"))
                if (!taught.contains(w)) report("the $role lesson never says $w", taught)
            see(t.completionToast(), "$role completion toast")
            val sk = TutorialController().also { it.skip() }
            see(sk.completionToast(), "$role skipped toast")
            assertTrue(sk.skipped && sk.done, "skipping ends the diagnostic where running does")
            for (s in listOf(0f, 8f, 16f, 24f)) {
                val line = Onboarding.lineFor(s, touch).orEmpty()
                val en = see(line, "$role onboarding line")
                if (en == line) report("onboarding line was left untranslated", en)
                if (en.lowercase().contains("mouse")) report("onboarding teaches a $role mouse", en)
            }
            assertEquals(null, Onboarding.lineFor(Onboarding.END, touch), "onboarding stops speaking")
        }
        assertClean()
    }
}
