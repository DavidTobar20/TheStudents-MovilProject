package com.example.thestudents.repository

import com.example.thestudents.data.CandidateSignals
import com.example.thestudents.data.MatchReason
import com.example.thestudents.data.MatchTier
import com.example.thestudents.data.ReceivedRating
import com.example.thestudents.data.repository.TeamMatchScorer
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TeamMatchScorerTest {

    private val me = "yo"
    private val course = "Estructuras de Datos"

    private fun ratings(vararg values: Double, course: String = this.course, reviewer: String = "otro") =
        values.mapIndexed { i, v -> ReceivedRating(v, "$reviewer$i", course, "2026-2") }

    @Test
    fun `un estudiante sin resenas queda neutral y marcado como por conocer`() {
        val result = TeamMatchScorer.score(CandidateSignals("a", emptyList()), me, course)

        assertThat(result.tier).isEqualTo(MatchTier.TO_DISCOVER)
        assertThat(result.score).isIn(40..55)
        assertThat(result.reasons).contains(MatchReason.FewReviews)
        assertThat(result.average).isNull()
    }

    @Test
    fun `muchas resenas altas le ganan a una sola resena perfecta`() {
        val consistente = CandidateSignals("consistente", ratings(4.8, 4.8, 4.8, 4.8, 4.8, 4.8, 4.8, 4.8, 4.8, 4.8))
        val suerte = CandidateSignals("suerte", ratings(5.0))

        val ranked = TeamMatchScorer.rank(listOf(suerte, consistente), me, course)

        assertThat(ranked.first().studentId).isEqualTo("consistente")
        assertThat(ranked.first().tier).isEqualTo(MatchTier.IDEAL)
    }

    @Test
    fun `las resenas de la misma materia pesan mas que las de otras`() {
        val enEstaMateria = CandidateSignals("a", ratings(5.0, 5.0, 3.0, 3.0, course = course))
        val enOtra = CandidateSignals(
            "b",
            ratings(5.0, 5.0, course = "Cálculo") + ratings(3.0, 3.0, course = course, reviewer = "x")
        )

        val a = TeamMatchScorer.score(enEstaMateria, me, course)
        val b = TeamMatchScorer.score(enOtra, me, course)

        // Mismo promedio simple (4.0), pero "a" sacó los 5 en esta materia
        assertThat(a.average).isEqualTo(b.average)
        assertThat(a.score).isGreaterThan(b.score)
    }

    @Test
    fun `si yo lo califique mal queda como riesgo y no se sugiere en el equipo`() {
        val conflictivo = CandidateSignals(
            "conflictivo",
            ratings(5.0, 5.0, 5.0, 5.0) + ReceivedRating(1.0, me, course, "2025-1")
        )
        val normal = CandidateSignals("normal", ratings(3.5, 3.5))

        val ranked = TeamMatchScorer.rank(listOf(conflictivo, normal), me, course)
        val resultConflictivo = ranked.first { it.studentId == "conflictivo" }

        assertThat(resultConflictivo.tier).isEqualTo(MatchTier.RISK)
        assertThat(resultConflictivo.score).isAtMost(TeamMatchScorer.RISK_SCORE_CAP)
        assertThat(resultConflictivo.reasons.first()).isInstanceOf(MatchReason.WorkedTogetherBadly::class.java)
        assertThat(TeamMatchScorer.suggestTeam(ranked, teamSize = 3)).containsExactly("normal")
    }

    @Test
    fun `haber trabajado bien juntos y seguirlo sube el match`() {
        val base = CandidateSignals("a", ratings(4.0, 4.0, 4.0))
        val conHistoria = base.copy(
            receivedRatings = base.receivedRatings + ReceivedRating(5.0, me, course, "2025-2"),
            ratingsTheyGaveMe = listOf(5.0),
            iFollowThem = true,
            sharedCoursesWithMe = 3
        )

        val sinHistoria = TeamMatchScorer.score(base, me, course)
        val resultado = TeamMatchScorer.score(conHistoria, me, course)

        assertThat(resultado.score).isGreaterThan(sinHistoria.score)
        assertThat(resultado.reasons).contains(MatchReason.YouFollow)
        assertThat(resultado.reasons).contains(MatchReason.SharedCourses(3))
        assertThat(resultado.reasons.any { it is MatchReason.WorkedTogetherWell }).isTrue()
        assertThat(resultado.reasons.any { it is MatchReason.RatedMeWell }).isTrue()
    }

    @Test
    fun `resenas muy bajas de otros companeros marcan riesgo`() {
        val result = TeamMatchScorer.score(CandidateSignals("a", ratings(1.0, 2.0, 2.0, 1.5)), me, course)

        assertThat(result.tier).isEqualTo(MatchTier.RISK)
        assertThat(result.reasons).contains(MatchReason.LowReviews)
    }

    @Test
    fun `el equipo sugerido respeta el tamano y el puntaje del equipo es el promedio`() {
        val candidatos = listOf(
            CandidateSignals("a", ratings(5.0, 5.0, 5.0, 5.0, 5.0)),
            CandidateSignals("b", ratings(4.5, 4.5, 4.5)),
            CandidateSignals("c", ratings(4.0, 4.0)),
            CandidateSignals("d", emptyList())
        )
        val ranked = TeamMatchScorer.rank(candidatos, me, course)

        val equipo = TeamMatchScorer.suggestTeam(ranked, teamSize = 3)
        assertThat(equipo).containsExactly("a", "b").inOrder()

        val miembros = ranked.filter { it.studentId in equipo }
        assertThat(TeamMatchScorer.teamScore(miembros))
            .isEqualTo(((miembros[0].score + miembros[1].score) / 2.0).let { Math.round(it).toInt() })
        assertThat(TeamMatchScorer.teamScore(emptyList())).isEqualTo(0)
        assertThat(TeamMatchScorer.suggestTeam(ranked, teamSize = 1)).isEmpty()
    }

    @Test
    fun `el usuario nunca aparece como candidato de si mismo`() {
        val ranked = TeamMatchScorer.rank(
            listOf(CandidateSignals(me, ratings(5.0)), CandidateSignals("a", ratings(4.0))),
            me,
            course
        )
        assertThat(ranked.map { it.studentId }).containsExactly("a")
    }
}
