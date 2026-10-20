package com.example.thestudents.ui.screens.studentDetail

import com.example.thestudents.data.UserProfile
import com.example.thestudents.ui.screens.profile.components.ProfileTab

data class StudentDetailState(
    val userProfile: UserProfile? = null,
    val selectedTab: ProfileTab = ProfileTab.RECEIVED,
    val reviewsCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
