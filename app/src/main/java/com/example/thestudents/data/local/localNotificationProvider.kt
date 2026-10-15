package com.example.thestudents.data.local

import com.example.thestudents.data.Notification
import com.example.thestudents.data.NotificationType

object localNotificationProvider {
    val allNotifications = listOf(
        Notification(
            id = 1,
            student = localStudentProvider.students[3], // Maria
            type = NotificationType.REVIEW,
            timeAgo = "5 minutos",
            subject = "Estructuras de Datos",
            rating = 5,
            comment = "Excelente compañera para proyectos y explica muy bien."
        ),
        Notification(
            id = 2,
            student = localStudentProvider.students[2], // Daniel
            type = NotificationType.LIKE,
            timeAgo = "15 minutos",
            reviewOf = "Carlos Gómez"
        ),
        Notification(
            id = 3,
            student = localStudentProvider.students[1], // Valeria
            type = NotificationType.FOLLOW_REQUEST,
            timeAgo = "1 hora"
        ),
        Notification(
            id = 4,
            student = localStudentProvider.students[0], // Juan Pablo
            type = NotificationType.COMMENT,
            timeAgo = "2 horas",
            subject = "Cálculo I",
            courseCode = "MATE1103",
            snippet = "Excelente disposición..."
        )
    )
}
