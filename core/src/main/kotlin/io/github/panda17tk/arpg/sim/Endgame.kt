package io.github.panda17tk.arpg.sim

/**
 * v2.93 エンディング — the run's closing screens. SyncRestoration caps at 99; the control core
 * surfaces once 外部同期 ≥ [THRESHOLD]. Pure rules + a numeric summary: pages → choice → close.
 */
object Endgame {
    /** 外部同期 at which the core surfaces (v2.155: 99→90 — most runs never crossed 99). The cap
     *  stays 99, so 外部同期 never reaches 100. */
    const val THRESHOLD = 90

    fun ready(syncPercent: Int): Boolean = syncPercent >= THRESHOLD

    /** Where the control core rests, just off the gate's shoulder. */
    fun corePos(gate: Pair<Float, Float>): Pair<Float, Float> = gate.first + 150f to gate.second - 90f

    /** The closing screens: ALWAYS 3 pages (numbers → numbers → the choice), so the caller's
     *  stage arithmetic cannot shift while the run is on screen. Tap advances; the last page
     *  stays behind the choice buttons (stage `pages.size + 1`), the epilogue after it. */
    fun pages(wave: Int, stardust: Int, syncPercent: Int, clears: Int): List<List<String>> = listOf(
        listOf("到達波 $wave", "所持 星屑 $stardust", "外部同期 $syncPercent%"),
        listOf("クリア回数 $clears", if (clears >= 1) "NG+ 解放済み" else "NG+ 未解放"),
        listOf("外部同期 残り 1%", "選択"),
    )

    // The choice buttons (v2.115: the labels are the contract — Modals draws, taps compare).
    const val CHOICE_SLEEP = "同期を完了して、眠りにつく"
    const val CHOICE_DRIFT = "切断して、漂流を続ける"
    const val CHOICE_UNBIND = "網を解いて、朝を返す"
    const val CHOICE_RECKON = "刻まれた名を受け入れる"

    /** v2.185: opens when no young was harmed on ANY remembered world and ≥3 worlds hold 好意 ≥ 0.5. */
    fun gentlePathOpen(memories: Collection<PlanetSocietyState>): Boolean {
        if (memories.isEmpty()) return false
        val noYoungHarmed = memories.none { it.childHarmed || it.childKilled || it.hatchlingKilled }
        val trusted = memories.count { it.mercy >= 0.5f }
        return noYoungHarmed && trusted >= 3
    }

    /** v2.189: opens IN PLACE OF the gentle door — young slain somewhere and ≥3 worlds at 敵対 ≥ 0.5.
     *  The two conditions are exclusive (one demands no young harmed, the other demands blood). */
    fun bloodPathOpen(memories: Collection<PlanetSocietyState>): Boolean {
        if (memories.isEmpty()) return false
        val slewYoung = memories.any { it.childKilled }
        val turned = memories.count { it.hostility >= 0.5f }
        return slewYoung && turned >= 3
    }

    /** The close after a choice: the label that was picked + the run's numbers (then the title). */
    fun epilogue(choice: String, wave: Int, stardust: Int, clears: Int): List<String> = listOf(
        "同期完了", "選択 $choice", "到達波 $wave", "所持 星屑 $stardust", "クリア回数 $clears",
    )

    /** The banner left behind when the run continues after 切断 (the core is gone from that sky). */
    const val DRIFT_LINE = "切断した（同期停止）"
}
