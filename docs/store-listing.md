# drift — ストア掲載文ドラフト（日英） / Store Listing Draft (JA & EN)

v2.192.0 時点のドラフト。Google Play の各欄の文字数制限に合わせてある
（アプリ名 30字 / 短い説明 80字 / 詳しい説明 4000字）。公開前に
バージョン表記・スクリーンショット・署名まわりの整備と合わせて見直すこと。

---

## 日本語

### アプリ名（30字以内）

> drift — 漂流と記憶のローグライト

### 短い説明（80字以内）

> 摩擦ゼロの宇宙を慣性で漂い、惑星に降りて生き延びる2Dローグライト。着陸⇄発進、星系移動、3スロット、周回解放。

### 詳しい説明

摩擦ゼロの慣性移動だけで動く小さな船で宇宙を漂い、惑星に降り、
波を凌ぎ、星系ごとの目標を追ってジャンプゲートの鍵を集めます。
着陸と発進は往復でき、地表では摩擦が戻ります。

一度降りた惑星の生態系は宇宙側で変動し、再訪すると地表の状態が
変わります。死因はレガシーとして残り、クリア回数は周回を進めます（1回
ごとに波が2波深く、全増援が1レベル上。3回で上限の+6波/+3レベル。
持ち越しの武器とアイテムは1回と2回で1個ずつ、カウントはアカウント共通）。

**特徴**

- 慣性ドリフトの操船と、着陸してからの2Dアクション。9種の武器と16種の強化カードでビルドを組む
- 165種の敵、6種のボス、15種の中ボス、賞金首、生態系への干渉で変わる波
- 6種のバイオーム、天候、水域、惑星ごとの記憶——再訪すると地表が変わる
- 星系ごとの目標とジャンプゲート、3段階の難易度、死因レガシー、周回解放、3つのセーブスロット
- 惑星ごとの市場と需要係数、行商船、装備の持ち出し判定、合成、消耗品と資料品
- 旧式戦闘訓練（同じ宙域に実際の装備を持ち込む練習。着陸・セーブ・獲得・クリア判定なし）
- 写真モード、ゲームパッド対応、環境音もBGMもすべて実行時生成
- エンディング4種（1ランで並ぶのは3種）。買い切り、広告なし、アプリ内課金なし、通信なし
- 快適性設定（左利き配置・画面の揺れ・閃光の軽減・エイム補助・音量・English表示）

小さな船で、大きな宇宙を。

---

## English

### App name (≤30 chars)

> drift — inertia roguelite

### Short description (≤80 chars)

> Zero-friction inertial drift, planet landings, 165 enemies, 3 slots, 4 endings.

### Full description

You pilot a small ship that obeys nothing but inertia: zero
friction in space, a speed cap that only dash breaks, and friction
that returns the moment you land. Every system has objectives and
jump-gate keys that lead to the next one.

Planets remember you in the mechanical sense: an ecosystem you
landed on shifts while you are away, and a revisit shows the
change. Death leaves a relic behind, and clear counts (which are
counted account-wide) deepen the next sky by up to six waves and
three levels; two carried unlocks arrive at one and two clears.

**Features**

- Inertial drift in space, grounded 2D action after landing: 9 weapons, melee combos, wall placement, 16 upgrade cards
- 165 enemies, 6 bosses, 15 mid-bosses, bounty heads, and waves that respond to what you do to the ecosystem
- 6 biomes, weather, waterways, and a per-planet memory book that changes the surface when you come back
- Per-system objectives, jump gates, 3 difficulty levels, death relics, NG+ (ceiling: three clears), 3 save slots
- Per-planet market with demand coefficients, the wandering trader vessel, gear carry-out checks, crafting, consumables and data samples
- Legacy Combat Drill: a walled training field on the same sky with your real loadout — no landings, saves, gains or clears
- Photo mode, gamepad support, and audio generated entirely at runtime
- 4 endings, 3 offered in each run. Paid once, no ads, no in-app purchases, no network
- Comfort options: left-handed layout, screen-shake and flash reduction, aim assist, volume, English UI

A small ship, a wide universe.

---

## 備考（公開作業のメモ）

- カテゴリ: ゲーム > アクション（ローグライト）
- コンテンツレーティング想定: 12+ 相当（ファンタジー暴力・軽度）。IARCの質問票で確定させる
- 収集データ: なし（セーブはすべて端末内のPreferences。ネットワーク送信なし）
- スクリーンショット: タイトル / 宇宙ドリフト / 地表戦闘 / 行商船 / インベントリ(English表示) / 設定 の6枚を想定
- 表記: ゲーム内のUI文字列の英語表示は全面表面（部分辞書ではない）。源の日本語は `i18n/Lang.kt` の `tr` が一致表と置換列で英訳し、`i18n/LangEnglishSurfaceTest.kt` が英語面に表示される日本語残りを検査
- 未決事項: リリース署名鍵の作成・管理（ユーザー判断待ち）。現状のCIはdebug署名APKのみ
