package com.example.thestudents.ui.screens.writeReview

import com.example.thestudents.data.Student

data class WriteReviewState(
    val student: Student? = null,
    val rating: Int = 0,
    val review: String = "",
    val isAnonymous: Boolean = false
)
