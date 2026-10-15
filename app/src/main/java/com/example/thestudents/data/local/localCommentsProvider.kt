package com.example.thestudents.data.local

import com.example.thestudents.data.Comment

object localCommentsProvider {
    val allComments = listOf(
        Comment(
            commentator = localStudentProvider.students[0],
            content = "Siempre llega preparada a clase y tiene muy buenas ideas para resolver los problemas del laboratorio.",
            likes = 25,
            disLikes = 2,
            createdAt = "hace 6 horas"
        ),
        Comment(
            commentator = localStudentProvider.students[1],
            content = "Este compañero es muy amigable y siempre está dispuesto a ayudar.",
            likes = 12,
            disLikes = 3,
            createdAt = "hace 24 horas"
        )
    )
}