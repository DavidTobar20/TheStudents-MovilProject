package com.example.thestudents.ui.screens.writeReview

import com.example.thestudents.data.Student

data class WriteReviewState(
    val student: Student? = null,
    val rating: Int = 0,
    val review: String = "",
    val isAnonymous: Boolean = false,
    val isEditMode: Boolean = false,
    val reviewId: String? = null,
    val courseInfo: String = "",
    val isLoading: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null
)
