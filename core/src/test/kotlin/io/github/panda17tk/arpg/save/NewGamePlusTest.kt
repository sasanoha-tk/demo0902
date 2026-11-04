package io.github.panda17tk.arpg.save

import io.github.panda17tk.arpg.config.WorkshopBoons
import io.github.panda17tk.arpg.ecs.components.Health
import io.github.panda17tk.arpg.ecs.components.Mods
import io.github.panda17tk.arpg.ecs.components.Stamina
import io.github.panda17tk.arpg.ecs.world.WorldFactory
import io.github.panda17tk.arpg.input.InputState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/** v2.104 周回の印: the mark a completed sync leaves on a run — every stat scales by one ratio. */
class NewGamePlusTest {
    @Test fun `the mark scales hp, stamina and the all-stat multipliers`() {
        val plain = WorldFactory.create(InputState(), seed = 3L)
        val marked = WorldFactory.create(InputState(), seed = 3L, boons = WorkshopBoons(allMul = 1.06f))
        val plainHp = with(plain.world) { plain.player[Health].hpMax }
        val markedHp = with(marked.world) { marked.player[Health].hpMax }
        assertEquals(plainHp * 1.06f, markedHp, 1e-2f)
        val plainSta = with(plain.world) { plain.player[Stamina].max }
        val markedSta = with(marked.world) { marked.player[Stamina].max }
        assertEquals(plainSta * 1.06f, markedSta, 1e-2f)
        with(marked.world) {
            val mods = marked.player[Mods]
            assertEquals(1.06f, mods.gunMul, 1e-4f)
            assertEquals(1.06f, mods.meleeMul, 1e-4f)
            assertEquals(1.06f, mods.moveMul, 1e-4f)
        }
    }

    /** The counter the next run's difficulty is read from — it only ever goes up. */
    @Test fun `a completed sync raises the counter the mark is paced by`() {
        val before = Endings.clears
        Endings.recordClear()
        assertEquals(before + 1, Endings.clears)
    }
}
