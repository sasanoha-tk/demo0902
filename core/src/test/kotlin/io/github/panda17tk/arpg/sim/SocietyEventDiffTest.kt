package io.github.panda17tk.arpg.sim

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SocietyEventDiffTest {
    @Test fun `a rising flag emits exactly one event`() {
        val before = PlanetSocietyState()
        val after = PlanetSocietyState(relicClaimed = true)
        val events = SocietyEventDiff.diff(before, after)
        assertEquals(1, events.size)
        assertEquals(PlanetEvent("遺物持ち出し　敵対上昇", EventKind.HOSTILE), events[0])
    }

    @Test fun `no change emits nothing`() {
        val s = PlanetSocietyState(childKilled = true, hostility = 0.8f)
        assertTrue(SocietyEventDiff.diff(s.copyState(), s).isEmpty())
    }

    @Test fun `several deeds in one tick come out in severity order`() {
        val before = PlanetSocietyState()
        val after = PlanetSocietyState(childKilled = true, childHarmed = true, apexKilled = true, relicClaimed = true)
        val events = SocietyEventDiff.diff(before, after)
        assertEquals(
            listOf(
                PlanetEvent("子殺し　敵対上昇", EventKind.HOSTILE),
                PlanetEvent("最上位捕食者の死　生態攪乱上昇", EventKind.ECOLOGY),
                PlanetEvent("遺物持ち出し　敵対上昇", EventKind.HOSTILE),
            ),
            events,
        )
    }

    @Test fun `a killed child does not double-report the harm`() {
        val before = PlanetSocietyState()
        val after = PlanetSocietyState(childKilled = true, childHarmed = true)
        val events = SocietyEventDiff.diff(before, after)
        assertEquals(1, events.size)
        assertEquals(PlanetEvent("子殺し　敵対上昇", EventKind.HOSTILE), events[0])
    }

    @Test fun `a harmed-but-alive child still reports the harm`() {
        val before = PlanetSocietyState()
        val after = PlanetSocietyState(childHarmed = true)
        assertEquals(listOf(PlanetEvent("子に危害　敵対上昇", EventKind.HOSTILE)), SocietyEventDiff.diff(before, after))
    }

    @Test fun `predator repelled and leader defeated land in the right colour families`() {
        val before = PlanetSocietyState()
        val after = PlanetSocietyState(predatorKilledNearChild = true, leaderDefeated = true)
        val events = SocietyEventDiff.diff(before, after)
        assertEquals(PlanetEvent("捕食者撃退　好意上昇", EventKind.MERCY), events[0])
        assertEquals(PlanetEvent("指導者撃破　素材回収", EventKind.NEUTRAL), events[1])
    }

    @Test fun `hostility fires once when crossing the threshold and never again past it`() {
        val crossing = SocietyEventDiff.diff(PlanetSocietyState(hostility = 0.55f), PlanetSocietyState(hostility = 0.65f))
        assertEquals(listOf(PlanetEvent("敵対度　臨界越え", EventKind.HOSTILE)), crossing)
        val past = SocietyEventDiff.diff(PlanetSocietyState(hostility = 0.65f), PlanetSocietyState(hostility = 0.9f))
        assertTrue(past.isEmpty())
    }

    @Test fun `mercy fires once when crossing its threshold`() {
        val crossing = SocietyEventDiff.diff(PlanetSocietyState(mercy = 0.4f), PlanetSocietyState(mercy = 0.5f))
        assertEquals(listOf(PlanetEvent("好意度　臨界越え", EventKind.MERCY)), crossing)
    }
}
