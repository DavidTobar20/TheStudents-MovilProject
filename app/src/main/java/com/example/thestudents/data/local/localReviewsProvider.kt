package com.example.thestudents.data.local

import com.example.thestudents.data.Review

object localReviewsProvider {
    val allReviews = listOf(
        Review(
            reviewer = localStudentProvider.students[0],
            reviewedStudentId = "2", // Para Valeria
            nameReviewed = "Valeria Gómez",
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
            reviewedStudentId = "1", // Para Juan Pablo
            nameReviewed = "Juan Pablo Mejía",
            classReviewed = "MATE110",
            periodReviewed = "2026-1",
            content = "Excelente compañero, explica muy bien los temas complejos y es muy puntual.",
            time = "Hace 1 semana",
            likes = 12,
            disLikes = 0,
            rating = null,
            comments = localCommentsProvider.allComments.subList(0, 1)
        ),
        Review(
            reviewer = localStudentProvider.students[2],
            reviewedStudentId = "2", // Para Valeria
            nameReviewed = "Valeria Gómez",
            classReviewed = "FIS103",
            periodReviewed = "2024-3",
            content = "Gran capacidad de liderazgo en el trabajo en equipo. Siempre está dispuesta a colaborar.",
            time = "Hace 2 meses",
            likes = 3,
            disLikes = 0,
            rating = "3",
            comments = emptyList()
        ),
        Review(
            reviewer = localStudentProvider.students[3],
            reviewedStudentId = "1", // Para Juan Pablo
            nameReviewed = "Juan Pablo Mejía",
            classReviewed = "FIS101",
            periodReviewed = "2024-2",
            content = "Muy buen compañero, siempre dispuesto a ayudar a los demás.",
            time = "Hace 1 mes",
            likes = 8,
            disLikes = 0,
            rating = "5",
            comments = emptyList()
        ),
        Review(
            reviewer = localStudentProvider.students[4],
            reviewedStudentId = "3", // Para Daniel Ruiz
            nameReviewed = "Daniel Ruiz",
            classReviewed = "ISIS1206",
            periodReviewed = "2024-2",
            content = "Excelente analista, sus aportes en el código fueron vitales.",
            time = "Hace 2 semanas",
            likes = 10,
            disLikes = 1,
            rating = "4.8",
            comments = emptyList()
        ),
        Review(
            reviewer = localStudentProvider.students[0],
            reviewedStudentId = "3", // Para Daniel Ruiz
            nameReviewed = "Daniel Ruiz",
            classReviewed = "FIS102",
            periodReviewed = "2025-1",
            content = "Muy juicioso con las entregas y puntual.",
            time = "Hace 5 días",
            likes = 2,
            disLikes = 0,
            rating = "4.0",
            comments = emptyList()
        ),
        Review(
            reviewer = localStudentProvider.students[1],
            reviewedStudentId = "4", // Para María Jiménez
            nameReviewed = "María Jiménez",
            classReviewed = "IIND2100",
            periodReviewed = "2025-1",
            content = "Una líder increíble, organizó todo el grupo de maravilla.",
            time = "Hace 3 semanas",
            likes = 15,
            disLikes = 0,
            rating = "5.0",
            comments = emptyList()
        ),
        Review(
            reviewer = localStudentProvider.students[2],
            reviewedStudentId = "5", // Para Valentina Torres
            nameReviewed = "Valentina Torres",
            classReviewed = "PSIC1101",
            periodReviewed = "2024-2",
            content = "Muy empática y buena para mediar en conflictos de grupo.",
            time = "Hace 4 meses",
            likes = 20,
            disLikes = 0,
            rating = "5.0",
            comments = emptyList()
        )
    )

    fun getReviewsForStudent(studentId: String): List<Review> = 
        allReviews.filter { it.reviewedStudentId == studentId }
}
