package com.example.thestudents.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.data.repository.AuthRepository
import com.example.thestudents.ui.screens.profile.components.ProfileTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val defaultPhotoUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRCq6qha5YiJYI4ZIs3Sug9cpBKz23j-X5kWIMC6qU0jA&s=10"

    private val _uiState = MutableStateFlow(
        ProfileState(
            email = authRepository.currentUser?.email ?: "",
            profileImageUrl = authRepository.currentUser?.photoUrl?.toString().let { 
                if (it.isNullOrEmpty()) defaultPhotoUrl else it 
            }
        )
    )
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        _uiState.update { it.copy(isLoading = true) }
        val student = localStudentProvider.currentUser
        val reviews = localReviewsProvider.getReviewsForStudent(student.id)
        val authPhoto = authRepository.currentUser?.photoUrl?.toString()
        val defaultPhotoUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRCq6qha5YiJYI4ZIs3Sug9cpBKz23j-X5kWIMC6qU0jA&s=10"
        _uiState.update {
            it.copy(
                student = student,
                reviews = reviews,
                profileImageUrl = if (authPhoto.isNullOrEmpty()) defaultPhotoUrl else authPhoto,
                isLoading = false
            )
        }
    }

    fun logout() {
        authRepository.signOut()
    }

    fun onTabSelected(tab: ProfileTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
}