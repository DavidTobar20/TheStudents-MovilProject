package com.example.thestudents.ui.screens.commentsReview

import com.example.thestudents.R
import com.example.thestudents.data.Review
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.theme.Avatar5

data class CommentsReviewState(
    val commentInputText: String = "",
    val isLiked: Boolean = false,
    val isDisliked: Boolean = false,
    val likedComments: Set<Int> = emptySet(),
    val dislikedComments: Set<Int> = emptySet(),
    val commentator: Student = Student(
        id = "1",
        name = "Juan Pablo Mejía",
        username = "@juan.pablo.m",
        program = "Ingeniería de Sistemas",
        semester = 7,
        bio = "Me gusta trabajar en equipo y aprender de proyectos reales. Abierto a grupos de estudio.",
        rating = 4.8f,
        reviewsCount = 21,
        initials = "JP",
        profileColor = Avatar5,
        period = "2024-2",
        profileImage = R.drawable.logosinfondo
    ),
    val review: Review = Review(
        id = "7",
        reviewer = localStudentProvider.students[0],
        reviewedStudent = localStudentProvider.students[2], // Daniel
        classReviewed = "FIS102",
        periodReviewed = "2025-1",
        content = "Muy juicioso con las entregas y puntual.",
        time = "Hace 5 días",
        likes = 2,
        disLikes = 0,
        rating = "4.0",
        comments = emptyList()
    ),
    val navigateBack: Boolean = false
)
