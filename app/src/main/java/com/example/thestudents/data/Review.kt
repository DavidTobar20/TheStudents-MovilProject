package com.example.thestudents.data

data class Review(
    val id: String,
    val reviewer: Student,
    val reviewedStudent: Student, // Ahora incluimos el objeto Student completo
    val classReviewed: String,
    val periodReviewed: String,
    val content: String,
    val time: String,
    val likes: Int,
    val disLikes: Int,
    val rating: String?,
    val comments: List<Comment>
)
