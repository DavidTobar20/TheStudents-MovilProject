package com.example.thestudents.data.repository

import com.example.thestudents.data.Notification
import com.example.thestudents.data.datasource.impl.firestore.NotificationFirestoreDataSourceImpl
import com.example.thestudents.data.dtos.toNotification
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val notificationRemoteDataSource: NotificationFirestoreDataSourceImpl
) {

    suspend fun getNotifications(userId: String): Result<List<Notification>> {
        return try {
            val response = notificationRemoteDataSource.getNotificationsByUserId(userId)
            Result.success(response.map { it.toNotification() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteNotification(notificationId: String): Result<Unit> {
        return try {
            notificationRemoteDataSource.deleteNotification(notificationId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
