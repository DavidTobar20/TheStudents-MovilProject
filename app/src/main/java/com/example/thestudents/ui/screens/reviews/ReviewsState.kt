package com.example.thestudents.ui.screens.reviews

import com.example.thestudents.data.CourseSection

data class ReviewsState(
    val sections: List<CourseSection> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
