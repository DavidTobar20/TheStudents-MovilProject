package com.example.thestudents.ui.screens.reviews

import com.example.thestudents.data.CourseSection

data class ReviewsState(
    val sections: List<CourseSection> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showEnrollDialog: Boolean = false,
    val catalogoMaterias: Map<String, List<String>> = emptyMap(),
    val materiasYaInscritas: Set<String> = emptySet(),
    val selectedMateria: String = "",
    val selectedPeriodo: String = "",
    val isEnrolling: Boolean = false,
    val enrollErrorMessage: String? = null
)
