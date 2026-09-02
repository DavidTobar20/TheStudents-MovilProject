package com.example.thestudents.ui.screens.commentsReview

import com.example.thestudents.data.Review
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.data.local.localStudentProvider

data class CommentsReviewState(
    val commentInputText: String = "",
    val isLiked: Boolean = false,
    val isDisliked: Boolean = false,
    val likedComments: Set<Int> = emptySet(),
    val dislikedComments: Set<Int> = emptySet(),
    val commentator: Student = localStudentProvider.currentUser,
    val review: Review = localReviewsProvider.allReviews[1]
)
