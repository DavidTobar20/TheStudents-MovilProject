package com.example.thestudents.data

data class UserProfile(
    val student: Student,
    val receivedReviews: List<Review>,
    val createdReviews: List<Review>
)
