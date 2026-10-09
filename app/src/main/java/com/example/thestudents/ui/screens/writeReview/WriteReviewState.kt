package com.example.thestudents.ui.screens.writeReview

import com.example.thestudents.data.Student

data class WriteReviewState(
    val student: Student? = null,
    val className: String = "",
    val period: String = "",
    val rating: Int = 0,
    val review: String = "",
    val isAnonymous: Boolean = false,
    val navigateToHome: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false
)
