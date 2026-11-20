package com.example.thestudents.data

import java.util.Calendar

// Modelos de "Arma tu equipo" (recomendador de compañeros para trabajos en grupo)

/** Una calificación que recibió un compañero. */
data class ReceivedRating(
    val value: Double,      // 0 a 5 estrellas
    val reviewerId: String, // quién la escribió
    val course: String,     // materia en la que se le calificó
    val period: String
)

/** Todo lo que sabemos de un compañero para calcular el match. */
data class CandidateSignals(
    val studentId: String,
    val receivedRatings: List<ReceivedRating>,
    val ratingsTheyGaveMe: List<Double> = emptyList(), // si él ya me reseñó a mí
    val sharedCoursesWithMe: Int = 0,                   // materias que ya vimos juntos
    val iFollowThem: Boolean = false
)

enum class MatchTier { IDEAL, GOOD, TO_DISCOVER, RISK }

/** Razones que se le muestran al usuario para que entienda por qué se recomienda a alguien. */
sealed class MatchReason {
    data class Reputation(val average: Double, val count: Int) : MatchReason()
    data class StrongInCourse(val average: Double, val count: Int) : MatchReason()
    data class WorkedTogetherWell(val myRating: Double) : MatchReason()
    data class WorkedTogetherBadly(val myRating: Double) : MatchReason()
    data class RatedMeWell(val rating: Double) : MatchReason()
    data class SharedCourses(val count: Int) : MatchReason()
    data object YouFollow : MatchReason()
    data object FewReviews : MatchReason()
    data object LowReviews : MatchReason()
}

data class MatchResult(
    val studentId: String,
    val score: Int,                 // 0 a 100
    val tier: MatchTier,
    val reasons: List<MatchReason>,
    val average: Double?,           // promedio simple de estrellas recibidas (null si no tiene)
    val reviewsCount: Int
)

/** Compañero recomendado para el equipo: sus datos + el resultado del match. */
data class TeamMatchCandidate(
    val student: Student,
    val match: MatchResult
)

/**
 * Periodos académicos con formato "AAAA-N": enero-junio = "AAAA-1", julio-diciembre = "AAAA-2".
 * Los equipos se arman al inicio de cada semestre, así que "Arma tu equipo" trabaja
 * con el semestre que viene.
 */
object AcademicPeriod {

    /**
     * Semestre para el que se arman equipos:
     * - En los dos primeros meses de un semestre (ene-feb, jul-ago) es el semestre que acaba de empezar.
     * - El resto del tiempo es el siguiente semestre.
     *
     * Ej: noviembre 2026 -> "2027-1"; febrero 2027 -> "2027-1"; abril 2027 -> "2027-2".
     */
    fun upcoming(calendar: Calendar = Calendar.getInstance()): String {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) // 0 = enero
        return when (month) {
            Calendar.JANUARY, Calendar.FEBRUARY -> "$year-1"
            in Calendar.MARCH..Calendar.JUNE -> "$year-2"
            Calendar.JULY, Calendar.AUGUST -> "$year-2"
            else -> "${year + 1}-1" // septiembre a diciembre
        }
    }
}
