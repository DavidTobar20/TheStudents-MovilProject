package com.example.thestudents.ui.screens.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.data.repository.InscripcionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val inscripcionRepository: InscripcionRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(ReviewsState())
    val uiState: StateFlow<ReviewsState> = _uiState

    init {
        getAllSections()
    }

    fun getAllSections() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val userId = localStudentProvider.currentUser.id
            val result = inscripcionRepository.getInscripcionesPorUsuario(userId)
            if (result.isSuccess) {
                val sections = result.getOrNull() ?: emptyList()
                _uiState.update {
                    it.copy(
                        sections = sections,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } else {
                val error = result.exceptionOrNull()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error?.message ?: "Error al cargar las secciones"
                    )
                }
            }
        }
    }
}
