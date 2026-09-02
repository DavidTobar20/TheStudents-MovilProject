package com.example.thestudents.ui.screens.editarPerfil

import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localStudentProvider

data class EditarPerfilState(
    val student: Student = localStudentProvider.currentUser,
    val name: String = "",
    val username: String = "",
    val bio: String = "",
    val showReviews: Boolean = true,
    val notificationsEnabled: Boolean = true
)
