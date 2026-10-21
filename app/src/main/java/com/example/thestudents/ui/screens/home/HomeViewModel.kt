package com.example.thestudents.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.repository.AuthRepository
import com.example.thestudents.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    private var reviewsJob: Job? = null

    fun updateLikedReviews(index: Int) {
        val studentId = authRepository.currentUser?.uid ?: return
        val targetReview = _uiState.value.followedReviews.getOrNull(index) ?: return

        viewModelScope.launch {
            val result = reviewRepository.sendOrDeleteReviewLike(targetReview.id, studentId)
            if (result.isSuccess) {
                _uiState.update { state ->
                    state.copy(
                        followedReviews = state.followedReviews.map { review ->
                            if (review.id == targetReview.id) {
                                review.copy(
                                    isLiked = !review.isLiked,
                                    likesCount = if (review.isLiked) review.likesCount - 1 else review.likesCount + 1,
                                    // si tenia dislike, Firestore ya se lo quito en la transaccion
                                    isDisliked = false,
                                    disLikes = if (review.isDisliked) review.disLikes - 1 else review.disLikes
                                )
                            } else {
                                review
                            }
                        }
                    )
                }
            } else {

            }
        }
    }

    fun updateDislikedReviews(index: Int) {
        val studentId = authRepository.currentUser?.uid ?: return
        val targetReview = _uiState.value.followedReviews.getOrNull(index) ?: return

        viewModelScope.launch {
            val result = reviewRepository.sendOrDeleteReviewDislike(targetReview.id, studentId)
            if (result.isSuccess) {
                _uiState.update { state ->
                    state.copy(
                        followedReviews = state.followedReviews.map { review ->
                            if (review.id == targetReview.id) {
                                review.copy(
                                    isDisliked = !review.isDisliked,
                                    disLikes = if (review.isDisliked) review.disLikes - 1 else review.disLikes + 1,
                                    // si tenia like, Firestore ya se lo quito en la transaccion
                                    isLiked = false,
                                    likesCount = if (review.isLiked) review.likesCount - 1 else review.likesCount
                                )
                            } else {
                                review
                            }
                        }
                    )
                }
            } else {

            }
        }
    }

    fun loadReviews() {

        val currentUserId = authRepository.currentUser?.uid ?: ""

        /*viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = reviewRepository.getAllReviewsExcludingStudentId(currentUserId)
            if (result.isSuccess) {
                val reviews = result.getOrNull()
                _uiState.update {
                    it.copy(
                        followedReviews = reviews ?: emptyList(),
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }else {
                val error = result.exceptionOrNull()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error?.message ?: "Error al cargar las reseñas"
                    )
                }
            }
        }*/

        reviewsJob?.cancel()
        reviewsJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            reviewRepository.getReviewsLive(currentUserId)
                .catch { e -> _uiState.update { it.copy(errorMessage = e.message, isLoading = false) } }
                .collect { reviews ->
                    _uiState.update { it.copy(followedReviews = reviews, isLoading = false, errorMessage = null) }
                }
        }
    }

}
