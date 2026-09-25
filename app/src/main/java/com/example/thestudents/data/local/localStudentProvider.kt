package com.example.thestudents.data.local

import com.example.thestudents.data.Student
import com.example.thestudents.ui.theme.Avatar2
import com.example.thestudents.ui.theme.Avatar3
import com.example.thestudents.ui.theme.Avatar4
import com.example.thestudents.ui.theme.Avatar5
import com.example.thestudents.ui.theme.Avatar7

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
            profileColor = Avatar5,
            period = "2024-2",
            profileImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTU8TFJ7iUwyhF0_LOmPpst5aFLBQUYvRcuREn63JTVvg&s=10"
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
            profileColor = Avatar2,
            period = "2025-1",
            profileImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSUZ126jfV5AGfpktXWWmTEFgk2RR39k68nIDL2favyjg&s=10"
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
            profileColor = Avatar3,
            period = "2025-2",
            profileImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQojgKLiFp8-dyqIK4ohTSzt9c9xrJS7uo_GPagEvVQnQ&s=10"
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
            profileColor = Avatar4,
            period = "2025-2",
            profileImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjI4Khb5hnQidFixTCwkg-wn4syJsMg2bUN4T_87hcpg&s=10"
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
            profileColor = Avatar7,
            period = "2024-2",
            profileImage = "https://radionacional-v3.s3.amazonaws.com/s3fs-public/node/article/field_image/Diomedes%20D%C3%ADas.jpg"
        )
    )
    
    val currentUser = students[0]
    
    // Simulación de seguidos: el usuario actual sigue a Valeria (2) y Daniel (3)
    val followingIds = setOf("2", "3")

    fun getStudentById(id: String): Student? = students.find { it.id == id }
}
