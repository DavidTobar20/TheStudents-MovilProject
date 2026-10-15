package com.example.thestudents.ui.screens.notifications

import androidx.lifecycle.ViewModel
import com.example.thestudents.data.local.localNotificationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(NotificationsState())
    val uiState: StateFlow<NotificationsState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        _uiState.update { it.copy(isLoading = true) }
        // Simulamos la carga con los datos locales
        val notifications = localNotificationProvider.allNotifications
        _uiState.update {
            it.copy(
                notifications = notifications,
                isLoading = false
            )
        }
    }

    fun onAcceptNotification(notificationId: Int) {
        // Por ahora solo removemos de la lista localmente
        _uiState.update { state ->
            state.copy(
                notifications = state.notifications.filter { it.id != notificationId }
            )
        }
    }

    fun onRejectNotification(notificationId: Int) {
        // Por ahora solo removemos de la lista localmente
        _uiState.update { state ->
            state.copy(
                notifications = state.notifications.filter { it.id != notificationId }
            )
        }
    }
    init {

    }
}
