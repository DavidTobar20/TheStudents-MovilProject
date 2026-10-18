package com.example.thestudents.ui.screens.studentDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.local.localStudentProvider
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
class StudentDetailViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(StudentDetailState())
    val uiState: StateFlow<StudentDetailState> = _uiState.asStateFlow()

    fun getStudentById(id: String) {
        if (_uiState.value.userProfile?.student?.id == id && !_uiState.value.isLoading) return

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {

            val result = userRepository.getUserProfile(id)

            if (result.isSuccess) {
                val profile = result.getOrNull()

                val followers = localStudentProvider.getFollowersCount(id)
                val following = localStudentProvider.getFollowingCount(id)
                val totalReviews = (profile?.receivedReviews?.size ?: 0) + (profile?.createdReviews?.size ?: 0)

                _uiState.update {
                    it.copy(
                        userProfile = profile,
                        followersCount = followers,
                        followingCount = following,
                        reviewsCount = totalReviews,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } else {
                val error = result.exceptionOrNull()
                Log.d("StudentDetailDebug", "Error al obtener perfil: ${error?.message}")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error?.message ?: "Error al cargar el perfil del estudiante"
                    )
                }
            }
        }
    }

    fun onTabSelected(tab: ProfileTab) {
        _uiState.update { state ->
            state.copy(selectedTab = tab)
        }
    }
}
