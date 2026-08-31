package com.example.thestudents.ui.screens.studentDetail

import com.example.thestudents.data.Review
import com.example.thestudents.data.Student

data class StudentDetailState(
    val student: Student? = null,
    val reviews: List<Review> = emptyList()
)
