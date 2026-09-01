package com.example.thestudents.ui.screens.editarPerfil

import com.example.thestudents.R
import com.example.thestudents.data.Student
import com.example.thestudents.ui.theme.Avatar5

data class EditarPerfilState(
    val student: Student = Student(
        id = "1",
        name = "Juan Pablo Mejía",
        username = "@juan.pablo.m",
        program = "Ingeniería de Sistemas",
        semester = 7,
        bio = "Me gusta trabajar en equipo y aprender de proyectos reales. Abierto a grupos de estudio.",
        rating = 4.8f,
        reviewsCount = 21,
        initials = "JP",
        profileColor = Avatar5,
        period = "2024-2",
        profileImage = R.drawable.logosinfondo
    ),
    val name: String = "",
    val username: String = "",
    val bio: String = "",
    val showReviews: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val saveEdit: Boolean = false,
    val navigateBack: Boolean = false
)
