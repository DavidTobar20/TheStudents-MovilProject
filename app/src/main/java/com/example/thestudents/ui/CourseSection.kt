package com.example.thestudents.ui

import androidx.compose.ui.graphics.vector.ImageVector

data class CourseSection(
    val title: String,
    val icon: ImageVector,
    val students: List<Student>
)
