package com.example.thestudents.ui.screens.map

import com.example.thestudents.data.Review
import com.google.android.gms.maps.model.LatLng

data class ReviewsMapState(
    val recentReviews: List<Review> = emptyList(),
    val selectedReview: Review? = null,
    val overlappingReviews: List<Review> = emptyList(),
    val searchQuery: String = "",
    val searchedLocation: LatLng? = null,
    val searchedAddressTitle: String = "",
    val userLocation: LatLng? = null,
    val isDarkMap: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
