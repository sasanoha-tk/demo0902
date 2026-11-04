package io.github.panda17tk.arpg.sim

import io.github.panda17tk.arpg.math.Rng

/**
 * v2.91 星系の個性 — every system past the first has a temperament, drawn deterministically
 * from its seed. The trait colors the waves (fill), the economy (dust), the pull of planets
 * (gravity) and the surge pressure (quota). Pure rules; the surge/gravity systems consult them.
 */
enum class SystemTrait(val label: String) {
    NONE(""),
    STORMY("嵐の星系"),
    HEAVY("重い星系"),
    CUSTODIAL("保守の星系"),
    RICH("豊かな星系"),
    SWARMING("多生の星系"), // v2.177 濃い外縁
    DESOLATE("痩せた星系"), // v2.183 濃い外縁II
    RESONANT("残響の星系"),
    AIRY("軽い星系"),
}

object SystemTraits {
    /** The first system is always calm (the teaching sky); later ones draw a temperament. */
    fun traitFor(systemSeed: Long): SystemTrait {
        if (systemSeed <= 1L) return SystemTrait.NONE
        val pool = listOf(SystemTrait.STORMY, SystemTrait.HEAVY, SystemTrait.CUSTODIAL, SystemTrait.RICH, SystemTrait.SWARMING) // v2.177
        val base = pool[Rng(systemSeed * -0x61c8864680b583ebL).nextInt(pool.size)]
        // v2.183 濃い外縁II: a second, independent stream sometimes deepens the temperament into a
        // rarer rim-trait. Layered so the base stream above is untouched — only its RESULT is
        // occasionally overridden, so no existing seed's rng sequence shifts.
        val rim = listOf(SystemTrait.DESOLATE, SystemTrait.RESONANT, SystemTrait.AIRY)
        val r = Rng(systemSeed * 0x2545F4914F6CDD1DL)
        return if (r.nextInt(4) == 0) rim[r.nextInt(rim.size)] else base
    }

    /** A quiet wave inherits the system's weather; scheduled events always win. */
    fun fillEvent(trait: SystemTrait, base: WaveEvent, wave: Int): WaveEvent = when {
        base != WaveEvent.NONE -> base
        trait == SystemTrait.STORMY -> WaveEvent.STORM
        trait == SystemTrait.CUSTODIAL && wave % 3 == 1 -> WaveEvent.PURGE
        trait == SystemTrait.RESONANT && wave % 2 == 0 -> WaveEvent.STORM // v2.183: the rim keeps ringing
        else -> base
    }

    /** RICH presses harder; DESOLATE runs lean — fewer bodies in a starved sky. */
    fun quotaMul(trait: SystemTrait): Float = when (trait) {
        SystemTrait.RICH -> 1.2f
        SystemTrait.DESOLATE -> 0.75f // v2.183 濃い外縁II
        else -> 1f
    }

    /** RICH and DESOLATE both pay a richer dust pile per kill (one lush, one lean-but-precious). */
    fun dustBonus(trait: SystemTrait): Int =
        if (trait == SystemTrait.RICH || trait == SystemTrait.DESOLATE) 1 else 0 // v2.183

    /** HEAVY planets pull half again as hard; AIRY ones barely pull. */
    fun gravityMul(trait: SystemTrait): Float = when (trait) {
        SystemTrait.HEAVY -> 1.5f
        SystemTrait.AIRY -> 0.6f // v2.183 濃い外縁II
        else -> 1f
    }
}
