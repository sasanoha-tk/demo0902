package io.github.panda17tk.arpg.sim

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/** v2.155: the memory-core visit latch — a fresh surface starts armed and records nothing. */
class RoadToEndingTest {
    @Test fun `the core visit arms fresh and starts unvisited`() {
        val ws = WorldState()
        assertTrue(ws.coreArmed, "a fresh surface starts armed")
        assertTrue(!ws.coreVisited, "a fresh visit records nothing")
    }
}
