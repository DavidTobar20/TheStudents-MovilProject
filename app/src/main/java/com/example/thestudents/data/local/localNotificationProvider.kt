package com.example.thestudents.data.local

import com.example.thestudents.data.Notification
import com.example.thestudents.data.NotificationType

object localNotificationProvider {
    val allNotifications = listOf(
        Notification(
            id = 1,
            userName = "María Jiménez",
            userInitials = "MJ",
            type = NotificationType.REVIEW,
            timeAgo = "5 minutos",
            studentId = "4",
            subject = "Estructuras de Datos",
            rating = 5,
            comment = "Excelente compañera para proyectos y explica muy bien."
        ),
        Notification(
            id = 2,
            userName = "Daniel Ruiz",
            userInitials = "DR",
            type = NotificationType.LIKE,
            timeAgo = "15 minutos",
            studentId = "3",
            reviewOf = "Carlos Gómez"
        ),
        Notification(
            id = 3,
            userName = "Laura Martínez",
            userInitials = "LM",
            type = NotificationType.FOLLOW_REQUEST,
            timeAgo = "1 hora",
            studentId = "2"
        ),
        Notification(
            id = 4,
            userName = "Carlos Gómez",
            userInitials = "CG",
            type = NotificationType.COMMENT,
            timeAgo = "2 horas",
            studentId = "1",
            subject = "Cálculo I",
            courseCode = "MATE1103",
            snippet = "Excelente disposición..."
        )
    )
}
