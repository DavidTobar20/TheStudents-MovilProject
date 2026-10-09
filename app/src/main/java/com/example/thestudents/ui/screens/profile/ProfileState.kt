package com.example.thestudents.ui.screens.profile

import com.example.thestudents.data.Review
import com.example.thestudents.data.Student
import com.example.thestudents.ui.screens.profile.components.ProfileTab

data class ProfileState(
    val student: Student? = null,
    val email: String = "",
    val reviews: List<Review> = emptyList(),
    val receivedReviews: List<Review> = emptyList(),
    val writtenReviews: List<Review> = emptyList(),
    val selectedTab: ProfileTab = ProfileTab.WRITTEN,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val reviewsCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
