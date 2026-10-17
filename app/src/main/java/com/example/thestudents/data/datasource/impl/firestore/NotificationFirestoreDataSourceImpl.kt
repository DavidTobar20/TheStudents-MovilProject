package com.example.thestudents.data.datasource.impl.firestore

import com.example.thestudents.data.datasource.NotificationRemoteDataSource
import com.example.thestudents.data.dtos.NotificationDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
): NotificationRemoteDataSource {

    // Notificaciones que recibio el usuario, de la mas reciente a la mas antigua.
    // Se ordena en el cliente para no necesitar un indice compuesto (whereEqualTo + orderBy).
    override suspend fun getNotificationsByUserId(userId: String): List<NotificationDto> {
        val snapshot = db.collection("notificaciones")
            .whereEqualTo("destinatarioId", userId)
            .get()
            .await()
        return snapshot.documents
            .mapNotNull { doc ->
                doc.toObject(NotificationDto::class.java)?.copy(id = doc.id)
            }
            .sortedByDescending { it.fechaCreacion }
    }

    override suspend fun createNotification(notification: NotificationDto) {
        db.collection("notificaciones").add(notification).await()
    }

    override suspend fun deleteNotification(notificationId: String) {
        db.collection("notificaciones").document(notificationId).delete().await()
    }
}
