package com.example.thestudents.data

enum class NotificationType {
    REVIEW, LIKE, FOLLOW_REQUEST, COMMENT, FOLLOW, // FOLLOW: alguien empezo a seguirte
    TEAM_INVITE // alguien te invito a su equipo de trabajo ("Arma tu equipo")
}

data class Notification(
    val id: String?,
    val student: Student,
    val type: NotificationType,
    val timeAgo: String,
    // Campos opcionales según el tipo
    val subject: String? = null,
    val rating: Int? = null,
    val comment: String? = null,
    val reviewOf: String? = null,
    val courseCode: String? = null,
    val snippet: String? = null,
    val reviewId: String? = null,
    val period: String? = null // periodo de la materia (invitaciones de equipo)
)
