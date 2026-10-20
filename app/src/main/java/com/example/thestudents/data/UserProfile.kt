package com.example.thestudents.data

data class UserProfile(
    val student: Student,
    val receivedReviews: List<Review>,
    val createdReviews: List<Review>,
    val followed: Boolean = false // si el usuario actual ya sigue a este estudiante
)
