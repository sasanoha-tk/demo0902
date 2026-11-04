package io.github.panda17tk.arpg.ui

import kotlin.math.min

/** v2.58 タイトル画面: pure geometry — the menu buttons, centered under the logo block. */
object TitleLayout {
    const val BTN_H = 54f
    const val GAP = 14f

    /** Menu buttons, top-down: つづきから (only with a saved run) / はじめから / 旧式戦闘訓練. */
    fun buttons(w: Float, h: Float, hasSave: Boolean): List<UiButton> {
        val labels = buildList {
            if (hasSave) add("つづきから")
            add("はじめから")
            add("旧式戦闘訓練")
        }
        val bw = min(320f, w * 0.72f)
        val top = h * 0.46f
        return labels.mapIndexed { i, lab ->
            UiButton((w - bw) / 2f, top - i * (BTN_H + GAP), bw, BTN_H, lab)
        }
    }

    /** 設定: a quiet corner chip, top-left — every toggle lives behind it. */
    fun settingsButton(w: Float, h: Float): UiButton = UiButton(12f, h - 52f, 92f, 40f, "設定")

    /** 難易度: the run-mode chip under 設定 — taps cycle 安定運転/標準/過負荷. */
    fun difficultyButton(w: Float, h: Float, label: String): UiButton = UiButton(12f, h - 100f, 92f, 40f, label)

    /** v2.98 調整: the passcode door, bottom-left — quiet until you know it's there. */
    fun tuneButton(w: Float, h: Float, label: String): UiButton = UiButton(12f, 12f, 92f, 40f, label)
}
