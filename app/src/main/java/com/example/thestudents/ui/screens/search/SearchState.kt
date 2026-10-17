package com.example.thestudents.ui.screens.search

import com.example.thestudents.data.Student

data class SearchState(
    val students: List<Student> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
