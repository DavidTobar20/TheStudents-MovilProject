package com.example.thestudents.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
): ViewModel() {

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
        loadReviews()
    }

    fun loadReviews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = reviewRepository.getAllReviewsExcludingStudentId("1")
            result.onSuccess { reviews ->
                _uiState.update {
                    it.copy(
                        followedReviews = reviews,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Error al cargar las reseñas"
                    )
                }
            }
        }
    }

}