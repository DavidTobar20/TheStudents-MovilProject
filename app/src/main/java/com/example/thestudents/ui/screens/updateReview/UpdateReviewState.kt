package com.example.thestudents.ui.screens.updateReview

import com.example.thestudents.data.Review

data class UpdateReviewState(
    val reviewId: String = "",
    val review: Review? = null,
    val rating: Int = 0,
    val reviewContent: String = "",
    val isAnonymous: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
