package com.example.thestudents.ui.screens.commentsReview

import androidx.lifecycle.ViewModel
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.data.local.localStudentProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CommentsReviewViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CommentsReviewState())
    val uiState : StateFlow<CommentsReviewState> = _uiState

    fun getReviewById(id: String) {
        _uiState.update { it.copy(review = localReviewsProvider.getReviewById(id) ?: localReviewsProvider.allReviews[0]) }
    }

    fun getCommentator() {
        _uiState.update { it.copy(commentator = localStudentProvider.currentUser) }
    }

    fun updateCommentInputText(text: String) {
        _uiState.update { it.copy(commentInputText = text) }
    }

    fun updateIsLiked() {
        _uiState.update { state ->
            if (state.isDisliked) {
                val nuevaResena = state.review.copy(
                    disLikes = state.review.disLikes - 1,
                    likes = state.review.likes + 1
                )
                state.copy(
                    isLiked = !state.isLiked,
                    isDisliked = false,
                    review = nuevaResena
                )
            } else{
                if(state.isLiked) {
                    val nuevaResena = state.review.copy(
                        likes = state.review.likes - 1,
                    )
                    state.copy(
                        isLiked = false,
                        review = nuevaResena
                    )
                } else{
                    val nuevaResena = state.review.copy(
                        likes = state.review.likes + 1,
                    )
                    state.copy(
                        isLiked = true,
                        review = nuevaResena
                    )
                }
            }
        }
    }

    fun updateIsDisliked() {
        _uiState.update { state ->
            if (state.isLiked) {
                val nuevaResena = state.review.copy(
                    disLikes = state.review.disLikes + 1,
                    likes = state.review.likes - 1
                )
                state.copy(
                    isDisliked = !state.isDisliked,
                    isLiked = false,
                    review = nuevaResena
                )
            } else{
                if(state.isDisliked) {
                    val nuevaResena = state.review.copy(
                        disLikes = state.review.disLikes - 1,
                    )
                    state.copy(
                        isDisliked = false,
                        review = nuevaResena
                    )
                } else{
                    val nuevaResena = state.review.copy(
                        disLikes = state.review.disLikes + 1,
                    )
                    state.copy(
                        isDisliked = true,
                        review = nuevaResena
                    )
                }
            }
        }
    }

    private fun Set<Int>.toggle(value: Int): Set<Int> =
        if (value in this) this - value else this + value

    fun updateLikedComments(index: Int) {
        _uiState.update { state ->
            val newLikedComments = state.likedComments.toggle(index)
            val newDislikedComments = if (index in newLikedComments) {
                state.dislikedComments - index
            } else {
                state.dislikedComments
            }
            state.copy(
                likedComments = newLikedComments,
                dislikedComments = newDislikedComments
            )
        }
    }

    fun updateDislikedComments(index: Int) {
        _uiState.update { state ->
            val newDislikedComments = state.dislikedComments.toggle(index)
            val newLikedComments = if (index in newDislikedComments) {
                state.likedComments - index
            } else {
                state.likedComments
            }
            state.copy(
                likedComments = newLikedComments,
                dislikedComments = newDislikedComments
            )
        }
    }

}