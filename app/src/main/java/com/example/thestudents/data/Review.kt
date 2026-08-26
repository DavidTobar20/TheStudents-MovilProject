package com.example.thestudents.data

data class Review(
    val reviewer: Student,
    val reviewedStudentId: String,
    val nameReviewed: String,
    val classReviewed: String,
    val periodReviewed: String,
    val content: String,
    val time: String,
    val likes: Int,
    val disLikes: Int,
    val rating: String?,
    val comments: List<Comment>
)
