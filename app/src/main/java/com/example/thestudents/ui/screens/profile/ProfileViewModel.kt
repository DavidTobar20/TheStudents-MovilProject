package com.example.thestudents.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.data.repository.AuthRepository
import com.example.thestudents.data.repository.ReviewRepository
import com.example.thestudents.data.repository.UserRepository
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
    private val userRepository: UserRepository,
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

            val result = userRepository.getUserProfile(currentUserId)

            if (result.isSuccess) {
                val profile = result.getOrNull()
                val currentEmail = authRepository.currentUser?.email ?: ""
                val authPhoto = authRepository.currentUser?.photoUrl?.toString()
                val photoUrl = if (authPhoto.isNullOrEmpty()) defaultPhotoUrl else authPhoto

                val updatedProfile = profile?.let {
                    it.copy(student = it.student.copy(profileImage = photoUrl))
                }

                val followers = localStudentProvider.getFollowersCount(currentUserId)
                val following = localStudentProvider.getFollowingCount(currentUserId)
                val totalReviews = (profile?.receivedReviews?.size ?: 0) + (profile?.createdReviews?.size ?: 0)

                _uiState.update {
                    it.copy(
                        userProfile = updatedProfile,
                        email = currentEmail,
                        followersCount = followers,
                        followingCount = following,
                        reviewsCount = totalReviews,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } else {
                val error = result.exceptionOrNull()
                val currentEmail = authRepository.currentUser?.email ?: ""
                _uiState.update {
                    it.copy(
                        email = currentEmail,
                        isLoading = false,
                        errorMessage = error?.message ?: "Error al cargar el perfil"
                    )
                }
            }
        }
    }

    fun logout() {
        authRepository.signOut()
    }

    fun onTabSelected(tab: ProfileTab) {
        _uiState.update { state ->
            state.copy(selectedTab = tab)
        }
    }

    fun deleteReview(reviewId: String) {
        viewModelScope.launch {
            val result = reviewRepository.deleteReview(reviewId)
            if (result.isSuccess) {
                _uiState.update { state ->
                    val profile = state.userProfile ?: return@update state
                    val updatedCreated = profile.createdReviews.filter { it.id != reviewId }
                    val updatedReceived = profile.receivedReviews.filter { it.id != reviewId }
                    val updatedProfile = profile.copy(
                        createdReviews = updatedCreated,
                        receivedReviews = updatedReceived
                    )
                    val newReviewsCount = updatedCreated.size + updatedReceived.size

                    state.copy(
                        userProfile = updatedProfile,
                        reviewsCount = newReviewsCount
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
