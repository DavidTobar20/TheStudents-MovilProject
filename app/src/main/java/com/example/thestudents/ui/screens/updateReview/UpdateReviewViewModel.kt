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

    fun loadReview(reviewId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, reviewId = reviewId) }
            val result = reviewRepository.getReviewById(reviewId)
            if (result.isSuccess) {
                val review = result.getOrNull()
                if (review != null) {
                    val initialRating = review.rating?.toDoubleOrNull()?.toInt() ?: 0
                    _uiState.update {
                        it.copy(
                            review = review,
                            rating = initialRating,
                            reviewContent = review.content,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Reseña no encontrada"
                        )
                    }
                }
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Error al cargar la reseña"
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = errorMsg
                    )
                }
            }
        }
    }

    fun updateRating(input: Int) {
        _uiState.update { it.copy(rating = input) }
    }

    fun updateReviewContent(input: String) {
        _uiState.update { it.copy(reviewContent = input) }
    }

    fun updateIsAnonymous(input: Boolean) {
        _uiState.update { it.copy(isAnonymous = input) }
    }

    fun submitUpdateReview(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val current = _uiState.value
            val result = reviewRepository.updateReview(
                reviewId = current.reviewId,
                rating = current.rating.toString(),
                contenido = current.reviewContent,
                estado = if (current.isAnonymous) "anonimo" else "activo"
            )

            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                onSuccess()
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Error al actualizar la reseña"
                _uiState.update { it.copy(isLoading = false, errorMessage = errorMsg) }
                onSuccess()
            }
        }
    }
}
