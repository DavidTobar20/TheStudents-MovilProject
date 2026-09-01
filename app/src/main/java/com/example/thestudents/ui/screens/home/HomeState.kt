package com.example.thestudents.ui.screens.home

import com.example.thestudents.data.Review

data class HomeState(
    val followingIds: Set<String> = emptySet(),
    val followedReviews: List<Review> = emptyList(),
    val likedReviews: Set<Int> = emptySet(),
    val dislikedReviews: Set<Int> = emptySet(),
    val idReview: String = "",
    val idReviewer: String = ""
)
