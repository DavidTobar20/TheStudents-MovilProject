package com.example.thestudents.ui.screens.teamMatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.TeamMatchCandidate
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.data.repository.AuthRepository
import com.example.thestudents.data.repository.TeamMatchRepository
import com.example.thestudents.data.AcademicPeriod
import com.example.thestudents.data.repository.TeamMatchScorer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamMatchViewModel @Inject constructor(
    private val teamMatchRepository: TeamMatchRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TeamMatchState(periodo = AcademicPeriod.upcoming()))
    val uiState: StateFlow<TeamMatchState> = _uiState

    private val currentUserId: String
        get() = authRepository.currentUser?.uid ?: localStudentProvider.currentUser.id

    init {
        loadCourseOptions()
    }

    // ---------- Paso 1: elegir la materia ----------

    fun loadCourseOptions() {
        val periodo = _uiState.value.periodo
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCourses = true, coursesError = null) }
            val result = teamMatchRepository.getCourseOptions(currentUserId, periodo)
            if (result.isSuccess) {
                val options = result.getOrNull()
                _uiState.update { state ->
                    val available = options?.availableCourses.orEmpty()
                    state.copy(
                        isLoadingCourses = false,
                        availableCourses = available,
                        mySearches = options?.mySearches.orEmpty(),
                        selectedCourse = state.selectedCourse.ifBlank { available.firstOrNull().orEmpty() }
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoadingCourses = false,
                        coursesError = result.exceptionOrNull()?.message ?: "No se pudieron cargar las materias"
                    )
                }
            }
        }
    }

    fun onCourseSelected(materia: String) {
        _uiState.update { it.copy(selectedCourse = materia) }
    }

    /** Marca al usuario como "buscando equipo" en la materia elegida y carga las recomendaciones. */
    fun searchTeam(materia: String = _uiState.value.selectedCourse) {
        if (materia.isBlank() || _uiState.value.isStartingSearch) return
        val periodo = _uiState.value.periodo

        viewModelScope.launch {
            _uiState.update { it.copy(isStartingSearch = true, coursesError = null) }
            val result = teamMatchRepository.startTeamSearch(currentUserId, materia, periodo)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        isStartingSearch = false,
                        mySearches = (it.mySearches + materia).distinct().sorted()
                    )
                }
                loadRecommendations(materia)
            } else {
                _uiState.update {
                    it.copy(
                        isStartingSearch = false,
                        coursesError = result.exceptionOrNull()?.message ?: "No se pudo registrar la búsqueda"
                    )
                }
            }
        }
    }

    /** Deja de buscar equipo en una materia (ya no aparece en las recomendaciones de otros). */
    fun stopSearch(materia: String) {
        val periodo = _uiState.value.periodo
        viewModelScope.launch {
            val result = teamMatchRepository.stopTeamSearch(currentUserId, materia, periodo)
            if (result.isSuccess) {
                _uiState.update { it.copy(mySearches = it.mySearches - materia) }
            } else {
                _uiState.update {
                    it.copy(coursesError = result.exceptionOrNull()?.message ?: "No se pudo cancelar la búsqueda")
                }
            }
        }
    }

    /** Vuelve al paso 1 para elegir otra materia. */
    fun changeCourse() {
        _uiState.update { it.copy(step = TeamMatchStep.SELECT_COURSE, coursesError = null) }
    }

    // ---------- Paso 2: recomendaciones ----------

    fun loadRecommendations(materia: String = _uiState.value.materia) {
        val periodo = _uiState.value.periodo
        _uiState.update {
            it.copy(
                step = TeamMatchStep.RESULTS,
                materia = materia,
                isLoading = true,
                errorMessage = null,
                candidates = emptyList(),
                selectedIds = emptySet(),
                invitesSent = false,
                inviteError = null
            )
        }
        viewModelScope.launch {
            val result = teamMatchRepository.getRecommendedTeammates(currentUserId, materia, periodo)
            if (result.isSuccess) {
                val candidates = result.getOrDefault(emptyList())
                _uiState.update { state ->
                    val teamSize = TeamMatchState.DEFAULT_TEAM_SIZE.coerceAtMost((candidates.size + 1).coerceAtLeast(TeamMatchState.MIN_TEAM_SIZE))
                    state.copy(
                        isLoading = false,
                        candidates = candidates,
                        teamSize = teamSize,
                        selectedIds = suggestedIds(candidates, teamSize)
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message
                            ?: "No se pudieron cargar las recomendaciones"
                    )
                }
            }
        }
    }

    /** Cambia cuántos integrantes tendrá el equipo y vuelve a sugerir los mejores. */
    fun onTeamSizeChange(size: Int) {
        _uiState.update { state ->
            val newSize = size.coerceIn(TeamMatchState.MIN_TEAM_SIZE, state.maxTeamSize)
            state.copy(
                teamSize = newSize,
                selectedIds = suggestedIds(state.candidates, newSize),
                invitesSent = false,
                inviteError = null
            )
        }
    }

    /** Agrega o quita un compañero del equipo (sin pasarse del tamaño elegido). */
    fun toggleCandidate(studentId: String) {
        _uiState.update { state ->
            val selected = state.selectedIds
            val newSelection = when {
                studentId in selected -> selected - studentId
                state.isTeamFull -> selected
                else -> selected + studentId
            }
            state.copy(selectedIds = newSelection, invitesSent = false, inviteError = null)
        }
    }

    /** Vuelve al equipo que recomienda el algoritmo. */
    fun resetToSuggestion() {
        _uiState.update { state ->
            state.copy(
                selectedIds = suggestedIds(state.candidates, state.teamSize),
                invitesSent = false,
                inviteError = null
            )
        }
    }

    fun sendInvites() {
        val state = _uiState.value
        if (state.selectedIds.isEmpty() || state.isSendingInvites) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSendingInvites = true, inviteError = null) }
            val result = teamMatchRepository.sendTeamInvites(
                fromUserId = currentUserId,
                toUserIds = state.selectedCandidates.map { it.student.id },
                materia = state.materia,
                periodo = state.periodo
            )
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        isSendingInvites = false,
                        invitesSent = true,
                        invitedCount = result.getOrDefault(0)
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isSendingInvites = false,
                        inviteError = result.exceptionOrNull()?.message
                            ?: "No se pudieron enviar las invitaciones"
                    )
                }
            }
        }
    }

    fun showHowItWorks(show: Boolean) {
        _uiState.update { it.copy(showHowItWorks = show) }
    }

    private fun suggestedIds(candidates: List<TeamMatchCandidate>, teamSize: Int): Set<String> =
        TeamMatchScorer.suggestTeam(candidates.map { it.match }, teamSize).toSet()
}
