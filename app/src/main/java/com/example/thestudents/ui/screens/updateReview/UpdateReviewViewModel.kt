package com.example.thestudents.ui.screens.updateReview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UpdateReviewState())
    val uiState: StateFlow<UpdateReviewState> = _uiState.asStateFlow()

    fun updateRating(input: Int) {
        _uiState.update { it.copy(rating = input) }
    }

    fun loadReview(reviewId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, reviewId = reviewId) }
            val result = reviewRepository.getReviewById(reviewId)
            if (result.isSuccess) {
                val reviewData = result.getOrNull()
                if (reviewData != null) {
                    val initialRating = reviewData.rating?.toDoubleOrNull()?.toInt() ?: 0
                    _uiState.update {
                        it.copy(
                            student = reviewData.reviewedStudent,
                            className = reviewData.classReviewed,
                            period = reviewData.periodReviewed,
                            rating = initialRating,
                            review = reviewData.content,
                            isLoading = false,
                            error = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Reseña no encontrada"
                        )
                    }
                }
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Error al cargar la reseña"
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = errorMsg
                    )
                }
            }
        }
    }

    fun updateReview(input: String) {
        _uiState.update { it.copy(review = input) }
    }

    fun updateIsAnonymous(input: Boolean) {
        _uiState.update { it.copy(isAnonymous = input) }
    }

    fun updateReview() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val state = _uiState.value
            val ratingStr = if (state.rating > 0) state.rating.toString() else null
            val estadoStr = if (state.isAnonymous) "anonimo" else "activo"

            val result = reviewRepository.updateReview(
                reviewId = state.reviewId,
                rating = ratingStr,
                contenido = state.review,
                estado = estadoStr
            )

            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, navigateToProfile = true, error = null) }
            } else {
                _uiState.update { it.copy(isLoading = false, error = result.exceptionOrNull()?.message) }
            }
        }
    }
}
