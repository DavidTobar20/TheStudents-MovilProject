package com.example.thestudents.ui.screens.home

import com.example.thestudents.data.Review

data class HomeState(
    val followedReviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
