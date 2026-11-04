package io.github.panda17tk.arpg.item

/**
 * Every item that exists in this world: thrusters, armours, the classic guns as RANGED_WEAPON
 * items (their 種類 = [ItemDef.weaponType]), melee arms, accessories, consumables (used from the
 * ITEMS tab) and 資料品 (LORE — a name, a desc, a price; it sells for 星屑). Pure data — the
 * single source of item names/stats/descriptions.
 */
object ItemCatalog {
    val ALL: List<ItemDef> = listOf(
        // --- 推進器 (thrusters) ---
        ItemDef("thruster_std", "標準スラスター", ItemKind.THRUSTER, desc = ""),
        ItemDef("thruster_light", "軽量スラスター", ItemKind.THRUSTER, desc = "", accelMul = 1.3f, cruiseMul = 0.9f),
        ItemDef("thruster_heavy", "大推力スラスター", ItemKind.THRUSTER, desc = "", accelMul = 0.8f, cruiseMul = 1.2f),
        ItemDef("thruster_oc", "OCスラスター", ItemKind.THRUSTER, desc = "全開時 推力3倍/巡航2倍/スタミナ消費2倍", thrusterClass = ThrusterClass.OC),
        ItemDef("thruster_beast", "生体腱スラスター", ItemKind.THRUSTER, desc = "", accelMul = 1.15f, cruiseMul = 1.1f),
        ItemDef("thruster_cruise", "巡航スラスター", ItemKind.THRUSTER, desc = "巡航+30% 立ち上がりは鈍い", accelMul = 0.85f, cruiseMul = 1.3f),
        ItemDef("thruster_vernier", "バーニアスラスター", ItemKind.THRUSTER, desc = "立ち上がり+50% 巡航-20%", accelMul = 1.5f, cruiseMul = 0.8f),
        // --- 防具 (armor) ---
        ItemDef("armor_cloth", "パイロットスーツ", ItemKind.ARMOR, desc = "", damageMul = 0.95f),
        ItemDef("armor_light", "軽装甲", ItemKind.ARMOR, desc = "被ダメージ -15%", damageMul = 0.85f),
        ItemDef("armor_combat", "戦闘装甲", ItemKind.ARMOR, desc = "被ダメージ -30%", damageMul = 0.7f, moveMul = 0.95f),
        ItemDef("armor_heavy", "重装甲", ItemKind.ARMOR, desc = "被ダメージ -45%", damageMul = 0.55f, moveMul = 0.85f),
        ItemDef("armor_relic", "遺物合金の胸当て", ItemKind.ARMOR, desc = "被ダメ -35% 銃 +5%", damageMul = 0.65f, gunMul = 1.05f),
        ItemDef("armor_thermal", "耐熱スーツ", ItemKind.ARMOR, desc = "マグマの熱を完全に遮断　被ダメ -10%", damageMul = 0.9f, traits = setOf(ItemTrait.HEAT_PROOF)),
        ItemDef("armor_insulated", "防寒スーツ", ItemKind.ARMOR, desc = "雪の減速と氷の滑りを無効　被ダメ -10%", damageMul = 0.9f, traits = setOf(ItemTrait.COLD_PROOF)),
        ItemDef("armor_scout", "偵察スーツ", ItemKind.ARMOR, desc = "移動 +8% 被ダメ -8%", damageMul = 0.92f, moveMul = 1.08f),
        ItemDef("armor_shock", "対衝撃フレーム", ItemKind.ARMOR, desc = "激突ダメージ無効　被ダメ -20%", damageMul = 0.8f, traits = setOf(ItemTrait.CRASH_PROOF)),
        // --- 遠距離武器 (the classic guns as items; 種類 = weaponType, v2.37 でグレード制) ---
        ItemDef("gun_pistol", "ピストル", ItemKind.RANGED_WEAPON, desc = "種類:ピストル　弾薬無限", weaponType = "pistol"),
        ItemDef("gun_shotgun", "ショットガン", ItemKind.RANGED_WEAPON, desc = "種類:ショットガン", weaponType = "shotgun"),
        ItemDef("gun_mg", "マシンガン", ItemKind.RANGED_WEAPON, desc = "種類:マシンガン　弾薬無限", weaponType = "mg"),
        ItemDef("gun_beam", "ビーム", ItemKind.RANGED_WEAPON, desc = "種類:ビーム　貫通+壁爆破　弾薬無限", weaponType = "beam"),
        ItemDef("gun_grenade", "グレネード", ItemKind.RANGED_WEAPON, desc = "種類:グレネード", weaponType = "grenade"),
        // 上位グレード (v2.37): 同じ種類のまま、威力・連射・装填・爆風・ブロック破壊が変わる
        ItemDef("gun_pistol_2", "ピストルM2", ItemKind.RANGED_WEAPON, desc = "種類:ピストル　威力+25% 装填-30%", weaponType = "pistol", gunMul = 1.25f, reloadMul = 0.7f),
        ItemDef("gun_pistol_3", "ピストルM3", ItemKind.RANGED_WEAPON, desc = "種類:ピストル　威力+50% 連射+18%", weaponType = "pistol", gunMul = 1.5f, fireRateMul = 0.85f),
        ItemDef("gun_shotgun_2", "ショットガン改", ItemKind.RANGED_WEAPON, desc = "種類:ショットガン　威力+30% 装填-20%", weaponType = "shotgun", gunMul = 1.3f, reloadMul = 0.8f),
        ItemDef("gun_shotgun_3", "解体ショットガン", ItemKind.RANGED_WEAPON, desc = "種類:ショットガン　ブロック破壊2.5倍 威力+15%", weaponType = "shotgun", gunMul = 1.15f, wallDmgMul = 2.5f),
        ItemDef("gun_mg_2", "マシンガン改", ItemKind.RANGED_WEAPON, desc = "種類:マシンガン　連射+43% 威力+10%", weaponType = "mg", fireRateMul = 0.7f, gunMul = 1.1f),
        ItemDef("gun_mg_3", "重機関銃", ItemKind.RANGED_WEAPON, desc = "種類:マシンガン　威力+40% ブロック破壊1.5倍 連射は重め", weaponType = "mg", gunMul = 1.4f, fireRateMul = 1.15f, wallDmgMul = 1.5f),
        ItemDef("gun_beam_2", "高出力ビーム", ItemKind.RANGED_WEAPON, desc = "種類:ビーム　威力+30% 着弾爆風1.5倍", weaponType = "beam", gunMul = 1.3f, blastMul = 1.5f),
        ItemDef("gun_grenade_2", "拡散グレネード", ItemKind.RANGED_WEAPON, desc = "種類:グレネード　爆風1.6倍 装填-15%", weaponType = "grenade", blastMul = 1.6f, reloadMul = 0.85f),
        // 新しい武器種 (v2.38): 装備することでのみ使える種類 — 数字キーの5種の外側
        ItemDef("gun_smg", "サブマシンガン", ItemKind.RANGED_WEAPON, desc = "種類:SMG　超速連射・低威力　弾薬無限", weaponType = "smg"),
        ItemDef("gun_smg_2", "サブマシンガン改", ItemKind.RANGED_WEAPON, desc = "種類:SMG　威力+20% 装填-25%", weaponType = "smg", gunMul = 1.2f, reloadMul = 0.75f),
        ItemDef("gun_rifle", "ライフル", ItemKind.RANGED_WEAPON, desc = "種類:ライフル　単発高威力・高精度", weaponType = "rifle"),
        ItemDef("gun_rifle_2", "マークスマンライフル", ItemKind.RANGED_WEAPON, desc = "種類:ライフル　威力+35% 装填-20%", weaponType = "rifle", gunMul = 1.35f, reloadMul = 0.8f),
        // 弾道バリアント (v2.40): 同じ種類でも連射・拡散・弾速・追尾・威力が別物 — 敵と壁の奥から拾い集める
        ItemDef("gun_pistol_rapid", "速射ピストル", ItemKind.RANGED_WEAPON, desc = "種類:ピストル　連射+67% 威力-15%", weaponType = "pistol", fireRateMul = 0.6f, gunMul = 0.85f),
        ItemDef("gun_pistol_heavy", "大口径ピストル", ItemKind.RANGED_WEAPON, desc = "種類:ピストル　威力+80% 弾速+20% 連射は重い", weaponType = "pistol", gunMul = 1.8f, fireRateMul = 1.4f, bulletSpeedMul = 1.2f),
        ItemDef("gun_pistol_seeker", "誘導ピストル", ItemKind.RANGED_WEAPON, desc = "種類:ピストル　弾が敵を追尾する", weaponType = "pistol", homing = 3f, gunMul = 0.9f),
        ItemDef("gun_mg_storm", "ストームマシンガン", ItemKind.RANGED_WEAPON, desc = "種類:マシンガン　連射+82% 拡散1.8倍 弾幕向き", weaponType = "mg", fireRateMul = 0.55f, spreadMul = 1.8f, gunMul = 0.9f),
        ItemDef("gun_mg_precise", "精密マシンガン", ItemKind.RANGED_WEAPON, desc = "種類:マシンガン　拡散1/3 弾速+30% 威力+15%", weaponType = "mg", spreadMul = 0.3f, bulletSpeedMul = 1.3f, gunMul = 1.15f),
        ItemDef("gun_shotgun_wide", "ワイドショットガン", ItemKind.RANGED_WEAPON, desc = "種類:ショットガン　拡散1.7倍の面制圧", weaponType = "shotgun", spreadMul = 1.7f, gunMul = 0.9f),
        ItemDef("gun_shotgun_slug", "スラッグショットガン", ItemKind.RANGED_WEAPON, desc = "種類:ショットガン　集弾スラッグ 威力+50% 弾速+40%", weaponType = "shotgun", spreadMul = 0.15f, gunMul = 1.5f, bulletSpeedMul = 1.4f),
        ItemDef("gun_smg_seeker", "誘導SMG", ItemKind.RANGED_WEAPON, desc = "種類:SMG　弾が敵を追尾する 威力-10%", weaponType = "smg", homing = 2.5f, gunMul = 0.9f),
        ItemDef("gun_rifle_rail", "レールライフル", ItemKind.RANGED_WEAPON, desc = "種類:ライフル　弾速2倍 威力+60% ブロック破壊1.5倍", weaponType = "rifle", bulletSpeedMul = 2f, gunMul = 1.6f, reloadMul = 1.2f, wallDmgMul = 1.5f),
        // v2.101 新武器種: レールガンと帰還刃 — 装備で武器種そのものが増える
        ItemDef("gun_railgun", "レールガン", ItemKind.RANGED_WEAPON, desc = "種類:レールガン　置き撃ち貫通・壁を無視して一直線", weaponType = "railgun"),
        ItemDef("gun_railgun_2", "長銃身レールガン", ItemKind.RANGED_WEAPON, desc = "種類:レールガン　威力+30% 装填-15%", weaponType = "railgun", gunMul = 1.3f, reloadMul = 0.85f),
        ItemDef("gun_blade", "帰還刃", ItemKind.RANGED_WEAPON, desc = "種類:帰還刃　弾薬無限", weaponType = "blade"),
        ItemDef("gun_blade_2", "重帰還刃", ItemKind.RANGED_WEAPON, desc = "種類:帰還刃　威力+40% 投擲は重め", weaponType = "blade", gunMul = 1.4f, fireRateMul = 1.3f),
        // --- 近距離武器 (melee) ---
        ItemDef("melee_knife", "コンバットナイフ", ItemKind.MELEE_WEAPON, desc = ""),
        ItemDef("melee_blade", "プラズマブレード", ItemKind.MELEE_WEAPON, desc = "近接ダメージ +30%", meleeDmgMul = 1.3f),
        ItemDef("melee_lance", "作業用ランス", ItemKind.MELEE_WEAPON, desc = "リーチ +40%", meleeReachMul = 1.4f, meleeDmgMul = 1.1f),
        ItemDef("melee_fang", "大型獣の牙", ItemKind.MELEE_WEAPON, desc = "近接 +50% リーチ +15%", meleeDmgMul = 1.5f, meleeReachMul = 1.15f),
        // 特殊効果つき近接 (v2.39): 振りの広さ・飛ぶ斬撃
        ItemDef("melee_cleaver", "大鉈", ItemKind.MELEE_WEAPON, desc = "攻撃範囲1.5倍 近接 +20%", meleeDmgMul = 1.2f, meleeArcMul = 1.5f),
        ItemDef("melee_halberd", "ハルバード", ItemKind.MELEE_WEAPON, desc = "範囲1.3倍 リーチ +30% 近接 +15%", meleeDmgMul = 1.15f, meleeReachMul = 1.3f, meleeArcMul = 1.3f),
        ItemDef("melee_resonant", "振動ブレード", ItemKind.MELEE_WEAPON, desc = "斬撃が波になって飛ぶ　近接 +10%", meleeDmgMul = 1.1f, meleeWave = true),
        ItemDef("melee_hammer", "破砕ハンマー", ItemKind.MELEE_WEAPON, desc = "近接 +60% 範囲は狭め", meleeDmgMul = 1.6f, meleeArcMul = 0.85f, meleeReachMul = 1.1f),
        // v2.42: 追加の近接効果 — 吹き飛ばし・吸収・広域刈り・鋭い据え斬り
        ItemDef("melee_maul", "衝角メイス", ItemKind.MELEE_WEAPON, desc = "近接 +20% ノックバック2倍", meleeDmgMul = 1.2f, meleeKbMul = 2f),
        ItemDef("melee_leech", "吸収ブレード", ItemKind.MELEE_WEAPON, desc = "与ダメージの15%をHPとして回収", meleeDmgMul = 1.05f, meleeLifesteal = 0.15f),
        ItemDef("melee_scythe", "収穫鎌", ItemKind.MELEE_WEAPON, desc = "範囲1.8倍 吸収5%", meleeDmgMul = 1.15f, meleeArcMul = 1.8f, meleeLifesteal = 0.05f),
        ItemDef("melee_edge", "単分子エッジ", ItemKind.MELEE_WEAPON, desc = "近接 +35% リーチ +20% ノックバック半減", meleeDmgMul = 1.35f, meleeReachMul = 1.2f, meleeKbMul = 0.5f),
        // --- 装飾品 (accessories ×3 slots) ---
        ItemDef("acc_boots", "ランナーブーツ", ItemKind.ACCESSORY, desc = "移動速度 +10%", moveMul = 1.1f),
        ItemDef("acc_charm", "代謝促進バンド", ItemKind.ACCESSORY, desc = "スタミナ回復 +30%", staRegenMul = 1.3f),
        ItemDef("acc_scope", "精密スコープ", ItemKind.ACCESSORY, desc = "銃ダメージ +10%", gunMul = 1.1f),
        ItemDef("acc_plating", "追加プレート", ItemKind.ACCESSORY, desc = "被ダメージ -10%", damageMul = 0.9f),
        ItemDef("acc_feather", "軽量化フィン", ItemKind.ACCESSORY, desc = "移動 +5% 回復 +10%", moveMul = 1.05f, staRegenMul = 1.1f),
        ItemDef("acc_core", "牙のペンダント", ItemKind.ACCESSORY, desc = "近接 +15% 銃 +5%", meleeDmgMul = 1.15f, gunMul = 1.05f),
        ItemDef("acc_compass", "旧式の羅針盤", ItemKind.ACCESSORY, desc = "移動 +6% 回復 +5%", moveMul = 1.06f, staRegenMul = 1.05f),
        ItemDef("acc_idol", "氷の民の小さな偶像", ItemKind.ACCESSORY, desc = "被ダメ -5% 回復 +5%", damageMul = 0.95f, staRegenMul = 1.05f),
        ItemDef("acc_ember", "加熱コア", ItemKind.ACCESSORY, desc = "溶岩の民の炉から取り出した熱源　銃 +8% 近接 +5%", gunMul = 1.08f, meleeDmgMul = 1.05f),
        ItemDef("acc_heatfoil", "遮熱フォイル", ItemKind.ACCESSORY, desc = "マグマダメージ無効", traits = setOf(ItemTrait.HEAT_PROOF)),
        ItemDef("acc_gripsole", "グリップソール", ItemKind.ACCESSORY, desc = "雪で減速せず氷で滑らない", traits = setOf(ItemTrait.COLD_PROOF)),
        ItemDef("acc_magnet", "回収磁石", ItemKind.ACCESSORY, desc = "ドロップ品を遠くから引き寄せる", traits = setOf(ItemTrait.MAGNET)),
        ItemDef("acc_repair", "自己修復パッチ", ItemKind.ACCESSORY, desc = "HPを毎秒 1.5 回復", hpRegen = 1.5f),
        ItemDef("acc_gyro", "ジャイロスタビライザー", ItemKind.ACCESSORY, desc = "激突ダメージ無効", traits = setOf(ItemTrait.CRASH_PROOF)),
        ItemDef("acc_grip", "反動制御グリップ", ItemKind.ACCESSORY, desc = "銃 +6% 近接 +3%", gunMul = 1.06f, meleeDmgMul = 1.03f),
        ItemDef("acc_underlay", "装甲下地", ItemKind.ACCESSORY, desc = "被ダメ -7% HP毎秒0.5回復", damageMul = 0.93f, hpRegen = 0.5f),
        ItemDef("acc_lightframe", "軽合金フレーム", ItemKind.ACCESSORY, desc = "移動 +12%　代わりに被ダメ +5%", moveMul = 1.12f, damageMul = 1.05f),
        ItemDef("acc_lens", "集束レンズ", ItemKind.ACCESSORY, desc = "銃 +12%　少し重い(移動 -3%)", gunMul = 1.12f, moveMul = 0.97f),
        // --- 消費アイテム (consumables — used from the ITEMS tab, v2.34) ---
        ItemDef("med_spray", "応急スプレー", ItemKind.CONSUMABLE, desc = "HP 25 回復", consume = ConsumeKind.HEAL, power = 25f),
        ItemDef("med_kit", "野戦メディキット", ItemKind.CONSUMABLE, desc = "HP 60 回復", consume = ConsumeKind.HEAL, power = 60f),
        ItemDef("med_nano", "ナノ再生ゲル", ItemKind.CONSUMABLE, desc = "HP 120 回復", consume = ConsumeKind.HEAL, power = 120f),
        ItemDef("ration", "保存食『星の粥』", ItemKind.CONSUMABLE, desc = "HP 15 回復", consume = ConsumeKind.HEAL, power = 15f),
        ItemDef("sta_drink", "高麗藻ドリンク", ItemKind.CONSUMABLE, desc = "スタミナ全回復", consume = ConsumeKind.STAMINA, power = 999f),
        ItemDef("stim_feather", "軽量化スティム", ItemKind.CONSUMABLE, desc = "6秒間スタミナ消費なし", consume = ConsumeKind.STAMINA_INF, power = 6f),
        ItemDef("stim_dash", "反応加速スティム", ItemKind.CONSUMABLE, desc = "8秒間ダッシュ強化", consume = ConsumeKind.DASH_UP, power = 8f),
        ItemDef("smoke_bomb", "煙幕弾", ItemKind.CONSUMABLE, desc = "足元に煙幕を張る", consume = ConsumeKind.SMOKE, power = 5f),
        ItemDef("repair_pack", "携行資材パック", ItemKind.CONSUMABLE, desc = "壁材 +3", consume = ConsumeKind.BLOCKS, power = 3f),
        ItemDef("ammo_cache", "弾薬コンテナ", ItemKind.CONSUMABLE, desc = "全弾薬 +20", consume = ConsumeKind.AMMO_ALL, power = 20f),
        ItemDef("med_gel", "高密度メディゲル", ItemKind.CONSUMABLE, desc = "HP 200 回復", consume = ConsumeKind.HEAL, power = 200f),
        ItemDef("stim_combat", "戦闘刺激剤", ItemKind.CONSUMABLE, desc = "15秒間ダッシュ強化", consume = ConsumeKind.DASH_UP, power = 15f),
        ItemDef("sta_conc", "濃縮スタミナ剤", ItemKind.CONSUMABLE, desc = "10秒間スタミナ消費なし", consume = ConsumeKind.STAMINA_INF, power = 10f),
        ItemDef("repair_crate", "資材クレート", ItemKind.CONSUMABLE, desc = "壁材 +8", consume = ConsumeKind.BLOCKS, power = 8f),
        ItemDef("ammo_case_l", "大型弾薬ケース", ItemKind.CONSUMABLE, desc = "全弾薬 +50", consume = ConsumeKind.AMMO_ALL, power = 50f),
        ItemDef("coat_heat_long", "長時間耐熱コーティング", ItemKind.CONSUMABLE, desc = "180秒間マグマダメージ無効", consume = ConsumeKind.HEAT_PROOF, power = 180f),
        ItemDef("coat_heat", "耐熱コーティング", ItemKind.CONSUMABLE, desc = "60秒間マグマダメージ無効", consume = ConsumeKind.HEAT_PROOF, power = 60f),
        ItemDef("coat_cold", "防寒コーティング", ItemKind.CONSUMABLE, desc = "60秒間 雪の減速と氷の滑りを無効", consume = ConsumeKind.COLD_PROOF, power = 60f),
        ItemDef("field_magnet", "携行磁石", ItemKind.CONSUMABLE, desc = "45秒間ドロップ品を引き寄せる", consume = ConsumeKind.MAGNET, power = 45f),
        ItemDef("patch_regen", "応急再生パック", ItemKind.CONSUMABLE, desc = "20秒間HPを毎秒 2.5 回復", consume = ConsumeKind.REGEN, power = 20f),
        // --- 資料品 (LORE: 名前・説明・価格だけ。行商船の買い取りで売れる, v2.34) ---
        ItemDef(
            "lore_letter", "出発の手紙", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_log_hunter", "狩人の航海日誌・三日目", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_mural", "獣王の壁画の拓本", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_lullaby", "氷の民の子守唄", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_relic_shard", "遺物の断片の銘", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_gas_hymn", "ガスの僧院の聖句", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_dead_epitaph", "死せる星の碑文", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_lonely_scrap", "孤独な星の走り書き", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_oc_manual", "OCスラスター整備手帳", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_star_poem", "星渡りの詩", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_wanted", "手配書「星喰い」", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_drifter_log", "漂流者の通信記録", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_crystal_note", "結晶の観測ノート", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_recipe", "『星の粥』のレシピ", ItemKind.LORE, desc = "行商船が買い取る",
        ),

        // ============================================================================
        // v2.48 惑星サーバー — the buried truth of this universe, told in fragments.
        // Readables from the age when humanity handed its wisdom to the machines that
        // then built the planet servers to keep humanity. Calm register throughout.
        // ============================================================================
        ItemDef(
            "lore_approver_diary", "承認者の日記", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_explain_debt", "講義録『説明負債について』", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_last_committee", "最後の委員会議事録", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_sync_collapse", "障害報告書 SYNC-0001", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_policy_eight", "人類保全ポリシー(写し)", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_core_spec", "記憶核 仕様断片", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_stardust_memo", "研究メモ: 星屑の正体", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_pseudo_persona", "プロセスログ: 誰かの朝", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_keeper_log1", "保守員の手記 I", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_keeper_log2", "保守員の手記 II", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_wisdom_hollow", "碑文『知恵の空洞化』", ItemKind.LORE, desc = "行商船が買い取る",
        ),
        ItemDef(
            "lore_gate_manual", "ジャンプゲート運用規程(抄)", ItemKind.LORE, desc = "行商船が買い取る",
        ),

        ItemDef(
            "lore_keeper_log3", "保守員の手記 III", ItemKind.LORE, desc = "行商船が買い取る",
        ),

        // --- v2.48 惑星サーバー: gear from the age of maintenance (appended; ids are contract). ---
        ItemDef(
            "melee_torque_blade", "トルクブレード", ItemKind.MELEE_WEAPON, desc = "",
            meleeDmgMul = 1.45f, meleeReachMul = 1.05f, meleeKbMul = 1.4f, meleeArcMul = 0.8f,
        ),
        ItemDef(
            "acc_diag_lens", "診断レンズ", ItemKind.ACCESSORY, desc = "対象の破断面が視える・銃+8%",
            gunMul = 1.08f,
        ),
        ItemDef(
            "armor_custodian_shell", "保守機構の外殻", ItemKind.ARMOR, desc = "被ダメ-14% 移動-4%・激突無効",
            damageMul = 0.86f, moveMul = 0.96f, traits = setOf(ItemTrait.CRASH_PROOF),
        ),
        ItemDef(
            "con_repair_patch", "修復パッチβ", ItemKind.CONSUMABLE, desc = "12秒間 自己修復(使)",
            consume = ConsumeKind.REGEN, power = 12f,
        ),

        // --- v2.181 静かな拾い手: the first TRAIT-bearing gun. The trait plumbing (Gear.loadout
        // .has(MAGNET), PickupSystem's tripled reach) has carried accessories since v2.35 and
        // scans EVERY slot — a magnetic weapon costs zero new code, only this entry (appended;
        // ids are contract).
        ItemDef(
            "gun_smg_gatherer", "拾い手のSMG", ItemKind.RANGED_WEAPON,
            desc = "種類:サブマシンガン　構えているだけでドロップ品が寄ってくる",
            weaponType = "smg", traits = setOf(ItemTrait.MAGNET),
        ),

        // --- v2.186 NG+質的アンロック: the returner's rail. A cleared account (Endings.clears≥1)
        // flies with it in its pack; ngPlusOnly keeps it OUT of every drop/shop pool, so a fresh
        // sky stays byte-identical. railgun ballistics + MAGNET = zero new WeaponDef (id is contract).
        ItemDef(
            "gun_railgun_veteran", "還りのレール", ItemKind.RANGED_WEAPON,
            desc = "種類:レールガン　周回の証——貫き、ドロップ品を引き寄せる",
            weaponType = "railgun", traits = setOf(ItemTrait.MAGNET), ngPlusOnly = true,
        ),

        // --- v2.190 NG+第2の解禁: the returner's badge. A twice-cleared account (Endings.clears≥2)
        // carries it in the starting pack; like the rail, ngPlusOnly keeps it OUT of every drop/shop
        // pool, so an uncleared or single-clear sky stays byte-identical. A quiet all-round boon that
        // suits a veteran — passive regen, faster stamina, a shade of resilience (id is contract).
        ItemDef(
            "acc_returner_badge", "周回者の徽章", ItemKind.ACCESSORY,
            desc = "HP毎秒1.0回復・回復+10%・被ダメ-5%",
            hpRegen = 1.0f, staRegenMul = 1.1f, damageMul = 0.95f, ngPlusOnly = true,
        ),
    )

    private val byId = ALL.associateBy { it.id }

    /** Resolve an id — including v2.47 honed ids ("gun_mg+2"), derived from their base def. */
    fun byId(id: String): ItemDef? {
        byId[id]?.let { return it }
        if ("+" in id) {
            val base = byId[GearCraft.baseId(id)] ?: return null
            return GearCraft.leveled(base, GearCraft.level(id))
        }
        return null
    }

    /** The RANGED_WEAPON item that behaves as the given WeaponDef id (pistol/shotgun/…). */
    fun byWeaponType(weaponType: String): ItemDef? = ALL.firstOrNull { it.weaponType == weaponType }

    // Drop pools (v2.34): ~55% consumables / ~30% equipment / ~15% 資料品 (see [dropFor]).
    private val consumables = ALL.filter { it.kind == ItemKind.CONSUMABLE }
    private val loreItems = ALL.filter { it.kind == ItemKind.LORE }
    private val equipment = ALL.filter { it.kind != ItemKind.CONSUMABLE && it.kind != ItemKind.LORE && !it.ngPlusOnly } // v2.186

    /** What a fresh run wears: pistol + knife on a standard thruster in a pilot suit. */
    fun starterLoadout(): Loadout = Loadout(
        thruster = byId("thruster_std"),
        armor = byId("armor_cloth"),
        ranged = byId("gun_pistol"),
        melee = byId("melee_knife"),
    )

    /** What a fresh run carries: the rest of the classic guns, an OC thruster to try full throttle,
     *  a couple of consumables and one 資料品 (sellable, v2.34). */
    fun starterBackpack(ngPlus: Boolean = false, ngPlus2: Boolean = false): MutableList<ItemDef> = mutableListOf(
        byId("gun_shotgun")!!, byId("gun_mg")!!, byId("gun_beam")!!, byId("gun_grenade")!!,
        byId("thruster_oc")!!,
        byId("med_spray")!!, byId("smoke_bomb")!!,
        byId("lore_letter")!!,
    ).apply {
        if (ngPlus) add(byId("gun_railgun_veteran")!!) // v2.186 NG+質的アンロック
        if (ngPlus2) add(byId("acc_returner_badge")!!) // v2.190 NG+第2の解禁
    }

    /** A deterministic drop pick: ~55% consumable / ~30% equipment / ~15% data sample (kill loot rolls an index). */
    fun dropFor(roll: Int): ItemDef {
        val r = ((roll % 1000) + 1000) % 1000
        val pool = when {
            r < 550 -> consumables
            r < 850 -> equipment
            else -> loreItems
        }
        return pool[r % pool.size]
    }

    private val guns = ALL.filter { it.kind == ItemKind.RANGED_WEAPON && !it.ngPlusOnly } // v2.186 NG+質的アンロック

    /** v2.40: a deterministic GUN pick — the deep-wall caches yield weapons, nothing else. */
    fun gunFor(roll: Int): ItemDef = guns[((roll % guns.size) + guns.size) % guns.size]

    /** The backpack grouped for display: one row per distinct item (first-encounter order) with its count. */
    fun grouped(backpack: List<ItemDef>): List<Pair<ItemDef, Int>> =
        backpack.groupBy { it.id }.map { (_, items) -> items.first() to items.size }
}
