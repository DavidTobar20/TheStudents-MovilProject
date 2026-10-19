package com.example.thestudents.ui.screens.commentsReview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.repository.AuthRepository
import com.example.thestudents.data.repository.ReviewRepository
import com.example.thestudents.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CommentsReviewState())
    val uiState : StateFlow<CommentsReviewState> = _uiState

    fun getReviewById(id: String) {
        // Si ya esta cargada esta misma resena (por ejemplo al rotar la pantalla), no se pide otra vez.
        if (_uiState.value.review?.id == id) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null,
                likedComments = emptySet(),
                dislikedComments = emptySet()) }
            val result = reviewRepository.getReviewById(id)

            if(result.isSuccess){
                val review = result.getOrNull()
                _uiState.update {
                    it.copy(review = review, isLoading = false, errorMessage = null)
                }
            }else {
                val error = result.exceptionOrNull()
                _uiState.update {
                    it.copy(isLoading = false,errorMessage = error?.message)
                }
            }
        }
    }

    fun getCommentator() {

        viewModelScope.launch {
            val currentUserId = authRepository.currentUser?.uid ?: ""
            val result = userRepository.getUserById(currentUserId)

            if(result.isSuccess){
                val commentator = result.getOrNull()

                _uiState.update {
                    it.copy(commentator = commentator)
                }
            }else {
                val error = result.exceptionOrNull()
                _uiState.update {
                    it.copy(errorMessage = error?.message)
                }
            }
        }
    }

    fun updateCommentInputText(text: String) {
        _uiState.update { it.copy(commentInputText = text) }
    }

    fun updateIsLiked() {
        val reviewId = _uiState.value.review?.id ?: return
        val studentId = authRepository.currentUser?.uid ?: return

        viewModelScope.launch {
            val result = reviewRepository.sendOrDeleteReviewLike(reviewId, studentId)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        review = it.review?.copy(
                            isLiked = !it.review.isLiked,
                            likesCount = if (it.review.isLiked) it.review.likesCount - 1 else it.review.likesCount + 1,
                            // si tenia dislike, Firestore ya se lo quito en la transaccion
                            isDisliked = false,
                            disLikes = if (it.review.isDisliked) it.review.disLikes - 1 else it.review.disLikes
                        )
                    )
                }
            } else {
                _uiState.update { it.copy(errorMessage = result.exceptionOrNull()?.message) }
            }
        }
    }

    fun updateIsDisliked() {
        val reviewId = _uiState.value.review?.id ?: return
        val studentId = authRepository.currentUser?.uid ?: return

        viewModelScope.launch {
            val result = reviewRepository.sendOrDeleteReviewDislike(reviewId, studentId)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        review = it.review?.copy(
                            isDisliked = !it.review.isDisliked,
                            disLikes = if (it.review.isDisliked) it.review.disLikes - 1 else it.review.disLikes + 1,
                            // si tenia like, Firestore ya se lo quito en la transaccion
                            isLiked = false,
                            likesCount = if (it.review.isLiked) it.review.likesCount - 1 else it.review.likesCount
                        )
                    )
                }
            } else {
                _uiState.update { it.copy(errorMessage = result.exceptionOrNull()?.message) }
            }
        }
    }

    private fun Set<Int>.toggle(value: Int): Set<Int> =
        if (value in this) this - value else this + value

    fun updateLikedComments(index: Int) {
        _uiState.update { state ->
            val review = state.review
            if (review != null && index in review.comments.indices) {
                val comments = review.comments.toMutableList()
                val targetComment = comments[index]
                val isCurrentlyLiked = index in state.likedComments
                val isCurrentlyDisliked = index in state.dislikedComments

                val updatedComment = if (isCurrentlyDisliked) {
                    targetComment.copy(
                        disLikes = targetComment.disLikes - 1,
                        likes = targetComment.likes + 1
                    )
                } else {
                    if (isCurrentlyLiked) {
                        targetComment.copy(
                            likes = targetComment.likes - 1
                        )
                    } else {
                        targetComment.copy(
                            likes = targetComment.likes + 1
                        )
                    }
                }

                comments[index] = updatedComment

                val newLikedComments = state.likedComments.toggle(index)
                val newDislikedComments = if (index in newLikedComments) {
                    state.dislikedComments - index
                } else {
                    state.dislikedComments
                }

                state.copy(
                    review = review.copy(comments = comments),
                    likedComments = newLikedComments,
                    dislikedComments = newDislikedComments
                )
            } else {
                state
            }
        }
    }

    fun updateDislikedComments(index: Int) {
        _uiState.update { state ->
            val review = state.review
            if (review != null && index in review.comments.indices) {
                val comments = review.comments.toMutableList()
                val targetComment = comments[index]
                val isCurrentlyLiked = index in state.likedComments
                val isCurrentlyDisliked = index in state.dislikedComments

                val updatedComment = if (isCurrentlyLiked) {
                    targetComment.copy(
                        likes = targetComment.likes - 1,
                        disLikes = targetComment.disLikes + 1
                    )
                } else {
                    if (isCurrentlyDisliked) {
                        targetComment.copy(
                            disLikes = targetComment.disLikes - 1
                        )
                    } else {
                        targetComment.copy(
                            disLikes = targetComment.disLikes + 1
                        )
                    }
                }

                comments[index] = updatedComment

                val newDislikedComments = state.dislikedComments.toggle(index)
                val newLikedComments = if (index in newDislikedComments) {
                    state.dislikedComments - index
                } else {
                    state.likedComments
                }

                state.copy(
                    review = review.copy(comments = comments),
                    likedComments = newLikedComments,
                    dislikedComments = newDislikedComments
                )
            } else {
                state
            }
        }
    }

}
