package com.example.thestudents.ui.screens.writeReview

import com.example.thestudents.data.Student

data class WriteReviewState(
    val student: Student? = null,
    val className: String = "",
    val period: String = "",
    val rating: Int = 0,
    val review: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val navigateToHome: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false
)
