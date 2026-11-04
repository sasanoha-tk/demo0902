package io.github.panda17tk.arpg.i18n

/**
 * A presentation-layer dictionary: keys stay Japanese in logic, hit-tests, saves and
 * every panel const; tr() maps them to English at the draw funnels only. Every string the UI
 * can draw is in here, so with Lang.en on nothing Japanese reaches the screen. Pure.
 */

object Lang {
    /** Set by the screens from the settings pref; the sim never reads this. */
    var en = false

    fun tr(s: String): String {
        if (!en) return s
        TABLE[s]?.let { return it }
        if (s.none { it.code >= 0x2E80 }) return s // nothing CJK to translate
        cache[s]?.let { return it }
        var out = s
        for ((ja, e) in SUB) if (ja in out) out = out.replace(ja, e)
        if (cache.size >= 256) cache.clear() // v2.121: tiny memo — draw funnels repeat the same lines
        cache[s] = out
        return out
    }

    private val cache = HashMap<String, String>()

    /** v2.142: an English display name from a stable snake_case id ("void_aji" → "Void Aji"). */
    private fun enName(id: String): String {
        val parts = id.split('_').filter { it.isNotEmpty() }
        val ordered = when (parts.firstOrNull()) {
            "thruster", "armor" -> parts.drop(1) + parts.first() // category reads better at the tail
            "gun", "melee", "acc", "use", "mat", "read", "lore" -> parts.drop(1)
            else -> parts
        }
        return ordered.joinToString(" ") { w ->
            if (w.all { it.isDigit() }) "Mk.$w" else w.replaceFirstChar { c -> c.uppercase() }
        }
    }

    private val TABLE: Map<String, String> = mapOf(
        "ユキ" to "Yuki",
        "ガラン" to "Garan",
        "スズ" to "Suzu",
        "ハクア" to "Hakua",
        "ロッカ" to "Rokka",
        "ノクト" to "Noct",
        "ヴェガ" to "Vega",
        "錆の" to "Rust",
        "枯野の" to "Withered",
        "白磁の" to "Porcelain",
        "緋の" to "Crimson",
        "宵の" to "Evening",
        "遠雷の" to "Distant Thunder",
        "調整◉" to "Tuning",
        "軽い星系" to "Light system",
        "多生の星系" to "Swarming system",
        "重い星系" to "Heavy system",
        "物量1.3倍・星屑+1山" to "Quantity 1.3x, dust +1 pile",
        "サブマシンガン" to "SMG",
        "マシンガン" to "Machine Gun",
        "ライフル" to "Rifle",
        "レールガン" to "Railgun",
        "ビーム" to "Beam",
        "グレネード" to "Grenade",
        "クロス" to "Crossbow",
        "ピストル" to "Pistol",
        "ショットガン" to "Shotgun",
        "ハルバード" to "Halberd",
        "濃縮スタミナ剤" to "Concentrated Stamina Agent",
        "スタミナ全回復" to "Full stamina restore",
        "装甲下地" to "Armor Underlay",
        "火山王" to "Volcano King",
        "大鉈" to "Billhook",
        "全開時 推力3倍/巡航2倍/スタミナ消費2倍" to "At full throttle: thrust 3x, cruise 2x, stamina drain 2x",
        "種類:ビーム　貫通+壁爆破　弾薬無限" to "Type: Beam. Pierces walls. Infinite ammo",
        "種類:ビーム　威力+30% 着弾爆風1.5倍" to "Type: Beam. Power +30%. Impact blast 1.5x",
        "種類:SMG　超速連射・低威力　弾薬無限" to "Type: SMG. Ultra-fast fire, low power. Infinite ammo",
        "12\u79d2\u9593 \u81ea\u5df1\u4fee\u5fa9(\u4f7f)" to "Self-repair for 12s (on use)",
        "1.3倍" to "1.3x",
        "12秒間" to "12s",
        "玉座　星屑+200" to "Throne - dust +200",
        "生存者　救助　星屑+40" to "Survivor rescued - dust +40",
        "武器" to "Weapon",
        "持物" to "Items",
        "遠" to "Far",
        "近" to "Near",
        "嵐" to "Storm",
        "門" to "Gate",
        "核" to "Core",
        "商" to "Trader",
        "発" to "Pad",
        "船" to "Ship",
        "遺" to "Wreck",
        "─ 売約済 ─" to "- sold -",
        "書き出し" to "Export",
        "全て既定へ" to "Reset all",
        "消" to "Del",
        "決" to "OK",
        "敵意" to "Hostility",
        "感謝" to "Affinity",
        "乱れ" to "Disruption",
        "最大HP" to "Max HP",
        "最大スタミナ" to "Max stamina",
        "移動速度" to "Move speed",
        "移動速度倍率" to "Move speed multiplier",
        "ダッシュ倍率" to "Dash multiplier",
        "ダッシュ推力" to "Dash thrust",
        "スタミナ消費" to "Stamina drain",
        "スタミナ回復" to "Stamina regen",
        "近接ダメージ" to "Melee damage",
        "近接リーチ" to "Melee reach",
        "近接間隔" to "Melee interval",
        "近接斬撃ダメージ" to "Slash damage",
        "近接弱化しきい値" to "Melee weaken threshold",
        "剣最低スタミナ" to "Min stamina for a blade",
        "近接弱化倍率" to "Melee weaken multiplier",
        "素手ダメージ" to "Bare-hand damage",
        "近接スタミナ消費" to "Melee stamina drain",
        "弾速" to "Bullet speed",
        "弾寿命" to "Bullet lifetime",
        "グレネード弾速" to "Grenade speed",
        "グレネード信管" to "Grenade fuse",
        "自動装填遅延" to "Auto-reload delay",
        "爆発半径" to "Blast radius",
        "爆発ダメージ" to "Blast damage",
        "爆発自傷" to "Self-blast damage",
        "爆発壁ダメージ" to "Wall blast damage",
        "全敵 HP倍率" to "All enemies: HP multiplier",
        "全敵 速度倍率" to "All enemies: speed multiplier",
        "全敵 攻撃力倍率" to "All enemies: attack multiplier",
        "全敵 攻撃間隔倍率" to "All enemies: attack interval multiplier",
        "初回ウェーブ遅延" to "First wave delay",
        "幕間" to "Intermission",
        "基本物量" to "Base quantity",
        "物量/波" to "Quantity/wave",
        "物量上限" to "Quantity cap",
        "敵HP成長/波" to "Enemy HP growth/wave",
        "敵速度成長/波" to "Enemy speed growth/wave",
        "同時生存 基本" to "Concurrent alive (base)",
        "同時生存/波" to "Concurrent alive/wave",
        "同時生存上限" to "Concurrent alive cap",
        "湧き間隔 基本" to "Spawn interval (base)",
        "湧き間隔短縮/波" to "Spawn interval shrink/wave",
        "湧き間隔下限" to "Spawn interval floor",
        "中ボス周期" to "Midboss cycle",
        "ボス周期" to "Boss cycle",
        "分離半径" to "Separation radius",
        "低HP鈍化倍率" to "Low-HP slowdown multiplier",
        "徘徊速度倍率" to "Roam speed multiplier",
        "徘徊詰まり" to "Roam congestion",
        "経路再計算間隔" to "Path recompute interval",
        "接触ノックバック" to "Contact knockback",
        "接触無敵時間" to "Contact invulnerability",
        "群れ結集" to "Flock cohesion",
        "群れ整列" to "Flock alignment",
        "群れ分離" to "Flock separation",
        "群れ逃走" to "Flock flee",
        "群れ徘徊" to "Flock roam",
        "噛みつき溜め" to "Bite wind-up",
        "帰還刃" to "Return Blade",
        "弾薬調達" to "Ammo Cache",
        "築城術" to "Fortification",
        "装填の手際" to "Deft Reload",
        "推進拡張" to "Thrust Extension",
        "縮地の呼吸" to "Shrink-Step Breath",
        "回収の手" to "Gathering Hand",
        "重機関銃" to "Heavy Machine Gun",
        "高出力ビーム" to "High-Output Beam",
        "マークスマンライフル" to "Marksman Rifle",
        "大口径ピストル" to "Large-Bore Pistol",
        "ストームマシンガン" to "Storm Machine Gun",
        "ワイドショットガン" to "Wide Shotgun",
        "スラッグショットガン" to "Slug Shotgun",
        "レールライフル" to "Rail Rifle",
        "長銃身レールガン" to "Long-Barrel Railgun",
        "重帰還刃" to "Heavy Return Blade",
        "コンバットナイフ" to "Combat Knife",
        "プラズマブレード" to "Plasma Blade",
        "作業用ランス" to "Work Lance",
        "大型獣の牙" to "Large Beast Fang",
        "振動ブレード" to "Vibrating Blade",
        "破砕ハンマー" to "Crushing Hammer",
        "衝角メイス" to "Ram Mace",
        "吸収ブレード" to "Siphon Blade",
        "収穫鎌" to "Harvest Sickle",
        "単分子エッジ" to "Monomolecular Edge",
        "ランナーブーツ" to "Runner Boots",
        "代謝促進バンド" to "Metabolism Band",
        "精密スコープ" to "Precision Scope",
        "追加プレート" to "Extra Plate",
        "軽量化フィン" to "Weight-Cut Fin",
        "牙のペンダント" to "Fang Pendant",
        "旧式の羅針盤" to "Old Compass",
        "氷の民の小さな偶像" to "Small Ice-Folk Idol",
        "加熱コア" to "Heated Core",
        "遮熱フォイル" to "Heat-Shield Foil",
        "グリップソール" to "Grip Sole",
        "回収磁石" to "Gathering Magnet",
        "自己修復パッチ" to "Self-Repair Patch",
        "ジャイロスタビライザー" to "Gyro Stabilizer",
        "反動制御グリップ" to "Recoil-Control Grip",
        "軽合金フレーム" to "Light-Alloy Frame",
        "集束レンズ" to "Focusing Lens",
        "応急スプレー" to "Field Spray",
        "対衝撃フレーム" to "Impact-Guard Frame",
        "トルクブレード" to "Torque Blade",
        "診断レンズ" to "Diagnostic Lens",
        "修復パッチβ" to "Repair Patch Beta",
        "野戦メディキット" to "Field Medkit",
        "ナノ再生ゲル" to "Nano Regen Gel",
        "保存食『星の粥』" to "Ration 'Star Porridge'",
        "高麗藻ドリンク" to "Laver Drink",
        "軽量化スティム" to "Weight-Cut Stim",
        "反応加速スティム" to "Reaction Stim",
        "煙幕弾" to "Smoke Grenade",
        "足元に煙幕を張る" to "Drops a smoke screen at your feet",
        "携行資材パック" to "Portable Material Pack",
        "全弾薬 +20" to "All ammo +20",
        "全弾薬 +50" to "All ammo +50",
        "弾薬コンテナ" to "Ammo Container",
        "高密度メディゲル" to "Dense Med Gel",
        "戦闘刺激剤" to "Combat Stimulant",
        "資材クレート" to "Material Crate",
        "大型弾薬ケース" to "Large Ammo Case",
        "180秒間マグマダメージ無効" to "Immune to magma damage for 180 s",
        "60秒間マグマダメージ無効" to "Immune to magma damage for 60 s",
        "長時間耐熱コーティング" to "Long Heat-Coat",
        "耐熱コーティング" to "Heat-Coat",
        "防寒コーティング" to "Cold-Coat",
        "携行磁石" to "Portable Magnet",
        "応急再生パック" to "Field Regen Pack",
        "煙幕を張った" to "Smoke screen deployed",
        "HPが回復した" to "HP restored",
        "スタミナが全回復した" to "Stamina fully restored",
        "標準スラスター" to "Standard Thruster",
        "軽量スラスター" to "Light Thruster",
        "大推力スラスター" to "Heavy Thruster",
        "巡航スラスター" to "Cruise Thruster",
        "バーニアスラスター" to "Vernier Thruster",
        "生体腱スラスター" to "Bio-Tendon Thruster",
        "OCスラスター" to "OC Thruster",
        "OCスラスター整備手帳" to "OC Thruster Service Log",
        "巡航+30% 立ち上がりは鈍い" to "Cruise +30%, sluggish ramp-up",
        "立ち上がり+50% 巡航-20%" to "Ramp-up +50%, cruise -20%",
        "行商船が買い取る" to "The trader ship buys it",
        "出発の手紙" to "Letter from Departure",
        "狩人の航海日誌・三日目" to "Hunter's Logbook, Day 3",
        "獣王の壁画の拓本" to "Rubbing of the Beast King mural",
        "氷の民の子守唄" to "Ice Folk Lullaby",
        "遺物の断片の銘" to "Engraving on a relic shard",
        "ガスの僧院の聖句" to "Hymn of the Gas Monastery",
        "死せる星の碑文" to "Inscription of the Dead Star",
        "孤独な星の走り書き" to "Scrawl of the Lonely Star",
        "星渡りの詩" to "Star-Ferry Poem",
        "手配書「星喰い」" to "Wanted Poster: Star Eater",
        "結晶の観測ノート" to "Crystal Observation Notes",
        "『星の粥』のレシピ" to "Recipe for Star Porridge",
        "承認者の日記" to "Approver's Diary",
        "講義録『説明負債について』" to "Lecture: On Explanatory Debt",
        "最後の委員会議事録" to "Minutes of the Last Committee",
        "障害報告書 SYNC-0001" to "Incident Report SYNC-0001",
        "人類保全ポリシー(写し)" to "Human Preservation Policy (copy)",
        "記憶核 仕様断片" to "Memory Core spec shard",
        "研究メモ: 星屑の正体" to "Research Note: the true nature of dust",
        "プロセスログ: 誰かの朝" to "Process Log: someone's morning",
        "保守員の手記 I" to "Maintenance Log I",
        "保守員の手記 II" to "Maintenance Log II",
        "保守員の手記 III" to "Maintenance Log III",
        "碑文『知恵の空洞化』" to "Inscription: The Hollowing of Wisdom",
        "パイロットスーツ" to "Pilot Suit",
        "ジャンプゲート運用規程(抄)" to "Jump Gate Operations Rules (excerpt)",
        "対象の破断面が視える・銃+8%" to "Fracture surfaces of the target become visible; gun +8%",
        "保守機構の外殻" to "Outer shell of the maintenance mechanism",
        "被ダメ-14% 移動-4%・激突無効" to "Dmg taken -14%, move -4%, immune to impacts",
        "近接ダメージ +30%" to "Melee damage +30%",
        "斬撃が波になって飛ぶ　近接 +10%" to "Cuts fly outward as waves; melee +10%",
        "近接 +60% 範囲は狭め" to "Melee +60%, narrow reach",
        "近接 +20% ノックバック2倍" to "Melee +20%, knockback 2x",
        "与ダメージの15%をHPとして回収" to "Recovers 15% of damage dealt as HP",
        "近接 +35% リーチ +20% ノックバック半減" to "Melee +35%, reach +20%, knockback halved",
        "銃ダメージ +10%" to "Gun damage +10%",
        "溶岩の民の炉から取り出した熱源　銃 +8% 近接 +5%" to "Heat source taken from the lava folk furnace; gun +8%, melee +5%",
        "マグマダメージ無効" to "Immune to magma damage",
        "雪で減速せず氷で滑らない" to "No slowing in snow, no sliding on ice",
        "ドロップ品を遠くから引き寄せる" to "Pulls drops in from afar",
        "激突ダメージ無効" to "Immune to impact damage",
        "移動 +12%　代わりに被ダメ +5%" to "Move +12%, but dmg taken +5%",
        "銃 +12%　少し重い(移動 -3%)" to "Gun +12%, a little heavy (move -3%)",
        "回復キット" to "Med Kit",
        "弾薬箱" to "Ammo Box",
        "ゲート鍵の断片" to "Gate key shard",
        "設計どおりの宙域" to "Sector as designed",
        "被ダメ0.7倍・休息回復1.5倍" to "Dmg taken 0.7x, rest heal 1.5x",
        "保守の星系" to "Maintenance system",
        "豊かな星系" to "Fertile system",
        "痩せた星系" to "Barren system",
        "残響の星系" to "Echo system",
        "嵐の星系" to "Storm system",
        "どの旅をつづけるか" to "Choose a save to continue",
        "どの枠ではじめるか（上書きされる）" to "Which slot to start in (it will be overwritten)",
        "調整モード — パスコード" to "Tuning mode - passcode",
        "書き出しに失敗した" to "Export failed",
        "drift 調整パラメータ" to "Drift tuning parameters",
        "訓練環境ではセーブできない" to "Cannot save in the training environment",
        "旅の記録 — 星々はあなたを覚えている" to "Voyage record - the planets remember you",
        "保守員は倒れた — 記録は星に残る" to "Keeper down - the record stays on the planet",
        "移動：左スティック / WASD" to "Move: left stick / WASD",
        "ポーズ：右上ボタン / Esc・P" to "Pause: top-right button / Esc or P",
        "上の帯：▲汚染の深さ　◆残る敵　横線＝宙域の安定" to "Top bar: \u25b2 Desync depth  \u25c6 enemies left  flat line = sector stability",
        "下の段：■資材　★星屑" to "Bottom row: ■ materials  ★ dust",
        // v2.192 systems-only English pass: the whole strings the HUD and the records draw
        "フルスロットル：全開ボタン / O（OCスラスター装備時）" to "Full throttle: full-open button / O (with the OC thruster)",
        "《周回の恵み》還りのレールと周回者の徽章を携えて出撃" to "New Game Plus gear: Returner Rail and Returner Badge",
        "旧式戦闘シミュレーション起動 — 成果は持ち出せない" to "Legacy sim started - results do not carry out",
        "縁の印：色つきひし形＝ランドマーク（輪つき＝門）" to "Edge marks: colored diamond = landmark (ringed = gate)",
        "エイム＆射撃：右スティック / 矢印 + K" to "Aim and shoot: right stick / arrows + K",
        "行商船が襲われている — ならず者を退けろ" to "Trader under attack - drive off the raiders",
        "《周回の恵み》還りのレールを携えて出撃" to "New Game Plus gear: Returner Rail",
        "この宇宙のすべての星があなたを忘れます" to "Every planet in this universe will forget you",
        "行商船を守り抜いた — 店の品を値引き" to "Trader defended - shop stock discounted",
        "武器切替・壁設置：ボタン / 数字" to "Weapon switch and wall build: button / numbers",
        "ランを保存 — やられると消える" to "Save the run - a clear-out wipes it",
        "インベントリ：持物ボタン / I" to "Inventory: gear button / I",
        "左：ハート＝船体　いなずま＝推進" to "Left: heart = hull  bolt = thrust",
        "最上位捕食者の死　生態攪乱上昇" to "Apex dead  Ecological disruption up",
        "ドラッグで移動 — 完了で保存" to "Drag to move - Done saves",
        "巣の守り手の死　生態攪乱上昇" to "Nest mother dead  Ecological disruption up",
        "まだどの星にも知られていない" to "No planet knows you yet",
        "市は惑星の地表でのみ開かれる" to "The market opens only on a planet surface",
        "封じられた遺構に踏み入った" to "Entered the sealed ruins",
        "この星はあなたと取引しない" to "This planet will not trade with you",
        "通った場所だけが描かれる" to "Only the places you walked are drawn",
        "幼体殺し　生態攪乱上昇" to "Hatchlings slain  Ecological disruption up",
        "遺物持ち出し　敵対上昇" to "Relic carried off  Hostility up",
        "タップで使う / 合成" to "Tap to use / Craft",
        "指導者撃破　素材回収" to "Leader down  Materials recovered",
        "捕食者撃退　好意上昇" to "Predator repelled  Goodwill up",
        "訓練シミュレーション" to "Training sim",
        "暴君の縄張りに入った" to "Tyrant territory entered",
        "訓練環境を終了した" to "Training sim ended",
        "子に危害　敵対上昇" to "Child harmed  Hostility up",
        "巣の守り手を倒した" to "Nest mother slain",
        "遺物は取っていない" to "Relic not taken",
        "子殺し　敵対上昇" to "Child slain  Hostility up",
        "敵対度　臨界越え" to "Hostility crossed the threshold",
        "好意度　臨界越え" to "Goodwill crossed the threshold",
        "首位捕食者を殺害" to "Apex predator slain",
        "巣の子を失わせた" to "Nest young lost",
        "脱出パッドへ戻れ" to "Return to the escape pad",
        "最上位捕食者の死" to "Apex dead",
        "本当に消しますか" to "Really wipe?",
        "聖なる子を殺害" to "Sacred child slain",
        "聖なる子を傷害" to "Sacred child wounded",
        "記憶コア　到達" to "Memory core reached",
        "素材を回収せよ" to "Gather the materials",
        "捕食者を退けた" to "Predator repelled",
        "巣の守り手の死" to "Nest mother dead",
        "タップ / R" to "Tap / R",
        "HUDの見かた" to "How to read the HUD",
        "星の主を倒した" to "Planet apex defeated",
        "捕食者を殺害" to "Predators slain",
        "捕食者を排除" to "Predators cleared",
        "子を傷つけた" to "Child wounded",
        "生態攪乱上昇" to "Disruption up",
        "遺物持ち出し" to "Relic carried off",
        "タップで交換" to "Tap to swap",
        "幼体を殺害" to "Hatchling slain",
        "神獣を殺害" to "Sacred beast slain",
        "指導者撃破" to "Leader down",
        "捕食者撃退" to "Predator repelled",
        "子を殺した" to "Child killed",
        "幼体を傷害" to "Hatchling wounded",
        "ボタン配置" to "Button layout",
        "セーブした" to "Saved",
        "主を倒した" to "Masters defeated",
        "子らを守れ" to "Protect the children",
        "行商船襲撃" to "Trader raid",
        "星の記憶" to "Planet memory",
        "幼体殺し" to "Hatchlings slain",
        "子に危害" to "Child harmed",
        "通常ラン" to "Normal run",
        "危険度　" to "Danger ",
        "巣を破壊" to "Nest destroyed",
        "執念深い" to "Vengeful",
        "誇り高い" to "Proud",
        "持物は空" to "Inventory empty",
        "主を倒せ" to "Defeat the apexes",
        "汚染深度" to "Desync depth",
        "宙域安定" to "Sector stability",
        "ゲート鍵" to "Gate keys",
        "子殺し" to "Child slain",
        "未訪問" to "Unvisited",
        "再訪　" to "Revisit ",
        "儀式的" to "Ritual",
        "穏やか" to "Calm",
        "静かな" to "Quiet",
        "合成可" to "Craftable",
        "気質　" to "temperament ",
        "神獣" to "Sacred beast",
        "幼体" to "Hatchling",
        "太古" to "Ancient",
        "沈黙" to "Silent",
        "遺跡" to "Ruins",
        "静寂" to "Silence",
        "臆病" to "Timid",
        "発進" to "Launch",
        "着陸" to "Land",
        "星系" to "system",
        "聖　" to "sacred ",
        "星屑" to "dust",
        "星" to "Star ",
        "星間同期復旧 " to "intersystem sync restored ",
        "今回の撃破 " to "kills this run ",
        "間同期復旧 " to "sync restored ",
        "生態攪乱 " to "Disruption ",
        "航行時間 " to "Voyage time ",
        "敵対 " to "Hostile ",
        "好意 " to "Goodwill ",
        // タイトル
        "つづきから" to "Continue",
        "はじめから" to "New Game",
        "旧式戦闘訓練" to "Legacy Combat Drill",
        "記録" to "Records",
        "設定" to "Settings",
        "調整" to "Tuning",
        "安定運転" to "Steady",
        "標準" to "Standard",
        "過負荷" to "Overload",
        // ポーズ / ゲームオーバー / モーダル
        "ポーズ" to "Paused",
        "再開" to "Resume",
        "最初からやり直す" to "Restart",
        "操作説明" to "Controls",
        "この星の記憶" to "This Planet's Memory",
        "訓練を終了" to "End Drill",
        "タイトルへ" to "To Title",
        "宇宙の記憶を消す" to "Erase All Memory",
        "ゲームオーバー" to "Game Over",
        "もう一度" to "Retry",
        "診断する" to "Run Diagnostic",
        "スキップ" to "Skip",
        "消す" to "Erase",
        "戻る" to "Back",
        "大きく" to "Bigger",
        "小さく" to "Smaller",
        "リセット" to "Reset",
        "完了" to "Done",
        "閉じる" to "Close",
        // 設定パネル
        "サウンド" to "Sound",
        "音量" to "Volume",
        "振動" to "Haptics",
        "左利き配置" to "Left-handed",
        "操作ヒント" to "Control Hints",
        "画面の揺れ" to "Screen Shake",
        "閃光をやわらげる" to "Soften Flashes",
        "エイム補助" to "Aim Assist",
        "English表示" to "English UI",
        // 設定のささやき (hintFor)
        "効果音と環境音" to "Sound effects and ambience",
        "タップで 100→75→50→25→0 と巡回" to "Tap cycles 100 > 75 > 50 > 25 > 0",
        "被弾の振動" to "Vibration on hits",
        "スティック右・ボタン左に反転" to "Stick right, buttons left",
        "操作ガイド" to "Control hints",
        "画面シェイクと反動" to "Shake & recoil",
        "撃破の白フラッシュを弱める" to "Dim the white kill flashes",
        "射撃が近くの敵へわずかに寄る" to "Shots lean gently toward foes",
        "主要ラベルを英語で表示" to "Show main labels in English",
        // v2.148 ヒント帯 (hintPanel) — 起動診断
        "起動診断を実行しますか？" to "Run the boot diagnostic?", // v2.171 手直し: the pared-down boot prompt
        "起動診断 1/6　推進制御を確認します" to "Diagnostic 1/6 - checking thrusters",
        "左スティックで船体を移動" to "Move with the left stick",
        "WASD / 方向キーで船体を移動" to "Move with WASD / arrow keys",
        "起動診断 2/6　防衛工具を確認します" to "Diagnostic 2/6 - checking defense tools",
        "右スティックで照準・離して発射　対象を1体破壊" to "Aim with the right stick, release to fire - destroy one target",
        "矢印キーで照準・K で発射　対象を1体破壊" to "Aim with the arrow keys, K to fire - destroy one target", // v2.179
        "矢印キーで照準・K で発射" to "Aim with the arrow keys, K to fire",
        "起動診断 3/6　記憶片を検出: 星屑" to "Diagnostic 3/6 - memory shards detected: dust",
        "回収してください" to "Collect them",
        "起動診断 4/6　緊急推進を確認します" to "Diagnostic 4/6 - checking emergency thrust",
        "DASH ボタンで距離を取る" to "DASH to break away",
        "Shift でダッシュして距離を取る" to "Shift to dash away",
        "起動診断 5/6　近傍に記憶星を検出" to "Diagnostic 5/6 - memory star detected nearby",
        "ナビの矢印へ" to "Follow the nav arrow",
        "起動診断 6/6　接続可能" to "Diagnostic 6/6 - connection ready",
        "惑星かスキャンカードをタップで着陸" to "Tap the planet or its card to land",
        "[L] で着陸" to "[L] to land",
        "地表サーバーに接続　住民出力を確認" to "Connected to the surface server - observe the residents",
        "すべてが敵とは限りません" to "Not everything is hostile",
        "帰還パッドへ戻ると離陸できます" to "Return to the pad to lift off",
        "起動診断をスキップ" to "Boot diagnostic skipped",
        "起動診断 完了" to "Boot diagnostic complete",
        // v2.148 ヒント帯 — オンボーディング
        "左スティックで移動" to "Move with the left stick",
        "WASD で移動" to "Move with WASD",
        "右スティックで照準・離して発射" to "Aim with the right stick, release to fire",
        "DASH でダッシュ" to "DASH to dash",
        "Shift でダッシュ" to "Shift to dash",
        "惑星に近づいて着陸" to "Approach a planet and land",
        // v2.148 ヒント帯 — 宇宙/地表の操作ヒント
        "訓練環境 — 模擬戦闘のみ" to "Training sim - combat drills only",
        "惑星をタップで着陸" to "Tap a planet to land",
        "惑星に近づいて [L] で着陸" to "Approach a planet, [L] to land",
        "[L] 着陸" to "[L] Land",
        "タップで着陸" to "Tap to land",
        "[L] 離陸" to "[L] Lift off",
        "[L] 離陸して宇宙へ" to "[L] Lift off to space",
        // v2.162/v2.185/v2.189 英語化: the closing screens — numbers and the choice
         "同期完了" to "Sync complete",
         "外部同期 残り 1%" to "External sync: 1% left",
         "NG+ 解放済み" to "NG+ unlocked",
         "NG+ 未解放" to "NG+ locked",
         "選択" to "Choose",
         "切断した（同期停止）" to "Disconnected (sync stopped)",
        "タップで続ける" to "Tap to continue",
        "タップで記録を閉じる" to "Tap to close the record",
        "ボタン配置を編集" to "Edit Button Layout", // v2.163
        "海の密度" to "Ocean Density", // v2.165
        "性能表示" to "Performance HUD", // v2.167
        "fps・sim・描画時間を左上に表示" to "Show fps, sim and draw times top-left",
        "宇宙の魚の数（次の空から反映）" to "How many fish fill the sky (applies from the next sky)",
        "高" to "High",
        "中" to "Medium",
        "低" to "Low",
        // 持物 / インベントリ
        "装備" to "Gear",
        "アイテム" to "Items",
        "マップ" to "Map",
        "市" to "Market",
        "セーブ" to "Save",
        "セーブする" to "Save Here",
        "推進器" to "Thruster",
        "防具" to "Armor",
        "遠距離" to "Ranged",
        "近距離" to "Melee",
        "装飾1" to "Charm 1",
        "装飾2" to "Charm 2",
        "装飾3" to "Charm 3",
        // 行商船
        "離れる" to "Leave",
        "売る" to "Sell",
        "棚へ戻る" to "Back to Shelves",
        "前へ" to "Prev",
        "戻す" to "Undo",
        "次へ" to "Next",
        "被ダメージ -15%" to "Damage taken -15%",
        "被ダメージ -30%" to "Damage taken -30%",
        "被ダメージ -45%" to "Damage taken -45%",
        "被ダメ -35% 銃 +5%" to "Damage taken -35%, gun +5%",
        "マグマの熱を完全に遮断　被ダメ -10%" to "Blocks magma heat completely. Damage taken -10%",
        "雪の減速と氷の滑りを無効　被ダメ -10%" to "Unaffected by snow slowdown and ice sliding. Damage taken -10%",
        "移動 +8% 被ダメ -8%" to "Move +8%, damage taken -8%",
        "激突ダメージ無効　被ダメ -20%" to "Immune to impact damage. Damage taken -20%",
        "種類:ピストル　弾薬無限" to "Type: Pistol. Infinite ammo",
        "種類:ショットガン" to "Type: Shotgun.",
        "種類:マシンガン　弾薬無限" to "Type: Machine gun. Infinite ammo",
        "種類:グレネード" to "Type: Grenade.",
        "種類:ピストル　威力+25% 装填-30%" to "Type: Pistol. Power +25%, reload -30%",
        "種類:ピストル　威力+50% 連射+18%" to "Type: Pistol. Power +50%, fire rate +18%",
        "種類:ショットガン　威力+30% 装填-20%" to "Type: Shotgun. Power +30%, reload -20%",
        "種類:ショットガン　ブロック破壊2.5倍 威力+15%" to "Type: Shotgun. Wall break 2.5x, power +15%",
        "種類:マシンガン　連射+43% 威力+10%" to "Type: Machine gun. Fire rate +43%, power +10%",
        "種類:マシンガン　威力+40% ブロック破壊1.5倍 連射は重め" to "Type: Machine gun. Power +40%, wall break 1.5x, heavy handling",
        "種類:グレネード　爆風1.6倍 装填-15%" to "Type: Grenade. Blast radius 1.6x, reload -15%",
        "種類:SMG　威力+20% 装填-25%" to "Type: SMG. Power +20%, reload -25%",
        "種類:ライフル　単発高威力・高精度" to "Type: Rifle. Single shots, high power, high accuracy",
        "種類:ライフル　威力+35% 装填-20%" to "Type: Rifle. Power +35%, reload -20%",
        "種類:ピストル　連射+67% 威力-15%" to "Type: Pistol. Fire rate +67%, power -15%",
        "種類:ピストル　威力+80% 弾速+20% 連射は重い" to "Type: Pistol. Power +80%, bullet speed +20%, heavy handling",
        "種類:ピストル　弾が敵を追尾する" to "Type: Pistol. Bullets home in on enemies",
        "種類:マシンガン　連射+82% 拡散1.8倍 弾幕向き" to "Type: Machine gun. Fire rate +82%, spread 1.8x. Best for covering fire",
        "種類:マシンガン　拡散1/3 弾速+30% 威力+15%" to "Type: Machine gun. Spread 1/3, bullet speed +30%, power +15%",
        "種類:ショットガン　拡散1.7倍の面制圧" to "Type: Shotgun. Spread 1.7x for area control",
        "種類:ショットガン　集弾スラッグ 威力+50% 弾速+40%" to "Type: Shotgun. Slug rounds, tight group, power +50%, bullet speed +40%",
        "種類:SMG　弾が敵を追尾する 威力-10%" to "Type: SMG. Bullets home in on enemies, power -10%",
        "種類:ライフル　弾速2倍 威力+60% ブロック破壊1.5倍" to "Type: Rifle. Bullet speed 2x, power +60%, wall break 1.5x",
        "種類:レールガン　置き撃ち貫通・壁を無視して一直線" to "Type: Railgun. Fired from standstill, pierces, ignores walls",
        "種類:レールガン　威力+30% 装填-15%" to "Type: Railgun. Power +30%, reload -15%",
        "種類:帰還刃　弾薬無限" to "Type: Returning blade. Infinite ammo",
        "種類:帰還刃　威力+40% 投擲は重め" to "Type: Returning blade. Power +40%, heavy throw",
        "リーチ +40%" to "Reach +40%",
        "近接 +50% リーチ +15%" to "Melee +50%, reach +15%",
        "攻撃範囲1.5倍 近接 +20%" to "Attack range 1.5x, melee +20%",
        "範囲1.3倍 リーチ +30% 近接 +15%" to "Range 1.3x, reach +30%, melee +15%",
        "範囲1.8倍 吸収5%" to "Range 1.8x, 5% lifesteal",
        "移動速度 +10%" to "Move speed +10%",
        "スタミナ回復 +30%" to "Stamina recovery +30%",
        "被ダメージ -10%" to "Damage taken -10%",
        "移動 +5% 回復 +10%" to "Move +5%, recovery +10%",
        "近接 +15% 銃 +5%" to "Melee +15%, gun +5%",
        "移動 +6% 回復 +5%" to "Move +6%, recovery +5%",
        "被ダメ -5% 回復 +5%" to "Damage taken -5%, recovery +5%",
        "HPを毎秒 1.5 回復" to "Regenerates 1.5 HP per second",
        "銃 +6% 近接 +3%" to "Gun +6%, melee +3%",
        "被ダメ -7% HP毎秒0.5回復" to "Damage taken -7%, regenerates 0.5 HP per second",
        "HP 25 回復" to "Restores 25 HP",
        "HP 60 回復" to "Restores 60 HP",
        "HP 120 回復" to "Restores 120 HP",
        "HP 15 回復" to "Restores 15 HP",
        "6秒間スタミナ消費なし" to "No stamina drain for 6 seconds",
        "8秒間ダッシュ強化" to "Dash boosted for 8 seconds",
        "壁材 +3" to "Wall material +3",
        "HP 200 回復" to "Restores 200 HP",
        "15秒間ダッシュ強化" to "Dash boosted for 15 seconds",
        "10秒間スタミナ消費なし" to "No stamina drain for 10 seconds",
        "壁材 +8" to "Wall material +8",
        "60秒間 雪の減速と氷の滑りを無効" to "Unaffected by snow slowdown and ice sliding for 60 seconds",
        "45秒間ドロップ品を引き寄せる" to "Pulls drops in for 45 seconds",
        "20秒間HPを毎秒 2.5 回復" to "Regenerates 2.5 HP per second for 20 seconds",
        "星からの餞別を受け取った" to "Received a gift from the star",
        "星は何も与えなかった" to "The star gave you nothing",
        "種類:サブマシンガン　構えているだけでドロップ品が寄ってくる" to "Type: SMG. Drops come to you while aiming",
        "種類:レールガン　周回の証——貫き、ドロップ品を引き寄せる" to "Type: Railgun. Proof of a full clear: pierces, pulls drops in",
        "HP毎秒1.0回復・回復+10%・被ダメ-5%" to "Regenerates 1.0 HP per second, heal +10%, damage taken -5%",
    )

    /** v2.121 英語化第2弾: substring tokens for COMPOSED lines (HUD strip, notes, records).
     *  Applied longest-first so short tokens never bite into longer ones. Names come from the
     *  live catalogs, so a new weapon/card/achievement only needs its English name added here. */
        // v2.192.0 drift cut · p49b: whole strings the UI scan still needs
        // (authored quality over id-derived names, functional identifiers, gauge words)
    private val SUB: List<Pair<String, String>> = buildList {
        add("種類:" to "type: ")
        add("武器種:" to "weapon: ")
        add("ピストル" to "pistol")
        add("ショットガン" to "shotgun")
        add("マシンガン" to "machine gun")
        add("ライフル" to "rifle")
        add("レールガン" to "railgun")
        add("グレネード" to "grenade")
        add("ビーム" to "beam")
        add("中ボス" to "mid-boss")
        add("ボス" to "boss")
        add("アステロイドベルト" to "asteroid belt")
        add("威力" to "power ")
        add("連射間隔" to "fire interval ")
        add("連射" to "fire rate ")
        add("装填" to "reload ")
        add("リロード" to "reload ")
        add("弾速" to "Bullet speed")
        add("弾薬無限" to "infinite ammo")
        add("装弾数" to "ammo ")
        add("発射数" to "shots ")
        add("拡散" to "spread ")
        add("反跳" to "recoil ")
        add("貫通" to "pierce")
        add("爆風" to "blast ")
        add("射程" to "range ")
        add("リーチ" to "reach ")
        add("近接" to "melee ")
        add("射撃" to "gunning ")
        add("被ダメージ" to "dmg taken ")
        add("被ダメ" to "dmg taken ")
        add("攻撃" to "atk ")
        add("防御" to "def ")
        add("回復" to "heal ")
        add("消費" to "cost ")
        add("スタミナ" to "stamina")
        add("移動" to "move ")
        add("速度" to "speed ")
        add("推力" to "thrust ")
        add("耐久" to "durability ")
        add("強度" to "strength ")
        add("価値" to "value ")
        add("価格" to "price ")
        add("重量" to "weight ")
        add("冷却" to "cooldown ")
        add("過熱" to "overheat ")
        add("照準" to "aim ")
        add("安定" to "stability ")
        add("溜め" to "charge ")
        add("星屑" to "dust ")
        add("秒間" to "s of ")
        add("毎秒" to "per sec ")
        add("影響なし" to "no effect from ")
        add("無効" to "immune ")
        add("属性" to "element ")
        add("耐性" to "resist ")
        add("強化" to "boost ")
        add("上昇" to " up ")
        add("低下" to " down ")
        add("自己修復" to "self-repair")
        add("引き寄せる" to "pulls in drops")
        add("減速" to "slow")
        add("滑り" to "slip")
        add("雪と氷" to "snow and ice")
        add("雪の" to "snow ")
        add("氷の" to "ice ")
        add("マグマ" to "magma")
        add("ブロック破壊" to "block break")
        add("ブロック" to "blocks")
        add("ボタン" to "button")
        add("全敵" to "all enemies")
        add("全開" to "full open")
        add("巡航" to "cruise")
        add("物量" to "quantity")
        add("波" to " wave")
        add("第" to "#")
        add("なし" to "none ")
        add("自動" to "auto-")
        add("変換" to "convert")
        add("素材" to "materials")
        add("ドロップ" to "drops")
        add("ドロップ品" to "drop items")
        add("出現" to "spawns")
        add("攻撃力" to "atk ")
        add("HP倍率" to "HP scale")
        add("爆発" to "explosion")
        add("毒" to "poison")
        add("炎" to "flame")
        add("氷" to "ice ")
        add("雷" to "lightning")
        add("溶岩" to "lava")
        add("霧" to "mist")
        add("吹雪" to "blizzard")
        add("静風" to "calm")
        add("雷雨" to "thunderstorm")
        add("晴れ" to "clear")
        add("曇り" to "cloudy")
        add("雨天" to "rain")
        add("雪天" to "snow")
        add("磁気" to "magnetic")
        add("帯電" to "charged")
        add("腐食" to "corrosive")
        add("灰" to "ash")
        add("塵" to "dust")
        add("岩" to "rock")
        add("砂" to "sand")
        add("泥" to "mud")
        add("湿地" to "wetland")
        add("森林" to "forest")
        add("林" to "woods")
        add("草地" to "grassland")
        add("原" to "plain")
        add("砂漠" to "desert")
        add("氷河" to "glacier")
        add("氷床" to "ice sheet")
        add("火山" to "volcano")
        add("隕石" to "meteor")
        add("洞窟" to "cave")
        add("坑道" to "tunnel")
        add("遺跡" to "ruins")
        add("廃墟" to "wrecks")
        add("都市" to "city")
        add("市街" to "urban")
        add("市場" to "market")
        add("交易" to "trade")
        add("航路" to "lanes")
        add("軌道" to "orbit")
        add("衛星" to "moon")
        add("惑星" to "planet")
        add("星系" to "system")
        add("主星" to "primary")
        add("恒星" to "star")
        add("ゲート" to "gate")
        add("門" to "gate")
        add("塔" to "spire")
        add("根城" to "stronghold")
        add("巣" to "nest")
        add("群れ" to "swarm")
        add("群" to "swarm")
        add("単独" to "solo")
        add("パック" to "pack")
        add("臆病" to "timid")
        add("穏やか" to "gentle")
        add("誇り高い" to "proud")
        add("執念深い" to "vengeful")
        add("儀式的" to "ritualistic")
        add("太古" to "ancient")
        add("沈黙" to "silent")
        add("飢えている" to "hungry")
        add("子供たち" to "children")
        add("王" to "king")
        add("神獣" to "sacred beast")
        add("生態攪乱" to "ecosystem disturbance")
        add("攪乱" to "disturbance")
        add("生態" to "ecosystem")
        add("捕食者" to "predator")
        add("首位捕食者" to "apex predator")
        add("牙" to "fangs")
        add("角" to "horns")
        add("爪" to "claws")
        add("触手" to "tentacles")
        add("翼" to "wings")
        add("尾" to "tail")
        add("殻" to "shell")
        add("甲" to "plating")
        add("骨" to "bones")
        add("肉" to "meat")
        add("皮" to "hide")
        add("粘液" to "slime")
        add("膜" to "membrane")
        add("噴出" to "ejects")
        add("吐く" to "spits")
        add("突進" to "charges")
        add("跳躍" to "leaps")
        add("貫く" to "impales")
        add("回避" to "dodge")
        add("転が" to "rolls")
        add("ダッシュ" to "dash")
        add("走る" to "runs")
        add("歩く" to "walks")
        add("浮遊" to "floats")
        add("飛行" to "flight")
        add("泳ぎ" to "swims")
        add("潜る" to "dives")
        add("這う" to "crawls")
        add("塞ぐ" to "blocks")
        add("妨害" to "interference")
        add("邪魔" to "nuisance")
        add("奪う" to "takes")
        add("守る" to "guards")
        add("守り抜" to "defends")
        add("守人" to "guardians")
        add("保守員" to "maintenance crew")
        add("部族" to "tribe")
        add("村" to "village")
        add("町" to "town")
        add("街" to "city")
        add("人々" to "people")
        add("人" to "person")
        add("戦士" to "warrior")
        add("狩人" to "hunter")
        add("戦い" to "fight")
        add("戦闘" to "combat")
        add("襲撃" to "raid")
        add("襲う" to "raids")
        add("襲来" to "incoming")
        add("来襲" to "raid")
        add("侵入" to "incursion")
        add("接近" to "close")
        add("退く" to "retreats")
        add("後退" to "retreat")
        add("展開" to "deploys")
        add("配置" to "layout")
        add("巡回" to "patrol")
        add("索敵" to "scouts")
        add("観測" to "observation")
        add("記録" to "records")
        add("記憶" to "memory")
        add("記憶核" to "memory core")
        add("星の記憶" to "planet memory")
        add("書庫" to "archive")
        add("資料" to "sample")
        add("資料品" to "data sample")
        add("読書" to "reading")
        add("地図" to "chart")
        add("名前" to "name")
        add("名称" to "name")
        add("呼称" to "name")
        add("称号" to "title")
        add("番号" to "number")
        add("記号" to "symbol")
        add("印: " to "mark: ")
        add("残骸" to "wrecks")
        add("死体" to "carcass")
        add("死" to "death")
        add("生" to "life")
        add("命" to "life")
        add("寿命" to "lifespan")
        add("時間" to "time")
        add("距離" to "distance")
        add("深度" to "depth")
        add("高度" to "altitude")
        add("面積" to "area")
        add("体積" to "volume")
        add("段階" to "tier")
        add("範囲" to "range")
        add("領域" to "region")
        add("地帯" to "zone")
        add("方向" to "heading")
        add("方角" to "bearing")
        add("位置" to "position")
        add("座標" to "coords")
        add("斜め" to "diagonal")
        add("中心" to "center")
        add("境界" to "boundary")
        add("壁" to "wall")
        add("床" to "floor")
        add("天井" to "ceiling")
        add("柱" to "pillar")
        add("床面" to "floor")
        add("地面" to "ground")
        add("地上" to "surface")
        add("地表" to "Surface")
        add("地下" to "underground")
        add("宇宙" to "Space")
        add("大気" to "atmosphere")
        add("気圧" to "air pressure")
        add("風" to "wind")
        add("温度" to "temperature")
        add("気候" to "weather")
        add("気象" to "weather")
        add("天気" to "weather")
        add("季節" to "season")
        add("明" to "light")
        add("暗" to "dark")
        add("光" to "light")
        add("影" to "shadow")
        add("色" to "color")
        add("銅" to "copper")
        add("鉄" to "iron")
        add("銀" to "silver")
        add("金" to "gold")
        add("合金" to "alloy")
        add("金属" to "metal")
        add("鉱" to "ore")
        add("石" to "stone")
        add("水晶" to "crystal")
        add("結晶" to "crystal")
        add("琥珀" to "amber")
        add("真珠" to "pearl")
        add("玻璃" to "glass")
        add("ガラス" to "glass")
        add("油" to "oil")
        add("樹脂" to "resin")
        add("繊維" to "fiber")
        add("布" to "cloth")
        add("革" to "leather")
        add("糸" to "thread")
        add("網" to "net")
        add("罠" to "trap")
        add("器具" to "gear")
        add("道具" to "tool")
        add("部品" to "parts")
        add("部件" to "parts")
        add("欠片" to "shards")
        add("かけ" to "scrap")
        add("破片" to "fragments")
        add("火" to "fire")
        add("水" to "water")
        add("電気" to "electric")
        add("放射" to "radiation")
        add("中性" to "neutral")
        add("平衡" to "balance")
        add("同期汚染" to "Desync")
        add("汚染" to "desync")
        add("同期" to "sync")
        add("切断" to "cut")
        add("解除" to "release")
        add("解放" to "free")
        add("返上" to "renounce")
        add("放棄" to "abandon")
        add("獲得" to "gain")
        add("失く" to "lose")
        add("加算" to "add")
        add("乗算" to "multiply")
        add("減算" to "subtract")
        add("変化" to "change")
        add("変更" to "change")
        add("設定" to "settings")
        add("初期" to "default")
        add("既定" to "default")
        add("基準" to "baseline")
        add("比較" to "compare")
        add("現在" to "current")
        add("最後" to "last")
        add("最初" to "first")
        add("最新" to "latest")
        add("済み" to " done")
        add("完了" to "complete")
        add("失敗" to "failed")
        add("成功" to "succeeded")
        add("継続" to "ongoing")
        add("中断" to "interrupted")
        add("途中" to "mid")
        add("開始" to "start")
        add("終了" to "end")
        add("閉じる" to "close")
        add("開く" to "open")
        add("可能" to "possible")
        add("不可" to "impossible")
        add("不得" to "cannot")
        add("禁止" to "forbidden")
        add("許可" to "allowed")
        add("制限" to "limit")
        add("上限" to "cap")
        add("下限" to "floor")
        add("限度" to "limit")
        add("超過" to "over")
        add("不足" to "short")
        add("足りない" to "not enough")
        add("必要" to "needs")
        add("要求" to "requires")
        add("条件" to "condition")
        add("前提" to "precondition")
        add("鍵" to "keys")
        add("解錠" to "unlocked")
        add("封印" to "sealed")
        add("解す" to "unseals")
        add("開錠" to "unlocks")
        add("繫が" to "links")
        add("リンク" to "link")
        add("接続" to "connection")
        add("通信" to "signal")
        add("信号" to "signal")
        add("応答" to "response")
        add("返事" to "reply")
        add("返す" to "returns")
        add("返り" to "return")
        add("来る" to "comes")
        add("寄る" to "calls at")
        add("行く" to "goes")
        add("届か" to "does not reach")
        add("届く" to "reaches")
        add("及ぶ" to "reaches")
        add("渡る" to "crosses")
        add("越え" to "crosses")
        add("入る" to "enters")
        add("出る" to "leaves")
        add("出す" to "releases")
        add("入れる" to "puts in")
        add("乗る" to "boards")
        add("離脱" to "departs")
        add("出撃" to "sortie")
        add("帰投" to "return")
        add("戻る" to "returns")
        add("再訪" to "revisit")
        add("訪問" to "visit")
        add("訪ね" to "visits")
        add("迎える" to "welcomes")
        add("迎撃" to "intercepts")
        add("撃退" to "drives off")
        add("押す" to "pushes")
        add("弾く" to "deflects")
        add("受け" to "receives")
        add("拾う" to "picks up")
        add("集める" to "gathers")
        add("運ぶ" to "carries")
        add("担ぐ" to "carries")
        add("持つ" to "holds")
        add("運搬" to "hauling")
        add("容量" to "capacity")
        add("余り" to "spare")
        add("余白" to "spare")
        add("余裕" to "spare")
        add("資産" to "assets")
        // v2.192 systems-only English pass: the tokens the composed lines need
    add("フルスロットル：全開ボタン / O（OCスラスター装備時）" to "Full throttle: full-open button / O (with the OC thruster)")
    add("《周回の恵み》還りのレールと周回者の徽章を携えて出撃" to "New Game Plus gear: Returner Rail and Returner Badge")
    add("旧式戦闘シミュレーション起動 — 成果は持ち出せない" to "Legacy sim started - results do not carry out")
    add("縁の印：色つきひし形＝ランドマーク（輪つき＝門）" to "Edge marks: colored diamond = landmark (ringed = gate)")
    add("エイム＆射撃：右スティック / 矢印 + K" to "Aim and shoot: right stick / arrows + K")
    add("行商船が襲われている — ならず者を退けろ" to "Trader under attack - drive off the raiders")
    add("《周回の恵み》還りのレールを携えて出撃" to "New Game Plus gear: Returner Rail")
    add("この宇宙のすべての星があなたを忘れます" to "Every planet in this universe will forget you")
    add("行商船を守り抜いた — 店の品を値引き" to "Trader defended - shop stock discounted")
    add("武器切替・壁設置：ボタン / 数字" to "Weapon switch and wall build: button / numbers")
    add("ランを保存 — やられると消える" to "Save the run - a clear-out wipes it")
    add("インベントリ：持物ボタン / I" to "Inventory: gear button / I")
    add("左：ハート＝船体　いなずま＝推進" to "Left: heart = hull  bolt = thrust")
    add("最上位捕食者の死　生態攪乱上昇" to "Apex dead  Ecological disruption up")
    add("ドラッグで移動 — 完了で保存" to "Drag to move - Done saves")
    add("巣の守り手の死　生態攪乱上昇" to "Nest mother dead  Ecological disruption up")
    add("まだどの星にも知られていない" to "No planet knows you yet")
    add("市は惑星の地表でのみ開かれる" to "The market opens only on a planet surface")
    add("封じられた遺構に踏み入った" to "Entered the sealed ruins")
    add("この星はあなたと取引しない" to "This planet will not trade with you")
    add("通った場所だけが描かれる" to "Only the places you walked are drawn")
    add("幼体殺し　生態攪乱上昇" to "Hatchlings slain  Ecological disruption up")
    add("遺物持ち出し　敵対上昇" to "Relic carried off  Hostility up")
    add("タップで使う / 合成" to "tap to use / craft")
    add("指導者撃破　素材回収" to "Leader down  Materials recovered")
    add("捕食者撃退　好意上昇" to "Predator repelled  Goodwill up")
    add("訓練シミュレーション" to "Training sim")
    add("暴君の縄張りに入った" to "Tyrant territory entered")
    add("訓練環境を終了した" to "Training sim ended")
    add("子に危害　敵対上昇" to "Child harmed  Hostility up")
    add("巣の守り手を倒した" to "Nest mother slain")
    add("遺物は取っていない" to "Relic not taken")
    add("子殺し　敵対上昇" to "Child slain  Hostility up")
    add("敵対度　臨界越え" to "Hostility crossed the threshold")
    add("好意度　臨界越え" to "Goodwill crossed the threshold")
    add("首位捕食者を殺害" to "Apex predator slain")
    add("巣の子を失わせた" to "Nest young lost")
    add("脱出パッドへ戻れ" to "Return to the escape pad")
    add("最上位捕食者の死" to "Apex dead")
    add("本当に消しますか" to "Really wipe?")
    add("聖なる子を殺害" to "Sacred child slain")
    add("聖なる子を傷害" to "Sacred child wounded")
    add("記憶コア　到達" to "Memory core reached")
    add("素材を回収せよ" to "Gather the materials")
    add("捕食者を退けた" to "Predator repelled")
    add("巣の守り手の死" to "Nest mother dead")
    add("タップ / R" to "Tap / R")
    add("HUDの見かた" to "How to read the HUD")
    add("星の主を倒した" to "Planet apex defeated")
    add("星間同期復旧 " to "intersystem sync restored ")
    add("捕食者を殺害" to "Predators slain")
    add("捕食者を排除" to "Predators cleared")
    add("子を傷つけた" to "Child wounded")
    add("この星の記憶" to "This planet memory")
    add("生態攪乱上昇" to "Disruption up")
    add("遺物持ち出し" to "Relic carried off")
    add("タップで交換" to "tap to swap")
    add("今回の撃破 " to "kills this run ")
    add("間同期復旧 " to "sync restored ")
    add("幼体を殺害" to "Hatchling slain")
    add("神獣を殺害" to "Sacred beast slain")
    add("指導者撃破" to "Leader down")
    add("捕食者撃退" to "Predator repelled")
    add("子を殺した" to "Child killed")
    add("生態攪乱 " to "Disruption ")
    add("幼体を傷害" to "Hatchling wounded")
    add("航行時間 " to "Voyage time ")
    add("ボタン配置" to "Button layout")
    add("セーブした" to "Saved")
    add("主を倒した" to "Masters defeated")
    add("子らを守れ" to "Protect the children")
    add("行商船襲撃" to "Trader raid")
    add("幼体殺し" to "Hatchlings slain")
    add("子に危害" to "Child harmed")
    add("通常ラン" to "Normal run")
    add("危険度　" to "Danger ")
    add("巣を破壊" to "Nest destroyed")
    add("持物は空" to "Inventory empty")
    add("主を倒せ" to "Defeat the apexes")
    add("汚染深度" to "desync depth")
    add("宙域安定" to "sector stability")
    add("ゲート鍵" to "gate keys")
    add("子殺し" to "Child slain")
    add("未訪問" to "Unvisited")
    add("再訪　" to "Revisit ")
    add("敵対 " to "Hostile ")
    add("好意 " to "Goodwill ")
    add("静かな" to "Quiet")
    add("合成可" to "craftable")
    add("気質　" to "temperament ")
    add("追跡者" to "stalker")
    add("静寂" to "Silence")
    add("発進" to "Launch")
    add("着陸" to "Land")
    add("聖　" to "sacred ")
    add("巨大" to "huge")
    add("星" to "Star ")
        // v2.162 第4弾/v2.185/v2.189: the four choices — the composed 「選択 …」 line
        // needs them here, before any shorter token can bite into them
        add("同期を完了して、眠りにつく" to "Complete the sync and sleep")
        add("切断して、漂流を続ける" to "Disconnect and keep drifting")
        add("網を解いて、朝を返す" to "Unbind the net, and give back the morning")
        add("刻まれた名を受け入れる" to "Accept the name that is written")
        add("到達波 " to "Waves reached: ")
        add("クリア回数 " to "Runs cleared: ")
        add("外部同期 " to "External sync ")
        add("選択 " to "Chosen: ")
        // HUD / records tokens
        add("ゲート鍵 " to "Gate keys ")
        // v2.170 文字の消灯: the pared-down compasses. Arrow-anchored so the tokens never bite
        // into star names mid-translation (「氷惑星を制圧した」→「氷惑星 is subdued」would offer
        // a bare「惑星 」to a greedy token; the arrow keeps these to the compass lines alone).
        for (a in listOf("→", "←", "↑", "↓")) {
            add("惑星 $a " to "planet $a ")
            add("門 $a " to "gate $a ")
        }
        add("鍵 " to "keys ")
        add("エリア " to "area ")
        add("ウェーブ(旧式) " to "Wave (legacy) ")
        add("残プロセス " to "processes ")
        add("宙域安定 " to "stability ")
        add("同期汚染 " to "Desync ")
        add("ウェーブ " to "Wave ")
        add("撃破 " to "kills ")
        add("時間 " to "Time ")
        add("資材 " to "mats ")
        add("星屑 " to "dust ")
        add("過熱!" to "Overheat!")
        add("同期完了 " to "Syncs completed ")
        add("出撃 " to "sorties ")
        // スロット / 空き
        add("スロット" to "Slot ")
        add("空き" to "empty")
        add("静寂 — 網が息をつく" to "Silence - the net draws breath") // v2.181
        add("拾い手のSMG" to "Gatherer SMG")
        add("還りのレール" to "Returner's Rail") // v2.186 NG+質的アンロック
        add("周回者の徽章" to "Returner's Badge") // v2.190 NG+第2の解禁
        add("写真モード" to "Photo Mode") // v2.187 写真モード
        // 行商船
        add("行商船 — 買い取り " to "Trader - buyback ")
        add("行商船" to "Trader")
        add("売れる持物がない" to "Nothing to sell")
        add("─ 売約済 ─" to "- sold -")
        add("行をタップで売却" to "tap a row to sell")
        add("行をタップで購入" to "tap a row to buy")
        add(" を売った（+" to " sold (+")
        add(" を購入した（-" to " bought (-")
        add(" を棚から戻した（-" to " returned (-")
        add("屑が足りない — 戻せない" to "Not enough dust to take it back")
        add("屑）" to " dust)")
        add("屑】" to " dust]")
        add("【+" to " [+")
        add("【" to " [")
        add("図録 " to "Codex ") // v2.182 図録: the codex header's tally
        // v2.162 英語化第4弾(後半): the memory cores' whole vocabulary — every pool line,
        // behind its prefix. Full-sentence tokens, so nothing bites into another line.
        add("記憶核: " to "Memory core: ")
        // v2.163 英語化第4弾(後半・その2): the inventory rows now ride tr() — markers,
        // slot labels, and every readable's one-line description.
        add("【合成可】" to "[Merge OK] ")
        add("【使】" to "[Use] ")
        add("（なし）" to "(none)")
        add("操作: " to "Controls: ")
        add("銃=ボタン / 近接=右スティック" to "gun = button / melee = right stick")
        add("近接=ボタン / 銃=右スティック" to "melee = button / gun = right stick")
        add("　(タップで入替)" to "  (tap to swap)")
        add("『" to "'")
        add("』" to "'")
        // v2.162: the event banner now funnels through tr() — the wave calls ride along
        add("⚠ 強大な気配が近づく" to "⚠ Something vast draws near")
        add("強敵の気配" to "A strong foe stirs")
        add("大群が接近している" to "A horde is closing in")
        add("磁気嵐 — 敵は荒ぶり、星屑は多くこぼれる" to "Magnetic storm - foes rage, and dust spills freely")
        add("賞金首『" to "Bounty head '")
        add("』が現れた" to " has appeared")
        add("清掃プロトコル起動 — 保守機構が展開する" to "Purge protocol engaged - the custodial machinery deploys")
        add("流星群 — 岩塊が降りしきる。落下点から離れよ" to "Meteor shower - rock falls thick. Stay clear of the impact points")
        // v2.159 英語化第4弾(前半): the surface objective line (biome name + situation),
        // the goal chips, and the quest chip/lines. Generic （）：！ fallbacks sort shortest
        // and run last, so composed lines close cleanly in ASCII.
        add("自然惑星" to "Nature Planet")
        add("火山惑星" to "Volcanic Planet")
        add("氷惑星" to "Ice Planet")
        add("ガス惑星" to "Gas Planet")
        add("死の惑星" to "Dead Planet")
        add("孤独な小惑星" to "Lonely Asteroid")
        add("この星の主を倒せ" to "Defeat this planet's apex")
        add("（残り " to " (left ")
        add("残り " to "left ")
        add("神獣に触れてしまった" to "The sacred beast was touched")
        add("神獣に触れるな" to "Do not touch the sacred beast")
        add("星の主を倒せ" to "Defeat the planet's apex")
        add("遺物を手にした" to "Relic in hand")
        add("遺物" to "Relic")
        add("（" to " (")
        add("）" to ")")
        add("：" to ": ")
        add("！" to "!")
        // v2.142 英語化第3弾: species names — 162 kinds, auto-derived from their stable English ids
        // ("tyrant_shark" → "Tyrant Shark"). The event feed picks these up.
        io.github.panda17tk.arpg.config.GameConfig().enemies.forEach { (id, def) ->
            if (def.name.any { it.code >= 0x2E80 }) add(def.name to enName(id))
        }
        // v2.142: item names (thruster/armor/consumable/lore …) — id-derived with the category moved
        // to the tail ("armor_cloth" → "Cloth Armor"); gun items ride the weapon tokens as well.
        io.github.panda17tk.arpg.item.ItemCatalog.ALL.forEach { item ->
            if (item.name.any { it.code >= 0x2E80 }) add(item.name to enName(item.id))
        }
        // weapon names (from the live catalog — ids are stable)
        val weaponEn = mapOf(
            "pistol" to "Pistol", "shotgun" to "Shotgun", "mg" to "Machine Gun",
            "beam" to "Beam", "grenade" to "Grenade", "smg" to "SMG",
            "rifle" to "Rifle", "railgun" to "Railgun", "blade" to "Return Blade",
        )
        io.github.panda17tk.arpg.combat.Weapons.ALL.forEach { w -> weaponEn[w.id]?.let { add(w.name to it) } }
        // upgrade card names
        val cardEn = mapOf(
            "gun_dmg" to "Firepower", "fire_rate" to "Rapid Fire", "melee" to "Melee Art",
            "max_hp" to "Sturdy Frame", "speed" to "Swift Step", "ammo" to "Ammo Cache",
            "lifesteal" to "Lifesteal", "engineer" to "Fieldworks", "reload_fast" to "Deft Reload",
            "stamina_up" to "Thruster Extension", "blast_up" to "Wider Blast", "regen_up" to "Self-Repair",
            "dash_eff" to "Light Dash", "bullet_speed" to "Muzzle Velocity", "armor_up" to "Rolled Armor",
            "magnet_up" to "Gathering Hand",
        )
        io.github.panda17tk.arpg.upgrade.Upgrades.ALL.forEach { u -> cardEn[u.id]?.let { add(u.name to it) } }
        add("へ" to " to ")
        add("と" to " and ")
        add("から" to " from ")
        add("より" to " from ")
        add("・" to ", ")
        add("、" to ", ")
        add("。" to ". ")
        add("倍率" to " multiplier")
        add("倍" to "x")
        add("秒" to "s")
        add("ダメージ" to "damage ")
        add("近接ダメージ" to "Melee damage")
        add("射撃ダメージ" to "gun dmg ")
        add("単分子" to "monomolecular")
        add("単" to "single ")
        add("銃" to "gun")
        add("弾薬" to "ammo")
        add("弾幕" to "bullet curtain")
        add("弾" to "bullet")
        add("全弾薬" to "all ammo")
        add("値" to " value")
        add("最大" to "max ")
        add("現在値" to "current value")
        add("既定値" to "default")
        add("免疫" to "immunity")
        add("回収" to "gather")
        add("修復" to "repair")
        add("反動制御" to "recoil control")
        add("集束" to "focus")
        add("精密" to "precision")
        add("追加" to "extra")
        add("代謝促進" to "metabolism boost")
        add("破砕" to "crushing")
        add("吸収" to "siphon")
        add("収穫" to "harvest")
        add("衝角" to "ram")
        add("磁石" to "magnet")
        add("遮熱" to "heat shield")
        add("対衝撃" to "impact guard")
        add("機構" to "mechanism")
        add("工具" to "tools")
        add("鉱区" to "mining zone")
        add("長柄" to "long-haft")
        add("作業用" to "work")
        add("転用" to "repurposed")
        add("腱" to "tendon")
        add("駆動" to "driven")
        add("索" to "cord")
        add("賞金首" to "bounty")
        add("討ち取った" to "slain")
        add("煙幕" to "smoke screen")
        add("祭壇" to "altar")
        add("筆跡" to "hand")
        add("頁" to "page")
        add("円" to "circle")
        add("装甲" to "armor")
        add("半径" to "radius")
        add("誘導" to "seeking")
        add("追" to "chase")
        add("獣" to "beast")
        add("蛾" to "moth")
        add("修道士" to "monk")
        add("マークスマン" to "marksman")
        add("待機" to "wait")
        add("徘徊" to "wander")
        add("低速" to "slow")
        add("高速" to "fast")
        add("大型" to "large")
        add("小型" to "small")
        add("中型" to "medium")
        add("軽量" to "lightweight")
        add("軽量化" to "weight cut")
        add("応急" to "field")
        add("携行" to "carry")
        add("高密度" to "dense")
        add("長" to "long")
        add("長時間" to "long")
        add("面制圧" to "area control")
        add("置き撃ち" to "set-fire")
        add("貫き" to "piercing")
        add("一直線" to "straight line")
        add("構え" to "aiming")
        add("構えている" to "while aiming")
        add("集弾" to "focus")
        add("高精度" to "high precision")
        add("高威力" to "high damage")
        add("単発" to "single shot")
        add("投擲" to "throw")
        add("ノックバック" to "knockback")
        add("半減" to "halved")
        add("壁を" to "walls")
        add("壁材" to "wall material")
        add("視える" to "visible")
        add("寄ってくる" to "come to you")
        add("遠く" to "from afar")
        add("雪" to "snow")
        add("滑" to "slide")
        add("民" to "folk")
        add("炉" to "furnace")
        add("熱源" to "heat source")
        add("飛沫" to "spray")
        add("星喰い" to "star eater")
        add("壁画" to "mural")
        add("拓本" to "rubbing")
        add("詩" to "poem")
        add("講義録" to "lecture")
        add("説明負債" to "explanatory debt")
        add("障害報告書" to "incident report")
        add("保全" to "preservation")
        add("写し" to "copy")
        add("運用規程" to "operations rules")
        add("抄" to "excerpt")
        add("断片" to "shard")
        add("仕様" to "spec")
        add("正体" to "true nature")
        add("研究" to "research")
        add("航海日誌" to "logbook")
        add("手記" to "journal")
        add("手紙" to "letter")
        add("子守唄" to "lullaby")
        add("偶像" to "idol")
        add("羅針盤" to "compass")
        add("走り書き" to "scrawl")
        add("整備手帳" to "service log")
        add("聖句" to "hymn")
        add("僧院" to "monastery")
        add("碑文" to "inscription")
        add("銘" to "engraving")
        add("承認者" to "approver")
        add("委員会" to "committee")
        add("議事録" to "minutes")
        add("保守" to "maintenance")
        add("外殻" to "outer shell")
        add("手際" to "flourish")
        add("築城" to "fortbuilding")
        add("縮地" to "shrink step")
        add("呼吸" to "breath")
        add("推进" to "propulsion")
        add("推進" to "thrust")
        add("アリーナ" to "arena")
        add("ピラー" to "pillar")
        add("クロス" to "cross")
        add("回廊" to "corridor")
        add("要塞" to "fortress")
        add("深層" to "deep layer")
        add("周期" to "cycle")
        add("入替" to "swap")
        add("調整モード" to "tuning mode")
        add("パラメータ" to "parameters")
        add("書き出し" to "export")
        add("書き出した" to "exported")
        add("できない" to "cannot ")
        add("エラー" to "error")
        add("前回の" to "last ")
        add("今回の" to "this ")
        add("休息" to "rest")
        add("設計" to "design")
        add("どおり" to "as designed")
        add("宙域" to "sector")
        add("収束" to "converge")
        add("過程" to "process")
        add("プロセス" to "process")
        add("与" to "deal")
        add("受" to "take")
        add("踏む" to "step on")
        add("死亡" to "death")
        add("検知" to "detected")
        add("検索" to "search")
        add("離陸" to "takeoff")
        add("船" to "ship")
        add("艦" to "vessel")
        add("本体" to "body")
        add("スラスター" to "thruster")
        add("バーニア" to "vernier")
        add("大推力" to "high thrust")
        add("高出力" to "high output")
        add("メディ" to "med")
        add("キット" to "kit")
        add("ゲル" to "gel")
        add("スティム" to "stim")
        add("ケース" to "case")
        add("クレート" to "crate")
        add("缶" to "can")
        add("ドリンク" to "drink")
        add("粥" to "porridge")
        add("保存食" to "ration")
        add("刺激剤" to "stimulant")
        add("スプレー" to "spray")
        add("ブレード" to "blade")
        add("ハンマー" to "hammer")
        add("メイス" to "mace")
        add("ハルバード" to "halberd")
        add("ランス" to "lance")
        add("ナイフ" to "knife")
        add("鎌" to "sickle")
        add("エッジ" to "edge")
        add("スコープ" to "scope")
        add("プレート" to "plate")
        add("フィン" to "fin")
        add("ブーツ" to "boots")
        add("バンド" to "band")
        add("ペンダント" to "pendant")
        add("レンズ" to "lens")
        add("フォイル" to "foil")
        add("グリップ" to "grip")
        add("ソール" to "sole")
        add("スタビライザー" to "stabilizer")
        add("ジャイロ" to "gyro")
        add("コア" to "core")
        add("加熱" to "heated")
        add("胸当て" to "breastplate")
        add("スーツ" to "suit")
        add("フレーム" to "frame")
        add("軽合金" to "light alloy")
        add("巫" to "priestess")
        add("屑" to "dust")
        add("刃" to "blade")
        add("王　" to "king ")
        add("星系 " to "system ")
        add("カラス" to "crow")
        add("子" to "child")
        add("子ら" to "children")
        add("の" to " ")
        add("を" to " ")
        add("が" to " ")
        add("は" to " ")
        add("も" to " ")
        add("に" to " ")
        add("で" to " ")
        add("「" to " ")
        add("」" to " ")
        add("研ぎ澄まされた" to "sharpened")
        add("体が軽い" to "Reflexes sharpened")
        add("回復し続ける" to "keeps regenerating")
        add("スタミナ剤" to "stamina agent")
        add("攻撃範囲" to "attack range")
        add("壁HP" to "wall HP")
        add("味方" to "allies")
        add("自爆" to "self-blast")
        add("即時" to "instant")
        add("爆破" to "blast")
        add("着弾" to "impact")
        add("砲台" to "turret")
        add("全回復" to "fully restored")
        add("補給" to "supply")
        add("解体" to "scrap")
        add("生存者" to "survivors")
        add("玉座" to "throne")
        add("とゲート鍵" to " and gate keys")
        add("を収束 — " to " converged - ")
        add("保守パッチ" to "maintenance patch")
        add("星系に到達した" to " system reached")
        add("12秒間 " to "12s ")
        add("星屑+1山" to "dust +1 pile")
        add("／" to "/")
        add(" 威力" to " damage")
        add(" 連射間隔" to " fire interval")
        add(" 拡散" to " spread")
        add(" 装弾数" to " ammo capacity")
        add(" 発射数" to " shots")
        add(" リロード" to " reload")
        add("全武器" to "all weapons")
        add("全敵 " to "All enemies: ")
        add("合成: " to "Craft: ")
        add("休息回復" to "rest regen")
        add("反応が研ぎ澄まされた" to "Reflexes sharpened")
        add("スタミナ消費なし" to "no stamina drain")
        add("し続ける" to "keeps ")
        add("発射間隔" to "fire interval")
        add("遺品" to "relic")
        add("が漂っている" to " drifts nearby")
        add("保守パッチを選択" to "choose a maintenance patch")
        add("召喚" to "summon")
        add("項目" to "item")
        add("調整" to "tuning")
        add("選択" to "choose")
        add("殺害" to "killed")
        add("遠雷" to "distant thunder")
        add("宵" to "evening")
        add("緋" to "crimson")
        add("白磁" to "porcelain")
        add("枯野" to "withered field")
        add("錆" to "rust")
        add("遠" to "far")
        add("重い星系" to "heavy system")
        add("多生の星系" to "swarming system")
        add("軽い星系" to "light system")
        add("多生" to "many lives")
        add("重い" to "heavy")
        add("軽い" to "light")
        add("ヴェガ" to "Vega")
        add("ノクト" to "Noct")
        add("ロッカ" to "Rokka")
        add("ハクア" to "Hakua")
        add("スズ" to "Suzu")
        add("ガラン" to "Garan")
        add("ユキ" to "Yuki")
        add("所持 " to "Held ")
        add("所持 星屑 " to "Held dust ")
        add("　　" to "  ")
        add("　" to " ")
        add("撃破ごとに HP +" to "per kill, HP +")
        add("柺擂 " to "baseline ")
        add("変更 " to "changed ")
        add(" 項目" to " items")
        add("誰庠パラーメータ" to "tuning knobs")
        add("最大HP" to "Max HP")
        add("最大スタミナ" to "Max stamina")
        add("移動速度" to "Move speed")
        add("移動速度倍率" to "Move speed multiplier")
        add("ダッシュ倍率" to "Dash multiplier")
        add("ダッシュ推力" to "Dash thrust")
        add("スタミナ消費" to "Stamina drain")
        add("スタミナ回復" to "Stamina regen")
        add("近接リーチ" to "Melee reach")
        add("近接間隔" to "Melee interval")
        add("近接斬撃ダメージ" to "Slash damage")
        add("近接弱化しきい値" to "Melee weaken threshold")
        add("剣最低スタミナ" to "Min stamina for a blade")
        add("近接弱化倍率" to "Melee weaken multiplier")
        add("素手ダメージ" to "Fist damage")
        add("近接スタミナ消費" to "Melee stamina drain")
        add("弾寿命" to "Bullet lifetime")
        add("グレネード弾速" to "Grenade speed")
        add("グレネード信管" to "Grenade fuse")
        add("自動装填遅延" to "Auto-reload delay")
        add("爆発半径" to "Blast radius")
        add("爆発ダメージ" to "Blast damage")
        add("爆発自傷" to "Self-blast damage")
        add("爆発壁ダメージ" to "Wall blast damage")
        add("全敵 HP倍率" to "All enemies: HP multiplier")
        add("全敵 速度倍率" to "All enemies: speed multiplier")
        add("全敵 攻撃力倍率" to "All enemies: attack multiplier")
        add("全敵 攻撃間隔倍率" to "All enemies: attack interval multiplier")
        add("初回ウェーブ遅延" to "First wave delay")
        add("幕間" to "Intermission")
        add("基本物量" to "Base enemy count")
        add("物量/波" to "Enemies added per wave")
        add("物量上限" to "Enemy count cap")
        add("敵HP成長/波" to "Enemy HP growth/wave")
        add("敵速度成長/波" to "Enemy speed growth/wave")
        add("同時生存 基本" to "Enemies alive at once (base)")
        add("同時生存/波" to "Enemies alive at once per wave")
        add("同時生存上限" to "Enemies alive at once cap")
        add("湧き間隔 基本" to "Spawn interval (base)")
        add("湧き間隔短縮/波" to "Spawn interval shrink/wave")
        add("湧き間隔下限" to "Spawn interval floor")
        add("中ボス周期" to "Mid-boss cycle")
        add("ボス周期" to "Boss cycle")
        add("分離半径" to "Separation radius")
        add("低HP鈍化倍率" to "Low-HP slowdown multiplier")
        add("徘徊速度倍率" to "Roam speed multiplier")
        add("徘徊詰まり" to "Roam congestion")
        add("経路再計算間隔" to "Path recompute interval")
        add("接触ノックバック" to "Contact knockback")
        add("接触無敵時間" to "Contact invulnerability")
        add("群れ結集" to "Flock cohesion")
        add("群れ整列" to "Flock alignment")
        add("群れ分離" to "Flock separation")
        add("群れ逃走" to "Flock flee")
        add("群れ徘徊" to "Flock roam")
        add("噛みつき溜め" to "Bite wind-up")
        add("ピストル 威力" to "Pistol damage")
        add("ピストル 連射間隔" to "Pistol fire interval")
        add("ピストル 拡散" to "Pistol spread")
        add("ピストル 装弾数" to "Pistol ammo capacity")
        add("ピストル 発射数" to "Pistol shots")
        add("ピストル リロード" to "Pistol reload")
        add("ショットガン 威力" to "Shotgun damage")
        add("ショットガン 連射間隔" to "Shotgun fire interval")
        add("ショットガン 拡散" to "Shotgun spread")
        add("ショットガン 装弾数" to "Shotgun ammo capacity")
        add("ショットガン 発射数" to "Shotgun shots")
        add("ショットガン リロード" to "Shotgun reload")
        add("マシンガン 威力" to "Machine gun damage")
        add("マシンガン 連射間隔" to "Machine gun fire interval")
        add("マシンガン 拡散" to "Machine gun spread")
        add("マシンガン 装弾数" to "Machine gun ammo capacity")
        add("マシンガン 発射数" to "Machine gun shots")
        add("マシンガン リロード" to "Machine gun reload")
        add("ビーム 威力" to "Beam damage")
        add("ビーム 連射間隔" to "Beam fire interval")
        add("ビーム 拡散" to "Beam spread")
        add("ビーム 発射数" to "Beam shots")
        add("グレネード 威力" to "Grenade damage")
        add("グレネード 連射間隔" to "Grenade fire interval")
        add("グレネード 拡散" to "Grenade spread")
        add("グレネード 装弾数" to "Grenade ammo capacity")
        add("グレネード 発射数" to "Grenade shots")
        add("グレネード リロード" to "Grenade reload")
        add("サブマシンガン 威力" to "SMG damage")
        add("サブマシンガン 連射間隔" to "SMG fire interval")
        add("サブマシンガン 拡散" to "SMG spread")
        add("サブマシンガン 装弾数" to "SMG ammo capacity")
        add("サブマシンガン 発射数" to "SMG shots")
        add("サブマシンガン リロード" to "SMG reload")
        add("ライフル 威力" to "Rifle damage")
        add("ライフル 連射間隔" to "Rifle fire interval")
        add("ライフル 拡散" to "Rifle spread")
        add("ライフル 装弾数" to "Rifle ammo capacity")
        add("ライフル 発射数" to "Rifle shots")
        add("ライフル リロード" to "Rifle reload")
        add("レールガン 威力" to "Railgun damage")
        add("レールガン 連射間隔" to "Railgun fire interval")
        add("レールガン 拡散" to "Railgun spread")
        add("レールガン 装弾数" to "Railgun ammo capacity")
        add("レールガン 発射数" to "Railgun shots")
        add("レールガン リロード" to "Railgun reload")
        add("帰還刃 威力" to "Returning blade damage")
        add("帰還刃 連射間隔" to "Returning blade fire interval")
        add("帰還刃 拡散" to "Returning blade spread")
        add("帰還刃 発射数" to "Returning blade shots")
        add("星系 #" to "System #")
        add("に到達した" to " reached")
    }.sortedByDescending { it.first.length }
}


