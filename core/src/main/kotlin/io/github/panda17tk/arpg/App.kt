package io.github.panda17tk.arpg

import com.badlogic.gdx.Game
import com.badlogic.gdx.Screen
import io.github.panda17tk.arpg.audio.Ambience
import io.github.panda17tk.arpg.screens.GameScreen
import io.github.panda17tk.arpg.screens.TitleScreen

/** Root libGDX application. Boots the title screen; screens switch through here. */
class App : Game() {
    override fun create() {
        io.github.panda17tk.arpg.audio.Sfx.init()
        setScreen(TitleScreen(this))
    }

    /** Enter the game: continue the slot's saved run, or start a fresh one. */
    fun startRun(fresh: Boolean = true, slot: Int = 0) {
        swapTo(GameScreen(startFresh = fresh, slot = slot))
    }

    /** Enter the walled-off combat simulation used to exercise the systems. */
    fun startTraining() {
        swapTo(GameScreen(startInTraining = true))
    }

    /** Back to the front door. */
    fun showTitle() {
        swapTo(TitleScreen(this))
    }

    private fun swapTo(next: Screen) {
        val old = screen
        setScreen(next)
        old?.dispose()
    }

    override fun dispose() {
        super.dispose()
        Ambience.dispose()
        io.github.panda17tk.arpg.audio.Sfx.dispose()
    }
}
