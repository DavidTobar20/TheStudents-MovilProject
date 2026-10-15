package com.example.thestudents.data

import androidx.compose.ui.graphics.Color


data class Student(
    val id: String,
    val name: String,
    val username: String,
    val program: String,
    val semester: Int,
    val bio: String,
    val rating: Float,
    val reviewsCount: Int,
    val initials: String,
    val profileColor: Color,
    val profileImage: String? = null,
    val followersCount: Int = 0,
    val followingCount: Int = 0
)
