package com.example.thestudents.ui.screens.followList

import com.example.thestudents.data.Student

data class FollowListState(
    val title: String = "",
    val currentUserId: String = "",
    val students: List<Student> = emptyList(),
    val followedStudentIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val emptyMessage: String = "No se encontraron estudiantes."
)
