package io.github.panda17tk.arpg.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ScreenViewport
import io.github.panda17tk.arpg.App
import io.github.panda17tk.arpg.audio.Ambience
import io.github.panda17tk.arpg.audio.AmbientTrack
import io.github.panda17tk.arpg.audio.Sfx
import io.github.panda17tk.arpg.i18n.Lang
import io.github.panda17tk.arpg.input.Haptics
import io.github.panda17tk.arpg.math.Rng
import io.github.panda17tk.arpg.render.Fonts
import io.github.panda17tk.arpg.save.PerfHud
import io.github.panda17tk.arpg.save.SaveSlots
import io.github.panda17tk.arpg.save.TuneMode
import io.github.panda17tk.arpg.sim.Difficulty
import io.github.panda17tk.arpg.ui.SettingsPanel
import io.github.panda17tk.arpg.ui.SlotPanel
import io.github.panda17tk.arpg.ui.TitleFx
import io.github.panda17tk.arpg.ui.TitleLayout
import io.github.panda17tk.arpg.ui.UiButton

/**
 * The front door: a drifting star field, the name, and the ways into the simulation.
 * Systems-only build: nothing here but the ways into the simulation and its settings.
 */
class TitleScreen(private val app: App) : ScreenAdapter() {
    private lateinit var shapes: ShapeRenderer
    private lateinit var batch: SpriteBatch
    private lateinit var viewport: ScreenViewport
    private val glyph = GlyphLayout()
    private val tmp = Vector3()
    private var t = 0f
    private var hasSave = false
    private var showSettings = false
    private var leftyOn = false
    private var hintsOn = true
    private var shakeOn = true
    private var softFlash = false
    private var assistOn = true
    private var langEn = false
    private var volume = 1f
    private var difficulty = Difficulty.NORMAL
    private var showSlots = false
    private var slotsFresh = false
    private var showTunePad = false
    private var tuneCode = ""

    private data class Star(val fx: Float, val fy: Float, val size: Float, val speed: Float)
    private val stars: List<Star> = run {
        val r = Rng(7L)
        List(110) { Star(r.nextFloat(), r.nextFloat(), 0.8f + r.nextFloat() * 1.8f, 2f + r.nextFloat() * 10f) }
    }

    private val cStar = Color(0.85f, 0.9f, 1f, 0.7f)
    private val cNebulaA = Color(0.30f, 0.40f, 0.75f, 0.05f)
    private val cNebulaB = Color(0.55f, 0.35f, 0.70f, 0.04f)
    private val cTwinkle = Color()
    private val cPlanet = Color(0.10f, 0.14f, 0.24f, 1f)
    private val cPlanetRim = Color(0.35f, 0.55f, 0.85f, 0.25f)
    private val cGate = Color(0.35f, 0.8f, 1f, 0.35f)
    private val cSub = Color(0.62f, 0.68f, 0.80f, 1f)
    private val cCrash = Color(1f, 0.5f, 0.45f, 0.85f)
    private var lastCrash = ""

    override fun show() {
        shapes = ShapeRenderer()
        batch = SpriteBatch()
        val uiScale = Gdx.graphics.density.coerceIn(1f, 4f)
        Fonts.load(uiScale)
        viewport = ScreenViewport()
        viewport.setUnitsPerPixel(1f / uiScale)
        viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
        hasSave = SaveSlots.hasAny()
        loadSettings()
        Ambience.setMaster(volume)
        Ambience.setEnabled(Sfx.enabled)
        Ambience.play(AmbientTrack.TITLE)
    }

    private fun loadSettings() {
        try {
            val sp = Gdx.app.getPreferences("drift-settings")
            Sfx.enabled = sp.getBoolean("soundOn", true)
            Haptics.enabled = sp.getBoolean("hapticsOn", true)
            leftyOn = sp.getBoolean("leftHanded", false)
            hintsOn = sp.getBoolean("controlHintsOn", true)
            shakeOn = sp.getBoolean("shakeOn", true)
            softFlash = sp.getBoolean("softFlash", false)
            assistOn = sp.getBoolean("aimAssist", true)
            langEn = sp.getBoolean("langEn", false)
            volume = sp.getFloat("masterVolume", 1f).coerceIn(0f, 1f)
            difficulty = Difficulty.byName(sp.getString("difficulty", "NORMAL"))
            PerfHud.enabled = sp.getBoolean("perfHud", false)
            lastCrash = sp.getString("lastCrash", "")
        } catch (_: Throwable) { /* defaults stay on */ }
        Lang.en = langEn
        Sfx.volume = volume
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        t += delta
        ScreenUtils.clear(0.03f, 0.04f, 0.07f, 1f)
        viewport.apply()
        val w = viewport.worldWidth
        val h = viewport.worldHeight
        val buttons = TitleLayout.buttons(w, h, hasSave)

        Gdx.gl.glEnable(GL20.GL_BLEND)
        shapes.projectionMatrix = viewport.camera.combined
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        val minDim = minOf(w, h)
        for ((ci, c) in TitleFx.CLOUDS.withIndex()) {
            val r = c.fr * minDim
            val x = (c.fx * w + t * c.speed) % (w + r * 2f) - r
            shapes.color = if (ci % 2 == 0) cNebulaA else cNebulaB
            shapes.circle(x, c.fy * h, r, 40)
            shapes.circle(x + r * 0.55f, c.fy * h - r * 0.25f, r * 0.7f, 32)
        }
        for ((i, s) in stars.withIndex()) {
            val x = (s.fx * w + t * s.speed) % w
            cTwinkle.set(cStar)
            cTwinkle.a = cStar.a * TitleFx.twinkle(i, t)
            shapes.color = cTwinkle
            shapes.circle(x, s.fy * h, s.size, 6)
        }
        TitleFx.meteorAt(t)?.let { m ->
            val fade = 1f - m.p
            for (k in 0..3) {
                val back = k * 0.045f
                cTwinkle.set(0.85f, 0.93f, 1f, (0.65f - k * 0.16f) * fade)
                shapes.color = cTwinkle
                shapes.circle((m.fx - m.dirX * back) * w, (m.fy - m.dirY * back) * h, 2.4f - k * 0.5f, 8)
            }
        }
        shapes.color = cPlanetRim
        shapes.circle(w * 0.12f, -h * 0.28f, h * 0.46f, 64)
        shapes.color = cPlanet
        shapes.circle(w * 0.12f, -h * 0.28f, h * 0.45f, 64)
        for (k in 0 until 5) {
            val a = t * 0.04f + k * 1.3f
            val lx = w * 0.12f + kotlin.math.cos(a) * h * 0.42f
            val ly = -h * 0.28f + kotlin.math.sin(a) * h * 0.42f
            if (ly > 0f && kotlin.math.sin(a) > 0f) {
                cTwinkle.set(0.65f, 0.8f, 1f, 0.35f + 0.15f * TitleFx.twinkle(k, t))
                shapes.color = cTwinkle
                shapes.circle(lx, ly, 1.8f, 6)
            }
        }
        cTwinkle.set(0.55f, 0.75f, 1f, TitleFx.glow(t))
        shapes.color = cTwinkle
        shapes.circle(w / 2f, h * 0.72f, minDim * 0.22f, 48)
        val breath = 0.5f + 0.5f * kotlin.math.sin(t * 0.8f)
        shapes.color = cGate
        shapes.circle(w * 0.82f, h * 0.80f, 16f + 3f * breath, 24)
        shapes.color = Color(0.8f, 0.97f, 1f, 0.8f)
        shapes.circle(w * 0.82f, h * 0.80f, 3f, 10)
        val set = TitleLayout.settingsButton(w, h)
        val dif = TitleLayout.difficultyButton(w, h, difficulty.label)
        val tun = TitleLayout.tuneButton(w, h, if (TuneMode.active) "調整◉" else "調整")
        (buttons + set + dif + tun).forEach { b ->
            shapes.color = Color(0.55f, 0.75f, 1f, 0.22f)
            shapes.rect(b.x - 1.5f, b.y - 1.5f, b.w + 3f, b.h + 3f)
            shapes.color = Color(0.05f, 0.07f, 0.11f, 0.85f)
            shapes.rect(b.x, b.y, b.w, b.h)
        }
        shapes.end()

        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        val title = Fonts.title
        val tx = title.data.scaleX; val ty = title.data.scaleY
        title.data.setScale(tx * 1.5f, ty * 1.5f)
        glyph.setText(title, "drift")
        title.draw(batch, glyph, (w - glyph.width) / 2f, h * 0.74f)
        title.data.setScale(tx, ty)
        val font = Fonts.ui
        font.color = Color.WHITE
        (buttons + set + dif + tun).forEach { b ->
            glyph.setText(font, Lang.tr(b.label))
            font.draw(batch, glyph, b.centerX - glyph.width / 2f, b.centerY + glyph.height / 2f)
        }
        batch.end()

        if (showSettings) drawSettings(w, h)
        if (showTunePad) drawTunePad(w, h)
        if (showSlots) drawSlots(w, h)
        if (lastCrash.isNotEmpty()) {
            batch.begin()
            val cf = Fonts.ui
            cf.color = cCrash
            drawFitted(cf, "前回のエラー: $lastCrash", w / 2f, 14f, w - 20f, scale = 0.7f)
            cf.color = Color.WHITE
            batch.end()
        }
        handleInput(buttons, set, dif, tun)
    }

    private fun drawSlots(w: Float, h: Float) {
        val rows = SlotPanel.rows(w, h)
        val close = SlotPanel.closeButton(w, h)
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color(0f, 0f, 0f, 0.72f); shapes.rect(0f, 0f, w, h)
        (rows + close).forEach { b ->
            shapes.color = Color(0.55f, 0.75f, 1f, 0.22f)
            shapes.rect(b.x - 1.5f, b.y - 1.5f, b.w + 3f, b.h + 3f)
            shapes.color = Color(0.05f, 0.07f, 0.11f, 0.94f)
            shapes.rect(b.x, b.y, b.w, b.h)
        }
        shapes.end()
        batch.begin()
        val font = Fonts.ui
        font.color = cSub
        drawFitted(font, if (slotsFresh) "どの枠ではじめるか（上書きされる）" else "どの旅をつづけるか", w / 2f, h * 0.72f, w - 48f)
        font.color = Color.WHITE
        rows.forEachIndexed { i, r ->
            val summary = SaveSlots.summary(i)
            if (summary == null && !slotsFresh) font.color = cSub
            drawFitted(font, "スロット${i + 1}　${summary ?: "空き"}", r.centerX, r.centerY + 8f, r.w - 24f)
            font.color = Color.WHITE
        }
        glyph.setText(font, Lang.tr(close.label))
        font.draw(batch, glyph, close.centerX - glyph.width / 2f, close.centerY + glyph.height / 2f)
        batch.end()
    }

    private fun drawSettings(w: Float, h: Float) {
        val btns = SettingsPanel.buttons(w, h)
        val px = 14f; val pw = w - 28f; val pTop = h * 0.88f; val pBot = h * 0.10f
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color(0f, 0f, 0f, 0.72f); shapes.rect(0f, 0f, w, h)
        shapes.color = Color(0.55f, 0.75f, 1f, 0.22f)
        shapes.rect(px - 1.5f, pBot - 1.5f, pw + 3f, (pTop - pBot) + 3f)
        shapes.color = Color(0.05f, 0.07f, 0.11f, 0.94f)
        shapes.rect(px, pBot, pw, pTop - pBot)
        btns.forEach { b ->
            shapes.color = Color(0.55f, 0.75f, 1f, 0.22f)
            shapes.rect(b.x - 1.5f, b.y - 1.5f, b.w + 3f, b.h + 3f)
            shapes.color = Color(0.09f, 0.12f, 0.18f, 0.95f)
            shapes.rect(b.x, b.y, b.w, b.h)
        }
        shapes.end()
        batch.begin()
        val font = Fonts.ui
        font.color = cSub
        drawFitted(font, "設定", w / 2f, pTop - 22f, pw - 24f)
        font.color = Color.WHITE
        btns.forEach { b ->
            val text = when (b.label) {
                SettingsPanel.CLOSE_LABEL -> b.label
                SettingsPanel.VOLUME -> "${Lang.tr(SettingsPanel.VOLUME)}: ${(volume * 100).toInt()}%"
                else -> "${Lang.tr(b.label)}: ${if (toggleState(b.label)) "ON" else "OFF"}"
            }
            if (b.label == SettingsPanel.CLOSE_LABEL) {
                drawFitted(font, text, b.centerX, b.centerY + 9f, b.w - 24f)
            } else {
                drawFitted(font, text, b.centerX, b.centerY + 22f, b.w - 24f)
                font.color = cSub
                drawFitted(font, SettingsPanel.hintFor(b.label), b.centerX, b.centerY - 2f, b.w - 24f, scale = 0.85f)
                font.color = Color.WHITE
            }
        }
        batch.end()
    }

    private fun drawTunePad(w: Float, h: Float) {
        val btns = io.github.panda17tk.arpg.ui.TunePad.buttons(w, h)
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color(0f, 0f, 0f, 0.78f); shapes.rect(0f, 0f, w, h)
        btns.forEach { b ->
            shapes.color = Color(0.55f, 0.75f, 1f, 0.22f)
            shapes.rect(b.x - 1.5f, b.y - 1.5f, b.w + 3f, b.h + 3f)
            shapes.color = Color(0.09f, 0.12f, 0.18f, 0.95f)
            shapes.rect(b.x, b.y, b.w, b.h)
        }
        shapes.end()
        batch.begin()
        val font = Fonts.ui
        font.color = cSub
        drawFitted(font, "調整モード — パスコード", w / 2f, h * 0.74f, w - 48f)
        font.color = Color.WHITE
        drawFitted(font, if (tuneCode.isEmpty()) "----" else "●".repeat(tuneCode.length), w / 2f, h * 0.68f, w - 48f)
        btns.forEach { b ->
            glyph.setText(font, b.label)
            font.draw(batch, glyph, b.centerX - glyph.width / 2f, b.centerY + glyph.height / 2f)
        }
        batch.end()
    }

    private fun persistSettings() {
        try {
            val sp = Gdx.app.getPreferences("drift-settings")
            sp.putBoolean("soundOn", Sfx.enabled)
            sp.putBoolean("hapticsOn", Haptics.enabled)
            sp.putBoolean("leftHanded", leftyOn)
            sp.putBoolean("controlHintsOn", hintsOn)
            sp.putBoolean("shakeOn", shakeOn)
            sp.putBoolean("softFlash", softFlash)
            sp.putBoolean("aimAssist", assistOn)
            sp.putBoolean("langEn", langEn)
            sp.putFloat("masterVolume", volume)
            sp.putString("difficulty", difficulty.name)
            sp.putBoolean("perfHud", PerfHud.enabled)
            sp.flush()
        } catch (_: Throwable) { /* persist best-effort */ }
    }

    private fun toggleState(label: String): Boolean = when (label) {
        SettingsPanel.SOUND -> Sfx.enabled
        SettingsPanel.HAPTICS -> Haptics.enabled
        SettingsPanel.LEFTY -> leftyOn
        SettingsPanel.CONTROL_HINTS -> hintsOn
        SettingsPanel.SHAKE -> shakeOn
        SettingsPanel.SOFT_FLASH -> softFlash
        SettingsPanel.AIM_ASSIST -> assistOn
        SettingsPanel.LANGUAGE -> langEn
        SettingsPanel.PERF -> PerfHud.enabled
        else -> false
    }

    private fun drawFitted(font: com.badlogic.gdx.graphics.g2d.BitmapFont, text: String, cx: Float, y: Float, maxW: Float, scale: Float = 1f) {
        val shown = Lang.tr(text)
        val sx = font.data.scaleX; val sy = font.data.scaleY
        if (scale != 1f) font.data.setScale(sx * scale, sy * scale)
        glyph.setText(font, shown)
        if (glyph.width > maxW) {
            val k = (maxW / glyph.width).coerceAtLeast(0.55f)
            font.data.setScale(sx * scale * k, sy * scale * k)
            glyph.setText(font, shown)
        }
        font.draw(batch, glyph, cx - glyph.width / 2f, y)
        font.data.setScale(sx, sy)
    }

    private fun handleInput(buttons: List<UiButton>, set: UiButton, dif: UiButton, tun: UiButton) {
        if (Gdx.input.justTouched()) {
            tmp.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            viewport.unproject(tmp)
            if (showSlots) {
                val pw = viewport.worldWidth; val ph = viewport.worldHeight
                if (SlotPanel.closeButton(pw, ph).contains(tmp.x, tmp.y)) { showSlots = false; return }
                val idx = SlotPanel.rows(pw, ph).indexOfFirst { it.contains(tmp.x, tmp.y) }
                if (idx >= 0) {
                    val occupied = SaveSlots.summary(idx) != null
                    if (slotsFresh || occupied) {
                        Sfx.play("scan")
                        app.startRun(fresh = slotsFresh, slot = idx)
                    } else {
                        Sfx.play("hit")
                    }
                }
                return
            }
            if (showTunePad) {
                val hit = io.github.panda17tk.arpg.ui.TunePad.buttons(viewport.worldWidth, viewport.worldHeight)
                    .firstOrNull { it.contains(tmp.x, tmp.y) }
                if (hit == null) { showTunePad = false; tuneCode = ""; return }
                when (hit.label) {
                    io.github.panda17tk.arpg.ui.TunePad.ERASE -> tuneCode = tuneCode.dropLast(1)
                    io.github.panda17tk.arpg.ui.TunePad.ENTER -> {
                        if (TuneMode.tryUnlock(tuneCode)) {
                            showTunePad = false; tuneCode = ""
                            Sfx.play("levelup")
                        } else {
                            tuneCode = ""
                            Sfx.play("hit")
                        }
                    }
                    else -> if (tuneCode.length < 8) tuneCode += hit.label
                }
                return
            }
            if (showSettings) {
                val hit = SettingsPanel.buttons(viewport.worldWidth, viewport.worldHeight)
                    .firstOrNull { it.contains(tmp.x, tmp.y) } ?: return
                when (hit.label) {
                    SettingsPanel.CLOSE_LABEL -> showSettings = false
                    SettingsPanel.SOUND -> {
                        Sfx.enabled = !Sfx.enabled
                        Ambience.setEnabled(Sfx.enabled)
                        persistSettings()
                    }
                    SettingsPanel.HAPTICS -> { Haptics.enabled = !Haptics.enabled; persistSettings() }
                    SettingsPanel.LEFTY -> { leftyOn = !leftyOn; persistSettings() }
                    SettingsPanel.CONTROL_HINTS -> { hintsOn = !hintsOn; persistSettings() }
                    SettingsPanel.VOLUME -> {
                        volume = if (volume <= 0f) 1f else ((volume * 4).toInt() - 1) / 4f
                        Sfx.volume = volume
                        Ambience.setMaster(volume)
                        persistSettings()
                        Sfx.play("scan")
                    }
                    SettingsPanel.SHAKE -> { shakeOn = !shakeOn; persistSettings() }
                    SettingsPanel.SOFT_FLASH -> { softFlash = !softFlash; persistSettings() }
                    SettingsPanel.AIM_ASSIST -> { assistOn = !assistOn; persistSettings() }
                    SettingsPanel.LANGUAGE -> { langEn = !langEn; Lang.en = langEn; persistSettings() }
                    SettingsPanel.PERF -> { PerfHud.enabled = !PerfHud.enabled; persistSettings() }
                }
                return
            }
            if (set.contains(tmp.x, tmp.y)) {
                showSettings = true
                Sfx.play("scan")
                return
            }
            if (dif.contains(tmp.x, tmp.y)) {
                difficulty = difficulty.next()
                persistSettings()
                Sfx.play("scan")
                return
            }
            if (tun.contains(tmp.x, tmp.y)) {
                if (TuneMode.active) {
                    TuneMode.active = false
                    Sfx.play("scan")
                } else {
                    showTunePad = true
                    tuneCode = ""
                    Sfx.play("scan")
                }
                return
            }
            val hit = buttons.firstOrNull { it.contains(tmp.x, tmp.y) } ?: return
            when (hit.label) {
                "つづきから" -> { showSlots = true; slotsFresh = false; Sfx.play("scan") }
                "はじめから" -> { showSlots = true; slotsFresh = true; Sfx.play("scan") }
                "旧式戦闘訓練" -> app.startTraining()
            }
            return
        }
        if (showSettings || showTunePad || showSlots) return
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ENTER) ||
            Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.SPACE)
        ) {
            app.startRun(fresh = !hasSave)
        }
    }

    override fun dispose() {
        if (::shapes.isInitialized) shapes.dispose()
        if (::batch.isInitialized) batch.dispose()
    }
}
