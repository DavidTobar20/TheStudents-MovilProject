package com.example.thestudents.ui.screens.teamMatch

import com.example.thestudents.data.TeamMatchCandidate
import com.example.thestudents.data.repository.TeamMatchScorer

enum class TeamMatchStep { SELECT_COURSE, RESULTS }

data class TeamMatchState(
    val step: TeamMatchStep = TeamMatchStep.SELECT_COURSE,
    val periodo: String = "",                               // semestre para el que se arma el equipo

    // Paso 1: elegir la materia que va a ver
    val isLoadingCourses: Boolean = false,
    val coursesError: String? = null,
    val availableCourses: List<String> = emptyList(),
    val mySearches: List<String> = emptyList(),
    val selectedCourse: String = "",
    val isStartingSearch: Boolean = false,

    // Paso 2: recomendaciones
    val materia: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val candidates: List<TeamMatchCandidate> = emptyList(), // quienes también buscan equipo, del mejor al peor match
    val teamSize: Int = DEFAULT_TEAM_SIZE,                  // integrantes contando al usuario
    val selectedIds: Set<String> = emptySet(),
    val isSendingInvites: Boolean = false,
    val invitesSent: Boolean = false,
    val invitedCount: Int = 0,
    val inviteError: String? = null,
    val showHowItWorks: Boolean = false
) {
    val selectedCandidates: List<TeamMatchCandidate>
        get() = candidates.filter { it.student.id in selectedIds }

    val teamScore: Int
        get() = TeamMatchScorer.teamScore(selectedCandidates.map { it.match })

    val isTeamFull: Boolean
        get() = selectedIds.size >= teamSize - 1

    // No tiene sentido pedir un equipo más grande que los compañeros disponibles
    val maxTeamSize: Int
        get() = (candidates.size + 1).coerceIn(MIN_TEAM_SIZE, MAX_TEAM_SIZE)

    companion object {
        const val MIN_TEAM_SIZE = 2
        const val MAX_TEAM_SIZE = 6
        const val DEFAULT_TEAM_SIZE = 3
    }
}
