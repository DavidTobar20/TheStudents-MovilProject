package com.example.thestudents.model

import androidx.compose.ui.graphics.Color

data class Student(
    val id: String,
    val name: String,
    val email: String,
    val program: String,
    val semester: Int,
    val bio: String,
    val rating: Float,
    val reviewsCount: Int,
    val initials: String,
    val profileColor: Color,
    val period: String = "",
    val profileImageRes: Int? = null // Atributo para la imagen de perfil
)