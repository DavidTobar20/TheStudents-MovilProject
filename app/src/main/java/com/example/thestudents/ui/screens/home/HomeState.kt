package com.example.thestudents.ui.screens.home

import com.example.thestudents.data.Review
import com.example.thestudents.ui.screens.home.components.HomeTab

data class HomeState(
    val selectedTab: HomeTab = HomeTab.ALL,
    val allReviews: List<Review> = emptyList(),
    val followedReviews: List<Review> = emptyList(),
    val likedReviews: Set<Int> = emptySet(),
    val dislikedReviews: Set<Int> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val currentReviews: List<Review>
        get() = if (selectedTab == HomeTab.ALL) allReviews else followedReviews
}
