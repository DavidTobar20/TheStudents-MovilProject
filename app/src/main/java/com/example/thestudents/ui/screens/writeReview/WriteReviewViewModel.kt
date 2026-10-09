package com.example.thestudents.ui.screens.writeReview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.local.localInscriptionProvider
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(WriteReviewState())
    val uiState: StateFlow<WriteReviewState> = _uiState.asStateFlow()

    fun updateRating(input: Int) {
        _uiState.update { it.copy(rating = input) }
    }

    fun updateReview(input: String) {
        _uiState.update { it.copy(review = input) }
    }

    fun updateIsAnonymous(input: Boolean) {
        _uiState.update { it.copy(isAnonymous = input) }
    }

    fun getStudentById(id: String) {
        initReview(id, null)
    }

    fun initReview(studentId: String, reviewId: String?) {
        val student = localStudentProvider.getStudentById(studentId)
        val currentUserId = localStudentProvider.currentUser.id

        val shared = localInscriptionProvider.getSharedClassFor(currentUserId, studentId)
        val courseInfo = if (shared != null) {
            "${shared.first} · ${shared.second}"
        } else {
            student?.program ?: ""
        }

        if (reviewId != null) {
            _uiState.update {
                it.copy(
                    student = student,
                    isEditMode = true,
                    reviewId = reviewId,
                    courseInfo = courseInfo,
                    isLoading = true,
                    errorMessage = null,
                    saveSuccess = false
                )
            }
            viewModelScope.launch {
                val result = reviewRepository.getReviewsByReviewerId(currentUserId)
                val existingReview = result.getOrNull()?.find { it.id == reviewId }
                    ?: localReviewsProvider.allReviews.find { it.id == reviewId }

                if (existingReview != null) {
                    val parsedRating = existingReview.rating?.toFloatOrNull()?.toInt() ?: 0
                    _uiState.update {
                        it.copy(
                            rating = parsedRating,
                            review = existingReview.content,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        } else {
            _uiState.update {
                it.copy(
                    student = student,
                    isEditMode = false,
                    reviewId = null,
                    courseInfo = courseInfo,
                    rating = 0,
                    review = "",
                    isLoading = false,
                    errorMessage = null,
                    saveSuccess = false
                )
            }
        }
    }

    fun saveReview() {
        val currentState = _uiState.value
        val student = currentState.student ?: return
        val currentUserId = localStudentProvider.currentUser.id

        if (currentState.review.isBlank()) {
            _uiState.update { it.copy(errorMessage = "La reseña no puede estar vacía") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            if (currentState.isEditMode && currentState.reviewId != null) {
                val ratingStr = if (currentState.rating > 0) currentState.rating.toString() else null
                val result = reviewRepository.updateReview(
                    reviewId = currentState.reviewId,
                    rating = ratingStr,
                    contenido = currentState.review,
                    estado = null
                )
                if (result.isSuccess) {
                    _uiState.update { it.copy(isLoading = false, saveSuccess = true) }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.exceptionOrNull()?.message ?: "Error al actualizar la reseña"
                        )
                    }
                }
            } else {
                val shared = localInscriptionProvider.getSharedClassFor(currentUserId, student.id)
                val materia = shared?.first ?: "Estructuras de Datos (ISIS1206)"
                val periodo = shared?.second ?: "2025-1"
                val ratingStr = if (currentState.rating > 0) currentState.rating.toString() else null

                val result = reviewRepository.createReview(
                    autorId = currentUserId,
                    resenadoId = student.id,
                    materia = materia,
                    periodo = periodo,
                    rating = ratingStr,
                    contenido = currentState.review
                )
                if (result.isSuccess) {
                    _uiState.update { it.copy(isLoading = false, saveSuccess = true) }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.exceptionOrNull()?.message ?: "Error al crear la reseña"
                        )
                    }
                }
            }
        }
    }
}
