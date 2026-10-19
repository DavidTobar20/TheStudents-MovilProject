package com.example.thestudents.data.dtos

import com.example.thestudents.data.Notification
import com.example.thestudents.data.NotificationType
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class NotificationDto(
    @get:Exclude val id: String? = null, // el id es el del documento, no se guarda como campo
    val destinatarioId: String? = null,
    val emisorId: String? = null,
    val resenaId: String? = null, // reseña a la que lleva "Ver reseña completa"
    val tipo: String? = null, // REVIEW, LIKE, FOLLOW_REQUEST, COMMENT
    val materia: String? = null,
    val rating: Int? = null,
    val comentario: String? = null,
    val resenaDe: String? = null,
    val codigoMateria: String? = null,
    val fragmento: String? = null,
    val emisor: StudentSummaryDto? = null,
    val fechaCreacion: Timestamp? = null // tipo fecha nativo de Firestore
)

fun NotificationDto.toNotification(): Notification {
    val notificationType = NotificationType.values()
        .firstOrNull { it.name == tipo?.uppercase() } ?: NotificationType.REVIEW
    return Notification(
        id = id,
        student = (emisor ?: StudentSummaryDto()).copy(id = emisor?.id ?: emisorId).toStudent(),
        type = notificationType,
        timeAgo = timeAgo(fechaCreacion),
        subject = materia,
        rating = rating,
        comment = comentario,
        reviewOf = resenaDe,
        courseCode = codigoMateria,
        snippet = fragmento,
        reviewId = resenaId
    )
}

// Se muestra como "1 minuto", "2 horas", etc.
fun timeAgo(fecha: Timestamp?): String {
    if (fecha == null) return "un momento"
    val minutos = ((System.currentTimeMillis() - fecha.toDate().time) / 60_000).coerceAtLeast(0)
    val horas = minutos / 60
    val dias = horas / 24

    return when {
        minutos < 1 -> "un momento"
        minutos < 60 -> if (minutos == 1L) "1 minuto" else "$minutos minutos"
        horas < 24 -> if (horas == 1L) "1 hora" else "$horas horas"
        else -> if (dias == 1L) "1 día" else "$dias días"
    }
}
