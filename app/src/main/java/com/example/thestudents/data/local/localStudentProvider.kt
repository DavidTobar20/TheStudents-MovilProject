package com.example.thestudents.data.local

import androidx.compose.ui.graphics.Color
import com.example.thestudents.R
import com.example.thestudents.data.Student

object localStudentProvider {
    val students = listOf(
        Student(
            id = "1",
            name = "Juan Pablo Mejía",
            username = "@juan.pablo.m",
            program = "Ingeniería de Sistemas",
            semester = 7,
            bio = "Me gusta trabajar en equipo y aprender de proyectos reales. Abierto a grupos de estudio.",
            rating = 4.8f,
            reviewsCount = 21,
            initials = "JP",
            profileColor = Color(0xFFF0EAE1),
            period = "2024-2",
            profileImageRes = R.drawable.logosinfondo
        ),
        Student(
            id = "2",
            name = "Valeria Gómez",
            username = "@valeria.gomez",
            program = "Diseño Gráfico",
            semester = 5,
            bio = "Apasionada por el UI/UX y la ilustración digital.",
            rating = 4.9f,
            reviewsCount = 15,
            initials = "VG",
            profileColor = Color(0xFFD3C3A7),
            period = "2025-1"
        ),
        Student(
            id = "3",
            name = "Daniel Ruiz",
            username = "@daniel.ruiz",
            program = "Física",
            semester = 8,
            bio = "Enfoque en investigación y resolución de problemas complejos.",
            rating = 4.5f,
            reviewsCount = 10,
            initials = "DR",
            profileColor = Color(0xFFB2B7AC),
            period = "2025-2"
        ),
        Student(
            id = "4",
            name = "María Jiménez",
            username = "@maria.j",
            program = "Ingeniería Industrial",
            semester = 4,
            bio = "Líder nata y muy organizada.",
            rating = 5.0f,
            reviewsCount = 10,
            initials = "MJ",
            profileColor = Color(0xFF4C8C64),
            period = "2025-2"
        ),
        Student(
            id = "5",
            name = "Valentina Torres",
            username = "@valentina.t",
            program = "Psicología",
            semester = 5,
            bio = "Interesada en comportamiento organizacional.",
            rating = 5.0f,
            reviewsCount = 28,
            initials = "VT",
            profileColor = Color(0xFF7B5CAB),
            period = "2024-2"
        )
    )
    
    val currentUser = students[0]
}
