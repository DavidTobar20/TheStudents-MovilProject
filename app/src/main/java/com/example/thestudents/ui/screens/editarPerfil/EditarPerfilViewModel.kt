package com.example.thestudents.ui.screens.editarPerfil

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.data.repository.AuthRepository
import com.example.thestudents.data.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditarPerfilViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(EditarPerfilState())
    val uiState: StateFlow<EditarPerfilState> = _uiState.asStateFlow()

    init {
        val currentUser = localStudentProvider.currentUser
        val authPhoto = authRepository.currentUser?.photoUrl?.toString()
        val student = if (!authPhoto.isNullOrEmpty()) {
            currentUser.copy(profileImage = authPhoto)
        } else {
            currentUser
        }
        _uiState.update {
            it.copy(
                student = student,
                name = student.name,
                username = student.username,
                bio = student.bio
            )
        }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun updateUsername(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun updateBio(bio: String) {
        _uiState.update { it.copy(bio = bio) }
    }

    fun updateShowReviews(showReviews: Boolean) {
        _uiState.update { it.copy(showReviews = showReviews) }
    }

    fun updateNotificationsEnabled(notificationsEnabled: Boolean) {
        _uiState.update { it.copy(notificationsEnabled = notificationsEnabled) }
    }

    fun uploadImageToFirebase(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = storageRepository.uploadProfileImage(uri)
            if (result.isSuccess) {
                val url = result.getOrNull()
                _uiState.update { state ->
                    state.copy(
                        student = state.student.copy(profileImage = url),
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
