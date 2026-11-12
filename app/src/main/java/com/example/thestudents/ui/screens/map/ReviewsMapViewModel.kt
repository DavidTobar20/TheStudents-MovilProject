package com.example.thestudents.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.Review
import com.example.thestudents.data.repository.AuthRepository
import com.example.thestudents.data.repository.ReviewRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewsMapViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewsMapState())
    val uiState: StateFlow<ReviewsMapState> = _uiState.asStateFlow()

    fun loadRecentReviews() {
        val currentUserId = authRepository.currentUser?.uid ?: ""
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            reviewRepository.getRecentMapReviewsLive(currentUserId)
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
                .collect { reviews ->
                    _uiState.update { currentState ->
                        val updatedSelected = currentState.selectedReview?.let { selected ->
                            reviews.find { it.id == selected.id }
                        }
                        currentState.copy(
                            recentReviews = reviews,
                            selectedReview = updatedSelected,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    fun selectReview(review: Review?) {
        _uiState.update { it.copy(selectedReview = review) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun updateSearchedLocation(location: LatLng?, title: String) {
        _uiState.update {
            it.copy(
                searchedLocation = location,
                searchedAddressTitle = title
            )
        }
    }

    fun updateUserLocation(latitude: Double, longitude: Double) {
        _uiState.update {
            it.copy(userLocation = LatLng(latitude, longitude))
        }
    }

    fun updateAmbientLight(lux: Float) {
        val shouldBeDark = lux < 2000f
        if (_uiState.value.isDarkMap != shouldBeDark) {
            _uiState.update { it.copy(isDarkMap = shouldBeDark) }
        }
    }
}
