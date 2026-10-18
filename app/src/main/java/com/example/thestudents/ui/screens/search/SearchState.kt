package com.example.thestudents.ui.screens.search

import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localStudentProvider

data class SearchState(
    val students: List<Student> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
