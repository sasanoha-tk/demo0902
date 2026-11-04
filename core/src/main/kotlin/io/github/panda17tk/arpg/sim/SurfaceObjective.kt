package io.github.panda17tk.arpg.sim

import io.github.panda17tk.arpg.planet.PlanetBiome

/**
 * The single HUD line for a planet's surface: the biome name, then the gravest deed the run has already
 * done to this world's society, then what is still left to do, then the non-zero society gauges. Every
 * part is a fact about the run, never a sentence, so the same builder serves a first visit and a return.
 */
object SurfaceObjective {

    
    fun hudLine(
        biome: PlanetBiome,
        elitesAlive: Int,
        society: PlanetSocietyState = PlanetSocietyState(),
        context: PlanetContext = PlanetContext.NEUTRAL,
    ): String {
        val out = ArrayList<String>()
        out += biome.displayName + COLON
        deedTag(society, context)?.let { out += it }
        out += when {
            elitesAlive > 0 -> ORDER_APEX + LB + "残り " + elitesAlive + RB
            society.leaderDefeated && !society.relicClaimed -> "素材を回収せよ"
            else -> "脱出パッドへ戻れ"
        }
        for ((label, value) in listOf(
            "敵対 " to pct(society.hostility),
            "好意 " to pct(society.mercy),
            "生態攪乱 " to pct(society.ecologicalDisruption),
        )) {
            if (value > 0) out += label + value
        }
        return out.joinToString(SEP)
    }

    private const val COLON = "："
    private const val LB = "（"
    private const val RB = "）"
    private const val SEP = "　"
    private const val ORDER_APEX = "この星の主を倒せ"

    /** The most serious deed done so far, read in the planet's own value order (FR-6.4 / FR-7.2). */
    private fun deedTag(s: PlanetSocietyState, c: PlanetContext): String? = when {
        s.childKilled && c.sacredThing == SacredThing.CHILDREN -> SACRED_CHILD_KILLED
        s.childKilled -> CHILD_KILLED
        s.childHarmed && c.sacredThing == SacredThing.CHILDREN -> SACRED_CHILD_HARMED
        s.childHarmed -> CHILD_HARMED
        s.apexKilled && c.sacredThing == SacredThing.APEX -> BEAST_KILLED
        s.predatorKilledNearChild -> PRED_REPELLED
        s.hatchlingKilled || s.nestMotherKilled -> NEST_DESTROYED
        s.apexKilled -> APEX_FALLEN
        else -> null
    }

    private const val SACRED_CHILD_KILLED = "聖なる子を殺害"
    private const val CHILD_KILLED = "幼体を殺害"
    private const val SACRED_CHILD_HARMED = "聖なる子を傷害"
    private const val CHILD_HARMED = "幼体を傷害"
    private const val BEAST_KILLED = "神獣を殺害"
    private const val APEX_FALLEN = "首位捕食者を殺害"
    private const val PRED_REPELLED = "捕食者を排除"
    private const val NEST_DESTROYED = "巣を破壊"

    /** The gauge share printed on the line, as an integer out of 100. */
    private fun pct(v: Float): Int = (v * 100).toInt()
}
