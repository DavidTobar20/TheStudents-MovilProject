package com.example.thestudents.ui.screens.commentsReview

import com.example.thestudents.data.Review
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localStudentProvider

data class CommentsReviewState(
    val commentInputText: String = "",
    val isLiked: Boolean = false,
    val isDisliked: Boolean = false,
    val likedComments: Set<Int> = emptySet(),
    val dislikedComments: Set<Int> = emptySet(),
    val commentator: Student = localStudentProvider.currentUser,
    // La resena se carga desde el backend con el id que llega por la ruta; ya no hay una fija.
    val review: Review? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
