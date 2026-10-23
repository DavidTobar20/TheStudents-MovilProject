package com.example.thestudents.data

enum class NotificationType {
    REVIEW, LIKE, FOLLOW_REQUEST, COMMENT, FOLLOW // FOLLOW: alguien empezo a seguirte
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
    val reviewId: String? = null
)
