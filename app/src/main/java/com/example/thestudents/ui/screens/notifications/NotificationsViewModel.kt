package com.example.thestudents.ui.screens.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.local.localNotificationProvider
import com.example.thestudents.data.repository.AuthRepository
import com.example.thestudents.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val authRepository: AuthRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(NotificationsState())
    val uiState: StateFlow<NotificationsState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        val userId = authRepository.currentUser?.uid
        if (userId == null) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "No hay un usuario autenticado") }
            return
        }
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val result = notificationRepository.getNotifications(userId)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        notifications = result.getOrNull() ?: emptyList(),
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message
                            ?: "Error al cargar las notificaciones"
                    )
                }
            }
        }
    }

    fun onAcceptNotification(notificationId: String) {
        removeNotification(notificationId)
    }

    fun onRejectNotification(notificationId: String) {
        removeNotification(notificationId)
    }

    private fun removeNotification(notificationId: String) {
        _uiState.update { state ->
            state.copy(notifications = state.notifications.filter { it.id != notificationId })
        }
        viewModelScope.launch {
            notificationRepository.deleteNotification(notificationId)
        }
    }

}
