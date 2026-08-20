package com.example.thestudents.data

enum class NotificationType {
    REVIEW, LIKE, FOLLOW_REQUEST, COMMENT
}

data class Notification(
    val id: Int,
    val userName: String,
    val userInitials: String,
    val type: NotificationType,
    val timeAgo: String,
    // Campos opcionales según el tipo
    val subject: String? = null,
    val rating: Int? = null,
    val comment: String? = null,
    val reviewOf: String? = null,
    val courseCode: String? = null,
    val snippet: String? = null
)
