package io.github.panda17tk.arpg.sim

/**
 * The pre-landing scan card (Living Planets v2.23): what kind of world this is, and what its
 * memory book holds about you — shown while a landing candidate is latched in space, before the
 * player commits. Built purely from the planet's deterministic context + the run's memory book, so
 * the same planet always scans the same (and GameScreen can cache by planet id). Pure →
 * unit-testable.
 */
data class PlanetCardInfo(
    val title: String,      // biome display name
    val traitLine: String,  // 「気質　◯◯　　聖　◯◯」
    val memoryLine: String, // 未訪問, or the remembered gauges as plain numbers
    val dangerLine: String? = null, // v2.43: 「危険度　★★☆☆」 — biome threat + remembered hostility
) {
    /** Body lines under the title (the hint row is drawn separately by Hud). */
    val lines: List<String> get() = listOfNotNull(traitLine, dangerLine, memoryLine)
}

object PlanetScan {
    const val UNVISITED = "未訪問"

    /**
     * Compose the card for a landing candidate. [known]/[memory] come from the run's PlanetMemoryBook;
     * the memory line prints the remembered gauges as percentages — the record itself, no greeting.
     */
    fun cardFor(p: PlanetBody, known: Boolean, memory: PlanetSocietyState): PlanetCardInfo = PlanetCardInfo(
        title = p.biome.displayName,
        traitLine = PlanetLexicon.traitLine(p.context),
        memoryLine = if (!known) UNVISITED
        else "再訪　敵対 ${(memory.hostility * 100).toInt()}　好意 ${(memory.mercy * 100).toInt()}",
        dangerLine = run { // v2.43: what the scanner can estimate before you commit (E-2)
            val d = dangerFor(p, memory)
            "危険度　" + "★".repeat(d) + "☆".repeat(4 - d)
        },
    )

    /** 1..4: the biome's native threat (apex worlds bite harder) + a remembered grudge adds one. */
    fun dangerFor(p: PlanetBody, memory: PlanetSocietyState): Int {
        val base = when (p.biome) {
            io.github.panda17tk.arpg.planet.PlanetBiome.NATURE -> 2
            io.github.panda17tk.arpg.planet.PlanetBiome.MAGMA -> 3
            io.github.panda17tk.arpg.planet.PlanetBiome.ICE -> 3
            io.github.panda17tk.arpg.planet.PlanetBiome.GAS -> 1
            io.github.panda17tk.arpg.planet.PlanetBiome.DEAD -> 1
            io.github.panda17tk.arpg.planet.PlanetBiome.LONELY -> 1
        }
        return (base + if (memory.hostility >= 0.5f) 1 else 0).coerceIn(1, 4)
    }
}
