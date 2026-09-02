package com.example.thestudents.ui.screens.notifications

import com.example.thestudents.data.Notification

data class NotificationsState(
    val notifications: List<Notification> = emptyList(),
    val isLoading: Boolean = false
)
