package com.example.thestudents.data.local

import com.example.thestudents.data.Review

object localReviewsProvider {
    val allReviews = listOf(
        Review(
            authorName = "Daniel Ruiz",
            authorInitials = "DR",
            courseAndPeriod = "FIS220, 2025-2",
            content = "Muy responsable, aportó ideas clave en todas las etapas del proyecto.",
            time = "Hace 3 días",
            likes = 5
        ),
        Review(
            authorName = "Valeria Gómez",
            authorInitials = "VG",
            courseAndPeriod = "MAT101, 2024-1",
            content = "Excelente compañera, explica muy bien los temas complejos y es muy puntual.",
            time = "Hace 1 semana",
            likes = 12
        ),
        Review(
            authorName = "Mateo Salazar",
            authorInitials = "MS",
            courseAndPeriod = "PROG302, 2024-2",
            content = "Gran capacidad de liderazgo en el trabajo en equipo. Siempre está dispuesta a colaborar.",
            time = "Hace 2 meses",
            likes = 3
        )
    )
}
