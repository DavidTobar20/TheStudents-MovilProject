package com.example.thestudents.data.local

import com.example.thestudents.data.Review

object localReviewsProvider {
    val allReviews = listOf(
        Review(
            reviewer = localStudentProvider.students[0],
            nameReviewed = "Luisa Mendoza",
            classReviewed = "MATE120",
            periodReviewed = "2025-3",
            content = "Muy responsable, aportó ideas clave en todas las etapas del proyecto.",
            time = "Hace 3 días",
            likes = 5,
            disLikes = 0,
            rating = "4.5",
            comments = localCommentsProvider.allComments
        ),
        Review(
            reviewer = localStudentProvider.students[1],
            nameReviewed = "Luisa Mendoza",
            classReviewed = "MATE110",
            periodReviewed = "2026-1",
            content = "Excelente compañera, explica muy bien los temas complejos y es muy puntual.",
            time = "Hace 1 semana",
            likes = 12,
            disLikes = 0,
            rating = null,
            comments = localCommentsProvider.allComments.subList(0,1)
        ),
        Review(
            reviewer = localStudentProvider.students[2],
            nameReviewed = "Luisa Mendoza",
            classReviewed = "FIS103",
            periodReviewed = "2024-3",
            content = "Gran capacidad de liderazgo en el trabajo en equipo. Siempre está dispuesta a colaborar.",
            time = "Hace 2 meses",
            likes = 3,
            disLikes = 0,
            rating = "3",
            comments = emptyList()
        )
    )
}
