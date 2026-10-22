package com.example.thestudents.ui.screens.commentsReview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.Comment
import com.example.thestudents.data.Review
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
    // Reseña o comentarios que tienen un like/dislike guardandose en Firestore.
    // Mientras tanto se ignoran nuevos toques para no mandar transacciones encima de otras.
    private var reviewPending = false
    private val pendingComments = mutableSetOf<String>()

    private val _uiState = MutableStateFlow(CommentsReviewState())
    val uiState : StateFlow<CommentsReviewState> = _uiState

    fun getReviewById(id: String) {
        // Si ya esta cargada esta misma resena (por ejemplo al rotar la pantalla), no se pide otra vez.
        if (_uiState.value.review?.id == id) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    currentUserId = authRepository.currentUser?.uid ?: ""
                )
            }
            val result = reviewRepository.getReviewById(id)

            if(result.isSuccess){
                val review = result.getOrNull()

                // Comentarios de la reseña (subcoleccion reviews/{id}/comments)
                val comments = reviewRepository.getComments(id).getOrNull() ?: emptyList()

                _uiState.update {
                    it.copy(review = review?.copy(comments = comments, commentsCount = comments.size), isLoading = false, errorMessage = null)
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

    fun sendComment() {
        val reviewId = _uiState.value.review?.id ?: return
        val contenido = _uiState.value.commentInputText.trim()
        if (contenido.isEmpty()) return

        viewModelScope.launch {
            val result = reviewRepository.createComment(reviewId, contenido)
            if (result.isSuccess) {
                val comment = result.getOrNull()
                val review = _uiState.value.review
                if (comment != null && review != null) {
                    // Se agrega al final de la lista (debajo de la reseña) y se limpia el campo
                    _uiState.value = _uiState.value.copy(
                        review = review.copy(
                            comments = review.comments + comment,
                            commentsCount = review.commentsCount + 1
                        ),
                        commentInputText = ""
                    )
                }
            } else {
                _uiState.update { it.copy(errorMessage = result.exceptionOrNull()?.message) }
            }
        }
    }

    fun updateIsLiked() {
        val review = _uiState.value.review ?: return
        val studentId = authRepository.currentUser?.uid ?: return
        if (reviewPending) return
        reviewPending = true

        // 1. Se cambia la pantalla de una vez, sin esperar a Firestore
        _uiState.value = _uiState.value.copy(
            review = review.copy(
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
            reviewPending = false
            if (result.isSuccess) {
                // Firestore lo guardo: la pantalla ya estaba bien, no hay nada que hacer
            } else {
                // Fallo: se vuelve a como estaba
                restoreReviewReactions(review)
            }
        }
    }

    fun updateIsDisliked() {
        val review = _uiState.value.review ?: return
        val studentId = authRepository.currentUser?.uid ?: return
        if (reviewPending) return
        reviewPending = true

        // 1. Se cambia la pantalla de una vez, sin esperar a Firestore
        _uiState.value = _uiState.value.copy(
            review = review.copy(
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
            reviewPending = false
            if (result.isSuccess) {
                // Firestore lo guardo: la pantalla ya estaba bien, no hay nada que hacer
            } else {
                // Fallo: se vuelve a como estaba
                restoreReviewReactions(review)
            }
        }
    }

    // Si Firestore fallo, devuelve los likes/dislikes de la reseña a como estaban
    // (sin tocar los comentarios, por si se agrego alguno mientras tanto)
    private fun restoreReviewReactions(previous: Review) {
        val current = _uiState.value.review ?: return
        _uiState.value = _uiState.value.copy(
            review = current.copy(
                isLiked = previous.isLiked,
                likesCount = previous.likesCount,
                isDisliked = previous.isDisliked,
                disLikes = previous.disLikes
            )
        )
    }

    // Reemplaza un comentario de la lista (por id) sin tocar los demas
    private fun replaceComment(updated: Comment) {
        val review = _uiState.value.review ?: return
        _uiState.value = _uiState.value.copy(
            review = review.copy(
                comments = review.comments.map { comment ->
                    if (comment.id == updated.id) updated else comment
                }
            )
        )
    }

    fun updateCommentLike(commentId: String) {
        val reviewId = _uiState.value.review?.id ?: return
        val studentId = authRepository.currentUser?.uid ?: return
        val comment = _uiState.value.review?.comments?.find { it.id == commentId } ?: return
        if (commentId in pendingComments) return
        pendingComments.add(commentId)

        // 1. Se cambia la pantalla de una vez, sin esperar a Firestore
        replaceComment(
            comment.copy(
                isLiked = !comment.isLiked,
                likes = if (comment.isLiked) comment.likes - 1 else comment.likes + 1,
                // si tenia dislike, la transaccion de Firestore tambien se lo quita
                isDisliked = false,
                disLikes = if (comment.isDisliked) comment.disLikes - 1 else comment.disLikes
            )
        )

        // 2. Se guarda en Firestore por detras; si falla, se vuelve a como estaba
        viewModelScope.launch {
            val result = reviewRepository.sendOrDeleteCommentLike(reviewId, commentId, studentId)
            pendingComments.remove(commentId)
            if (result.isSuccess) {
                // Firestore lo guardo: la pantalla ya estaba bien, no hay nada que hacer
            } else {
                // Fallo: se vuelve a como estaba
                replaceComment(comment)
            }
        }
    }

    fun updateCommentDislike(commentId: String) {
        val reviewId = _uiState.value.review?.id ?: return
        val studentId = authRepository.currentUser?.uid ?: return
        val comment = _uiState.value.review?.comments?.find { it.id == commentId } ?: return
        if (commentId in pendingComments) return
        pendingComments.add(commentId)

        // 1. Se cambia la pantalla de una vez, sin esperar a Firestore
        replaceComment(
            comment.copy(
                isDisliked = !comment.isDisliked,
                disLikes = if (comment.isDisliked) comment.disLikes - 1 else comment.disLikes + 1,
                // si tenia like, la transaccion de Firestore tambien se lo quita
                isLiked = false,
                likes = if (comment.isLiked) comment.likes - 1 else comment.likes
            )
        )

        // 2. Se guarda en Firestore por detras; si falla, se vuelve a como estaba
        viewModelScope.launch {
            val result = reviewRepository.sendOrDeleteCommentDislike(reviewId, commentId, studentId)
            pendingComments.remove(commentId)
            if (result.isSuccess) {
                // Firestore lo guardo: la pantalla ya estaba bien, no hay nada que hacer
            } else {
                // Fallo: se vuelve a como estaba
                replaceComment(comment)
            }
        }
    }

    fun editComment(commentId: String, nuevoContenido: String) {
        val reviewId = _uiState.value.review?.id ?: return
        val contenido = nuevoContenido.trim()
        if (contenido.isEmpty()) return

        viewModelScope.launch {
            val result = reviewRepository.updateComment(reviewId, commentId, contenido)
            if (result.isSuccess) {
                val review = _uiState.value.review
                if (review != null) {
                    _uiState.value = _uiState.value.copy(
                        review = review.copy(
                            comments = review.comments.map { comment ->
                                if (comment.id == commentId) comment.copy(content = contenido) else comment
                            }
                        )
                    )
                }
            }
        }
    }

    fun deleteComment(commentId: String) {
        val reviewId = _uiState.value.review?.id ?: return

        viewModelScope.launch {
            val result = reviewRepository.deleteComment(reviewId, commentId)
            if (result.isSuccess) {
                val review = _uiState.value.review
                if (review != null) {
                    _uiState.value = _uiState.value.copy(
                        review = review.copy(
                            comments = review.comments.filter { it.id != commentId },
                            commentsCount = review.commentsCount - 1
                        )
                    )
                }
            }
        }
    }

}
