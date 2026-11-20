package com.example.thestudents.ui.screens.teamMatch.components

import com.example.thestudents.data.TeamMatchCandidate
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.data.CandidateSignals
import com.example.thestudents.data.ReceivedRating
import com.example.thestudents.data.repository.TeamMatchScorer

/** Datos de ejemplo solo para los @Preview de "Arma tu equipo". */
object TeamMatchPreviewData {
    private const val COURSE = "Estructuras de Datos"

    private fun ratings(vararg values: Double) =
        values.mapIndexed { i, v -> ReceivedRating(v, "r$i", COURSE, "2026-2") }

    val candidates: List<TeamMatchCandidate> by lazy {
        val students = localStudentProvider.students.drop(1).take(4)
        val signals = listOf(
            CandidateSignals("p1", ratings(5.0, 4.5, 5.0, 4.8, 5.0, 4.7), iFollowThem = true, sharedCoursesWithMe = 2),
            CandidateSignals("p2", ratings(4.0, 4.5, 4.0)),
            CandidateSignals("p3", emptyList(), sharedCoursesWithMe = 1),
            CandidateSignals("p4", ratings(2.0, 1.5, 2.0))
        )
        TeamMatchScorer.rank(signals, "yo", COURSE).mapIndexed { index, match ->
            val student = students[index % students.size]
            TeamMatchCandidate(
                student = student.copy(id = match.studentId),
                match = match
            )
        }
    }
}
