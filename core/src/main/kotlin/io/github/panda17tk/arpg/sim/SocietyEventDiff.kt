package io.github.panda17tk.arpg.sim

/**
 * Read-only change detection for the surface event feed (LP v2.24). The existing emitters
 * (EcologyEventSystem / PickupSystem) mutate [PlanetSocietyState] directly — and some calls repeat
 * every tick — so the feed fires on **state edges** instead: booleans on their rising edge only,
 * gauges only when they cross a threshold (a later climb past it stays silent).
 *
 * The checks below run in report-priority order (the gravest deed first), and each emits a terse
 * functional line: the deed recorded plus the society gauge it moves. The flavour sentences the
 * feed used to print are gone; the gauges themselves still scale with the planet's context
 * (SocietyImpact), so the numbers stay characterful even though the words are not. Pure.
 */
object SocietyEventDiff {
    const val HOSTILITY_CROSS = 0.6f // hostility crossing this emits the 臨界越え line
    const val MERCY_CROSS = 0.5f     // mercy crossing this emits the 臨界越え line

    fun diff(before: PlanetSocietyState, after: PlanetSocietyState): List<PlanetEvent> {
        val out = ArrayList<PlanetEvent>(2)
        fun add(text: String, kind: EventKind) = out.add(PlanetEvent(text, kind))
        val childKilledNow = !before.childKilled && after.childKilled
        if (childKilledNow) add("子殺し　敵対上昇", EventKind.HOSTILE)
        if (!before.apexKilled && after.apexKilled) add("最上位捕食者の死　生態攪乱上昇", EventKind.ECOLOGY)
        if (!before.nestMotherKilled && after.nestMotherKilled) add("巣の守り手の死　生態攪乱上昇", EventKind.ECOLOGY)
        if (!before.hatchlingKilled && after.hatchlingKilled) add("幼体殺し　生態攪乱上昇", EventKind.ECOLOGY)
        // A killed child also sets childHarmed — the kill line already covers it, so don't double-report.
        if (!before.childHarmed && after.childHarmed && !childKilledNow) add("子に危害　敵対上昇", EventKind.HOSTILE)
        if (!before.predatorKilledNearChild && after.predatorKilledNearChild) add("捕食者撃退　好意上昇", EventKind.MERCY)
        if (!before.relicClaimed && after.relicClaimed) add("遺物持ち出し　敵対上昇", EventKind.HOSTILE)
        if (!before.leaderDefeated && after.leaderDefeated) add("指導者撃破　素材回収", EventKind.NEUTRAL)
        if (before.hostility < HOSTILITY_CROSS && after.hostility >= HOSTILITY_CROSS) add("敵対度　臨界越え", EventKind.HOSTILE)
        if (before.mercy < MERCY_CROSS && after.mercy >= MERCY_CROSS) add("好意度　臨界越え", EventKind.MERCY)
        return out
    }
}
