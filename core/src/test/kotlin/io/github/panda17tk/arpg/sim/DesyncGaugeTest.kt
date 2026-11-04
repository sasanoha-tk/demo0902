package io.github.panda17tk.arpg.sim

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/** v2.50 同期ズレゲージ: stability decay with the wave counter. */
class DesyncGaugeTest {
    @Test fun `stability decays with the surge level but never dies`() {
        assertEquals(100, DesyncGauge.stability(1))
        assertTrue(DesyncGauge.stability(10) < DesyncGauge.stability(2))
        assertEquals(5, DesyncGauge.stability(999))
    }
}
