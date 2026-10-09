package com.example.thestudents.ui.screens.writeReview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.data.repository.InscripcionRepository
import com.example.thestudents.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val inscripcionRepository: InscripcionRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(WriteReviewState())
    val uiState: StateFlow<WriteReviewState> = _uiState

    fun updateRating(input: Int){
        _uiState.update { it.copy(rating = input) }
    }
    
    fun getInscriptionById(inscriptionId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(error = null) }
            val result = inscripcionRepository.getInscripcionById(inscriptionId)
            if (result.isSuccess) {
                val inscription = result.getOrNull()
                if (inscription != null) {
                    _uiState.update {
                        it.copy(
                            student = inscription.student,
                            className = inscription.className,
                            period = inscription.period
                        )
                    }
                }
            } else {
                val error = result.exceptionOrNull()
                _uiState.update {
                    it.copy(error = error?.message ?: "Error al cargar la inscripción")
                }
            }
        }
    }

    fun updateReview(input: String){
        _uiState.update { it.copy(review = input) }
    }

    fun updateIsAnonymous(input: Boolean){
        _uiState.update { it.copy(isAnonymous = input) }
    }

    fun createReview() {
        viewModelScope.launch {
            val state = _uiState.value
            val autorId = localStudentProvider.currentUser.id
            val resenadoId = state.student?.id ?: return@launch
            val ratingStr = if (state.rating > 0) state.rating.toString() else null

            val result = reviewRepository.createReview(
                autorId = autorId,
                resenadoId = resenadoId,
                materia = state.className,
                periodo = state.period,
                rating = ratingStr,
                contenido = state.review
            )

            if (result.isSuccess) {
                _uiState.update { it.copy(navigateToHome = true, error = null) }
            } else {
                _uiState.update { it.copy(error = result.exceptionOrNull()?.message) }
            }
        }
    }
}
