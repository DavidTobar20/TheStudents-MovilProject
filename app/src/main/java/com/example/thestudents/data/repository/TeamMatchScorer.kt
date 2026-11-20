package com.example.thestudents.data.repository

import com.example.thestudents.data.CandidateSignals
import com.example.thestudents.data.MatchReason
import com.example.thestudents.data.MatchResult
import com.example.thestudents.data.MatchTier
import com.example.thestudents.data.ReceivedRating
import kotlin.math.min
import kotlin.math.roundToInt

/*
 * "Arma tu equipo": lógica para recomendar compañeros de trabajo en grupo a partir
 * de las reseñas. No depende de Android ni de Firebase, así que se puede probar fácil.
 */
object TeamMatchScorer {

    // Pesos de cada componente (suman 1.0)
    const val WEIGHT_REPUTATION = 0.55
    const val WEIGHT_CONFIDENCE = 0.15
    const val WEIGHT_AFFINITY = 0.20
    const val WEIGHT_SOCIAL = 0.10

    // Promedio bayesiano: se parte de 3.5 estrellas "virtuales" con peso de 3 reseñas.
    // Así alguien con una sola reseña de 5 no le gana a alguien con 10 reseñas de 4.8.
    const val PRIOR_MEAN = 3.5
    const val PRIOR_WEIGHT = 3.0

    // Las reseñas de la misma materia pesan más que las de otras materias
    const val SAME_COURSE_WEIGHT = 1.5

    // Con 8 reseñas o más la confianza es total
    const val FULL_CONFIDENCE_REVIEWS = 8

    const val IDEAL_THRESHOLD = 70
    const val GOOD_THRESHOLD = 56
    const val RISK_SCORE_CAP = 40

    fun score(candidate: CandidateSignals, myId: String, course: String): MatchResult {
        // Mis propias reseñas sobre él cuentan como "experiencia directa", no como reputación
        val myRatings = candidate.receivedRatings.filter { it.reviewerId == myId }.map { it.value }
        val othersRatings = candidate.receivedRatings.filter { it.reviewerId != myId }

        // 1. Reputación (promedio bayesiano ponderado por materia)
        var weightedSum = 0.0
        var totalWeight = 0.0
        othersRatings.forEach { rating ->
            val weight = if (rating.course.equals(course, ignoreCase = true)) SAME_COURSE_WEIGHT else 1.0
            weightedSum += rating.value.coerceIn(0.0, 5.0) * weight
            totalWeight += weight
        }
        val bayesian = (PRIOR_MEAN * PRIOR_WEIGHT + weightedSum) / (PRIOR_WEIGHT + totalWeight)
        val reputation = bayesian / 5.0

        // 2. Confianza: más reseñas = más seguro el dato. Se multiplica por la reputación
        // para que tener muchas reseñas malas no sume puntos.
        val confidence = min(othersRatings.size, FULL_CONFIDENCE_REVIEWS).toDouble() / FULL_CONFIDENCE_REVIEWS

        // 3. Afinidad: cómo nos fue trabajando juntos (yo a él y él a mí). Sin datos = neutral.
        val myAverage = myRatings.averageOrNull()
        val theirAverage = candidate.ratingsTheyGaveMe.averageOrNull()
        val affinityValues = listOfNotNull(myAverage, theirAverage)
        val affinity = if (affinityValues.isEmpty()) 0.5 else affinityValues.average() / 5.0

        // 4. Social: lo sigo y/o compartimos varias materias
        val social = (if (candidate.iFollowThem) 0.5 else 0.0) +
                (min(candidate.sharedCoursesWithMe.coerceAtLeast(0), 3) / 3.0) * 0.5

        val raw = WEIGHT_REPUTATION * reputation +
                WEIGHT_CONFIDENCE * confidence * reputation +
                WEIGHT_AFFINITY * affinity +
                WEIGHT_SOCIAL * social
        var score = (raw * 100).roundToInt().coerceIn(0, 100)

        // Señales de alerta: yo ya trabajé con él y me fue mal, o tiene reseñas muy bajas
        val allValues = candidate.receivedRatings.map { it.value }
        val simpleAverage = allValues.averageOrNull()
        val workedBadly = myAverage != null && myAverage <= 2.0
        val lowReviews = othersRatings.size >= 3 && othersRatings.map { it.value }.average() < 2.5
        val isRisk = workedBadly || lowReviews
        if (isRisk) score = min(score, RISK_SCORE_CAP)

        val tier = when {
            isRisk -> MatchTier.RISK
            score >= IDEAL_THRESHOLD -> MatchTier.IDEAL
            score >= GOOD_THRESHOLD -> MatchTier.GOOD
            else -> MatchTier.TO_DISCOVER
        }

        return MatchResult(
            studentId = candidate.studentId,
            score = score,
            tier = tier,
            reasons = buildReasons(candidate, othersRatings, course, myAverage, theirAverage, workedBadly, lowReviews),
            average = simpleAverage,
            reviewsCount = allValues.size
        )
    }

    /** Ordena a los compañeros del mejor al peor match. */
    fun rank(candidates: List<CandidateSignals>, myId: String, course: String): List<MatchResult> {
        return candidates
            .filter { it.studentId != myId }
            .distinctBy { it.studentId }
            .map { score(it, myId, course) }
            .sortedWith(
                compareByDescending<MatchResult> { it.score }
                    .thenByDescending { it.reviewsCount }
                    .thenBy { it.studentId }
            )
    }

    /**
     * Equipo sugerido: los mejores compañeros hasta completar [teamSize] contando al usuario.
     * Nunca sugiere a alguien marcado como riesgo.
     */
    fun suggestTeam(ranked: List<MatchResult>, teamSize: Int): List<String> {
        val slots = (teamSize - 1).coerceAtLeast(0)
        return ranked
            .filter { it.tier != MatchTier.RISK }
            .take(slots)
            .map { it.studentId }
    }

    /** Compatibilidad del equipo: promedio del match de los integrantes elegidos. */
    fun teamScore(members: List<MatchResult>): Int {
        if (members.isEmpty()) return 0
        return members.map { it.score }.average().roundToInt()
    }

    private fun buildReasons(
        candidate: CandidateSignals,
        othersRatings: List<ReceivedRating>,
        course: String,
        myAverage: Double?,
        theirAverage: Double?,
        workedBadly: Boolean,
        lowReviews: Boolean
    ): List<MatchReason> {
        val reasons = mutableListOf<MatchReason>()

        if (workedBadly && myAverage != null) reasons += MatchReason.WorkedTogetherBadly(myAverage)
        if (lowReviews) reasons += MatchReason.LowReviews

        if (myAverage != null && myAverage >= 4.0) reasons += MatchReason.WorkedTogetherWell(myAverage)
        if (theirAverage != null && theirAverage >= 4.0) reasons += MatchReason.RatedMeWell(theirAverage)

        val inCourse = othersRatings.filter { it.course.equals(course, ignoreCase = true) }
        if (inCourse.isNotEmpty() && inCourse.map { it.value }.average() >= 4.0) {
            reasons += MatchReason.StrongInCourse(inCourse.map { it.value }.average(), inCourse.size)
        }

        if (othersRatings.isNotEmpty() && !lowReviews) {
            reasons += MatchReason.Reputation(othersRatings.map { it.value }.average(), othersRatings.size)
        }

        if (candidate.iFollowThem) reasons += MatchReason.YouFollow
        if (candidate.sharedCoursesWithMe >= 1) reasons += MatchReason.SharedCourses(candidate.sharedCoursesWithMe)
        if (othersRatings.size < 2) reasons += MatchReason.FewReviews

        return reasons
    }

    private fun List<Double>.averageOrNull(): Double? = if (isEmpty()) null else average()
}
