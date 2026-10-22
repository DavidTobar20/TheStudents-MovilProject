package com.example.thestudents.data

data class HomeFeed(
    val allReviews: List<Review> = emptyList(),
    val followedReviews: List<Review> = emptyList()
)
