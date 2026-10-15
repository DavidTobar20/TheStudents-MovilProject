package com.example.thestudents.ui.screens.updateReview

import com.example.thestudents.data.Student

data class UpdateReviewState(
    val reviewId: String = "",
    val student: Student? = null,
    val className: String = "",
    val period: String = "",
    val rating: Int = 0,
    val review: String = "",
    val isAnonymous: Boolean = false,
    val navigateToProfile: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
