package com.example.thestudents.ui.screens.commentsReview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.local.localStudentProvider
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
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CommentsReviewState())
    val uiState : StateFlow<CommentsReviewState> = _uiState

    fun getReviewById(id: String, forceReload: Boolean = false) {
        if (!forceReload && _uiState.value.review?.id == id) return

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                isLiked = false,
                isDisliked = false,
                likedComments = emptySet(),
                dislikedComments = emptySet()
            )
        }
        viewModelScope.launch {
            val result = reviewRepository.getReviewById(id)
            _uiState.update { state ->
                result.fold(
                    onSuccess = { review ->
                        state.copy(review = review, isLoading = false, errorMessage = null)
                    },
                    onFailure = { error ->
                        state.copy(
                            review = null,
                            isLoading = false,
                            errorMessage = error.message ?: "Error al cargar la reseña"
                        )
                    }
                )
            }
        }
    }

    fun getCommentator() {
        viewModelScope.launch {
            val currentUserId = authRepository.currentUser?.uid
            val authPhoto = authRepository.currentUser?.photoUrl?.toString()
            if (currentUserId != null) {
                val profileResult = userRepository.getUserProfile(currentUserId)
                val student = profileResult.getOrNull()?.student
                if (student != null) {
                    val photo = if (!authPhoto.isNullOrBlank()) authPhoto else student.profileImage
                    _uiState.update { it.copy(commentator = student.copy(profileImage = photo)) }
                    return@launch
                }
            }
            val fallback = localStudentProvider.currentUser
            val photo = if (!authPhoto.isNullOrBlank()) authPhoto else fallback.profileImage
            _uiState.update { it.copy(commentator = fallback.copy(profileImage = photo)) }
        }
    }

    fun sendComment(reviewId: String) {
        val text = _uiState.value.commentInputText.trim()
        if (text.isBlank()) return

        viewModelScope.launch {
            val result = reviewRepository.addComment(reviewId, text)
            if (result.isSuccess) {
                _uiState.update { it.copy(commentInputText = "") }
                getReviewById(reviewId, forceReload = true)
            }
        }
    }

    fun updateCommentInputText(text: String) {
        _uiState.update { it.copy(commentInputText = text) }
    }

    fun updateIsLiked() {
        _uiState.update { state ->
            val review = state.review ?: return@update state
            if (state.isDisliked) {
                val nuevaResena = review.copy(
                    disLikes = review.disLikes - 1,
                    likes = review.likes + 1
                )
                state.copy(
                    isLiked = !state.isLiked,
                    isDisliked = false,
                    review = nuevaResena
                )
            } else{
                if(state.isLiked) {
                    val nuevaResena = review.copy(
                        likes = review.likes - 1,
                    )
                    state.copy(
                        isLiked = false,
                        review = nuevaResena
                    )
                } else{
                    val nuevaResena = review.copy(
                        likes = review.likes + 1,
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
            val review = state.review ?: return@update state
            if (state.isLiked) {
                val nuevaResena = review.copy(
                    disLikes = review.disLikes + 1,
                    likes = review.likes - 1
                )
                state.copy(
                    isDisliked = !state.isDisliked,
                    isLiked = false,
                    review = nuevaResena
                )
            } else{
                if(state.isDisliked) {
                    val nuevaResena = review.copy(
                        disLikes = review.disLikes - 1,
                    )
                    state.copy(
                        isDisliked = false,
                        review = nuevaResena
                    )
                } else{
                    val nuevaResena = review.copy(
                        disLikes = review.disLikes + 1,
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