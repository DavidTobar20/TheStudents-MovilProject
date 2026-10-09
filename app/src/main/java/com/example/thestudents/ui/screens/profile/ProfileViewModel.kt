package com.example.thestudents.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.data.repository.AuthRepository
import com.example.thestudents.data.repository.ReviewRepository
import com.example.thestudents.ui.screens.profile.components.ProfileTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val reviewRepository: ReviewRepository
): ViewModel() {

    private val defaultPhotoUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRCq6qha5YiJYI4ZIs3Sug9cpBKz23j-X5kWIMC6qU0jA&s=10"

    private val _uiState = MutableStateFlow(
        ProfileState(
            email = authRepository.currentUser?.email ?: ""
        )
    )
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val baseStudent = localStudentProvider.currentUser
            val currentUserId = baseStudent.id

            // Consultar reseñas escritas y recibidas desde el backend vía Retrofit
            val writtenResult = reviewRepository.getReviewsByReviewerId(currentUserId)
            val receivedResult = reviewRepository.getReviewsByReviewedStudentId(currentUserId)

            val written = writtenResult.getOrDefault(emptyList())
            val received = receivedResult.getOrDefault(emptyList())

            val authPhoto = authRepository.currentUser?.photoUrl?.toString()
            val photoUrl = if (authPhoto.isNullOrEmpty()) defaultPhotoUrl else authPhoto
            val student = baseStudent.copy(profileImage = photoUrl)

            val followersCount = localStudentProvider.getFollowersCount(currentUserId)
            val followingCount = localStudentProvider.getFollowingCount(currentUserId)
            val totalReviewsCount = written.size + received.size

            val currentTab = _uiState.value.selectedTab
            val activeReviews = if (currentTab == ProfileTab.RECEIVED) received else written

            val error = if (writtenResult.isFailure && receivedResult.isFailure) {
                writtenResult.exceptionOrNull()?.message ?: "Error al cargar reseñas"
            } else null

            _uiState.update {
                it.copy(
                    student = student,
                    writtenReviews = written,
                    receivedReviews = received,
                    reviews = activeReviews,
                    followersCount = followersCount,
                    followingCount = followingCount,
                    reviewsCount = totalReviewsCount,
                    isLoading = false,
                    errorMessage = error
                )
            }
        }
    }

    fun logout() {
        authRepository.signOut()
    }

    fun onTabSelected(tab: ProfileTab) {
        _uiState.update { state ->
            val activeReviews = if (tab == ProfileTab.RECEIVED) state.receivedReviews else state.writtenReviews
            state.copy(
                selectedTab = tab,
                reviews = activeReviews
            )
        }
    }

    fun deleteReview(reviewId: String) {
        viewModelScope.launch {
            val result = reviewRepository.deleteReview(reviewId)
            if (result.isSuccess) {
                _uiState.update { state ->
                    val updatedWritten = state.writtenReviews.filter { it.id != reviewId }
                    val activeReviews = if (state.selectedTab == ProfileTab.WRITTEN) updatedWritten else state.receivedReviews
                    state.copy(
                        writtenReviews = updatedWritten,
                        reviews = activeReviews,
                        reviewsCount = updatedWritten.size + state.receivedReviews.size
                    )
                }
            } else {
                _uiState.update {
                    it.copy(errorMessage = "Error al eliminar la reseña")
                }
            }
        }
    }
}