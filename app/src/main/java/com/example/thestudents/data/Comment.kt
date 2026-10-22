package com.example.thestudents.data

data class Comment(
    val id: String = "", // id del documento en reviews/{reviewId}/comments
    val commentator: Student,
    val content: String,
    val likes: Int,
    val disLikes: Int,
    val createdAt: String,
    val isLiked: Boolean = false,    // si el usuario actual le dio like
    val isDisliked: Boolean = false  // si el usuario actual le dio dislike
)