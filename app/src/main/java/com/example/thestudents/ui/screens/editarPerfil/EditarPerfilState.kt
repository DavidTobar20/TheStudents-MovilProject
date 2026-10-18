package com.example.thestudents.ui.screens.editarPerfil

import com.example.thestudents.data.Student

data class EditarPerfilState(
    val student: Student? = null,
    val name: String = "",
    val username: String = "",
    val program: String = "",
    val semester: String = "",
    val bio: String = "",
    val showReviews: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
