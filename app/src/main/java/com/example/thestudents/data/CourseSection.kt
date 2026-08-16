package com.example.thestudents.data

import androidx.compose.ui.graphics.vector.ImageVector

data class CourseSection(
    val title: String,
    val icon: ImageVector,
    val students: List<Student>
)
