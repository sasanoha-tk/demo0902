package io.github.panda17tk.arpg.sim

/**
 * v2.50 同期ズレ: the HUD's sector-stability readout, derived from the wave number alone.
 * Stability decays as the surges deepen, and never quite reaches zero: the machines hold on.
 */
object DesyncGauge {
    fun stability(wave: Int): Int = (100 - (wave - 1) * 3).coerceAtLeast(5)
}
