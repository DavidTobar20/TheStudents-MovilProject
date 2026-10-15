package com.example.thestudents.ui.screens.studentDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.UserProfile
import com.example.thestudents.data.local.localReviewsProvider
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
        val initialStudent = localStudentProvider.students.find { it.id == id }
        if (initialStudent != null && _uiState.value.userProfile == null) {
            val received = localReviewsProvider.allReviews.filter { it.reviewedStudent.id == id }
            val written = localReviewsProvider.allReviews.filter { it.reviewer.id == id }
            _uiState.update {
                it.copy(
                    userProfile = UserProfile(
                        student = initialStudent,
                        receivedReviews = received,
                        createdReviews = written
                    ),
                    isLoading = true,
                    errorMessage = null
                )
            }
        } else {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        }

        viewModelScope.launch {
            val result = userRepository.getUserProfile(id)
            if (result.isSuccess) {
                val profile = result.getOrNull()
                _uiState.update {
                    it.copy(
                        userProfile = profile,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } else {
                if (_uiState.value.userProfile == null) {
                    val fallbackStudent = localStudentProvider.students.find { it.id == id }
                    if (fallbackStudent != null) {
                        val received = localReviewsProvider.allReviews.filter { it.reviewedStudent.id == id }
                        val written = localReviewsProvider.allReviews.filter { it.reviewer.id == id }
                        _uiState.update {
                            it.copy(
                                userProfile = UserProfile(
                                    student = fallbackStudent,
                                    receivedReviews = received,
                                    createdReviews = written
                                ),
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.exceptionOrNull()?.message ?: "Estudiante no encontrado"
                            )
                        }
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun onTabSelected(tab: ProfileTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
}
