package com.example.thestudents.ui.screens.profile

import com.example.thestudents.data.Review
import com.example.thestudents.data.Student
import com.example.thestudents.ui.screens.profile.components.ProfileTab

data class ProfileState(
    val student: Student? = null,
    val reviews: List<Review> = emptyList(),
    val selectedTab: ProfileTab = ProfileTab.RECEIVED,
    val isLoading: Boolean = false
)
