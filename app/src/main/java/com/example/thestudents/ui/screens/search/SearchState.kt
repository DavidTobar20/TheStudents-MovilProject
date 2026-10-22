package com.example.thestudents.ui.screens.search

import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localStudentProvider

data class SearchState(
    val students: List<Student> = emptyList(),
    val query: String = "",
    val followedStudentIds: Set<String> = emptySet(), // a quienes sigo, para mostrar SEGUIR o SIGUIENDO
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
