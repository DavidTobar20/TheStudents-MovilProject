package com.example.thestudents.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.Review
import com.example.thestudents.data.repository.AuthRepository
import com.example.thestudents.data.repository.ReviewRepository
import com.example.thestudents.ui.screens.home.components.HomeTab
import dagger.hilt.android.lifecycle.HiltViewModel
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

    // Reseñas que tienen un like/dislike guardandose en Firestore.
    // Mientras tanto se ignoran nuevos toques para no mandar transacciones encima de otras.
    private val pendingReviews = mutableSetOf<String>()

    fun onTabSelected(tab: HomeTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    // Reemplaza una reseña de las listas (por id) sin tocar las demas
    private fun replaceReview(updated: Review) {
        _uiState.value = _uiState.value.copy(
            followedReviews = _uiState.value.followedReviews.map { review ->
                if (review.id == updated.id) updated else review
            }
        )
    }

    fun updateLikedReviews(index: Int) {
        val studentId = authRepository.currentUser?.uid ?: return
        val review = _uiState.value.currentReviews.getOrNull(index) ?: return
        if (review.id in pendingReviews) return
        pendingReviews.add(review.id)

        // 1. Se cambia la pantalla de una vez, sin esperar a Firestore
        replaceReview(
            review.copy(
                isLiked = !review.isLiked,
                likesCount = if (review.isLiked) review.likesCount - 1 else review.likesCount + 1,
                // si tenia dislike, la transaccion de Firestore tambien se lo quita
                isDisliked = false,
                disLikes = if (review.isDisliked) review.disLikes - 1 else review.disLikes
            )
        )

        // 2. Se guarda en Firestore por detras; si falla, se vuelve a como estaba
        viewModelScope.launch {
            val result = reviewRepository.sendOrDeleteReviewLike(review.id, studentId)
            pendingReviews.remove(review.id)
            if (result.isFailure) {
                replaceReview(review)
            }
        }
    }

    fun updateDislikedReviews(index: Int) {
        val studentId = authRepository.currentUser?.uid ?: return
        val review = _uiState.value.currentReviews.getOrNull(index) ?: return
        if (review.id in pendingReviews) return
        pendingReviews.add(review.id)

        // 1. Se cambia la pantalla de una vez, sin esperar a Firestore
        replaceReview(
            review.copy(
                isDisliked = !review.isDisliked,
                disLikes = if (review.isDisliked) review.disLikes - 1 else review.disLikes + 1,
                // si tenia like, la transaccion de Firestore tambien se lo quita
                isLiked = false,
                likesCount = if (review.isLiked) review.likesCount - 1 else review.likesCount
            )
        )

        // 2. Se guarda en Firestore por detras; si falla, se vuelve a como estaba
        viewModelScope.launch {
            val result = reviewRepository.sendOrDeleteReviewDislike(review.id, studentId)
            pendingReviews.remove(review.id)
            if (result.isFailure) {
                replaceReview(review)
            }
        }
    }

    fun loadReviews() {
        val currentUserId = authRepository.currentUser?.uid ?: ""

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            reviewRepository.getHomeFeedLive(currentUserId)
                .catch { e -> _uiState.update { it.copy(errorMessage = e.message, isLoading = false) } }
                .collect { homeFeed ->
                    _uiState.update {
                        it.copy(
                            allReviews = homeFeed.allReviews,
                            followedReviews = homeFeed.followedReviews,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
        }
    }
}
