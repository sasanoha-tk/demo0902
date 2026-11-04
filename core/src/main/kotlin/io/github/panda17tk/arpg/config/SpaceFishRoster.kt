package io.github.panda17tk.arpg.config

/**
 * v2.141 図鑑の救済: the single source of truth for which fish the sky actually spawns.
 * WorldFactory draws from THESE lists — so a new fish def added to [defaultEnemies] but
 * forgotten here would simply never swim, however complete the roster looked without it.
 * SpawnCoverageTest asserts every [EnemyDef.swims] def is present in [ALL].
 */
object SpaceFishRoster {
    const val TYRANT = "tyrant_shark"
    const val WHALE = "isle_whale"
    const val PILOT = "pilot_minnow"

    val TINY = listOf("star_sardine", "void_aji")
    val MEDIUMS = listOf("comet_saury", "nebula_herring", "gate_smelt", "drift_capelin")
    val SINGLES = listOf(
        "stardust_minnow", "aurora_trout", "magnet_catfish", "crystal_seahorse", "moon_flounder",
        "glass_icefish", "rust_grouper", "twin_sole", "cloud_puffer", "ring_saba", "debris_goby",
        "silver_arowana", "pale_dolphin", "sun_tang", "echo_pike", "warp_flyfish", "blink_darter",
        "void_koi", "lantern_angler",
    )
    val HUNTERS = listOf("void_shark", "rift_cuda", "abyss_lure", "ember_piranha", "star_moray", "thunder_marlin", "dusk_gar")
    val GIANTS = listOf("gravity_whale", "song_whale", "old_coelacanth")

    val ALL: Set<String> = (TINY + MEDIUMS + SINGLES + HUNTERS + GIANTS + listOf(TYRANT, WHALE, PILOT)).toSet()
}
