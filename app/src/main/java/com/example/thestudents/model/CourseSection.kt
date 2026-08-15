package com.example.thestudents.model

import androidx.compose.ui.graphics.vector.ImageVector

data class CourseSection(
    val title: String,
    val icon: ImageVector,
    val students: List<Student>
)
