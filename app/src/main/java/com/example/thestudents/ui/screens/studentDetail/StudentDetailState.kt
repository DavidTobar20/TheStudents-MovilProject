package com.example.thestudents.ui.screens.studentDetail

import com.example.thestudents.data.Review
import com.example.thestudents.data.Student
import com.example.thestudents.data.UserProfile
import com.example.thestudents.ui.screens.profile.components.ProfileTab

data class StudentDetailState(
    val userProfile: UserProfile? = null,
    val selectedTab: ProfileTab = ProfileTab.RECEIVED,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val student: Student?
        get() = userProfile?.student

    val reviews: List<Review>
        get() = when (selectedTab) {
            ProfileTab.RECEIVED -> userProfile?.receivedReviews.orEmpty()
            ProfileTab.WRITTEN -> userProfile?.createdReviews.orEmpty()
        }

    val reviewsCount: Int
        get() = (userProfile?.receivedReviews?.size ?: 0) + (userProfile?.createdReviews?.size ?: 0)
}
