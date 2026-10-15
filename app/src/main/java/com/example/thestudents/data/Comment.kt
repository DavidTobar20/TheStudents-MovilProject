package com.example.thestudents.data

data class Comment(
    val commentator: Student,
    val content: String,
    val likes: Int,
    val disLikes: Int,
    val createdAt: String
)