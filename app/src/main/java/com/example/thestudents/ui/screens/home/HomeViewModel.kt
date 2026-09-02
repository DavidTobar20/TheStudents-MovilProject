package com.example.thestudents.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.data.local.localStudentProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<HomeState>(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    fun reviewIsLiked(index: Int): Boolean {
        return index in _uiState.value.likedReviews
    }

    fun reviewIsDisliked(index: Int): Boolean {
        return index in _uiState.value.dislikedReviews
    }

    private fun Set<Int>.toggle(value: Int): Set<Int> =
        if (value in this) this - value else this + value

    fun updateLikedReviews(index: Int) {
        _uiState.update { state ->
            val newLikedReviews = state.likedReviews.toggle(index)
            val newDislikedReviews = if (index in newLikedReviews) {
                state.dislikedReviews - index
            } else {
                state.dislikedReviews
            }
            state.copy(
                likedReviews = newLikedReviews,
                dislikedReviews = newDislikedReviews
            )
        }
    }

    fun updateDislikedReviews(index: Int) {
        _uiState.update { state ->
            val newDislikedReviews = state.dislikedReviews.toggle(index)
            val newLikedReviews = if (index in newDislikedReviews) {
                state.likedReviews - index
            } else {
                state.likedReviews
            }
            state.copy(
                likedReviews = newLikedReviews,
                dislikedReviews = newDislikedReviews
            )
        }
    }

    init {
        _uiState.update {
            it.copy(
                followingIds = localStudentProvider.followingIds,
                followedReviews = localReviewsProvider.getReviewsByFollowed(localStudentProvider.followingIds)
            )
        }
    }

}