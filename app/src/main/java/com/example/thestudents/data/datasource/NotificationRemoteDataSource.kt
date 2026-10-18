package com.example.thestudents.data.datasource

import com.example.thestudents.data.dtos.NotificationDto

interface NotificationRemoteDataSource {

    suspend fun getNotificationsByUserId(userId: String): List<NotificationDto>

    suspend fun createNotification(notification: NotificationDto): Unit

    suspend fun deleteNotification(notificationId: String): Unit

}