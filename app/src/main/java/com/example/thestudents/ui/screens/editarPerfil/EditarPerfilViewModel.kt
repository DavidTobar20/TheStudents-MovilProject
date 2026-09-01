package com.example.thestudents.ui.screens.editarPerfil

import androidx.lifecycle.ViewModel
import com.example.thestudents.data.local.localStudentProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class EditarPerfilViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EditarPerfilState())
    val uiState : StateFlow<EditarPerfilState> = _uiState

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

    fun saveEdit() {
        _uiState.update { it.copy(saveEdit = true) }
    }

    fun navigateBack() {
        _uiState.update { it.copy(navigateBack = true) }
    }

    fun onNavigated() {
        _uiState.update { it.copy(saveEdit = false, navigateBack = false) }
    }

    fun updatePhoto() {

    }

    init {
        _uiState.update { it.copy(
            student = localStudentProvider.currentUser,
            name = localStudentProvider.currentUser.name,
            username = localStudentProvider.currentUser.username,
            bio = localStudentProvider.currentUser.bio
        ) }
    }
}