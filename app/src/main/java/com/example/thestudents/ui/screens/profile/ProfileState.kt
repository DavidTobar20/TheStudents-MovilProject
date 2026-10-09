package com.example.thestudents.ui.screens.profile

import com.example.thestudents.data.UserProfile
import com.example.thestudents.ui.screens.profile.components.ProfileTab

data class ProfileState(
    val userProfile: UserProfile? = null,
    val email: String = "",
    val selectedTab: ProfileTab = ProfileTab.WRITTEN,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val reviewsCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
