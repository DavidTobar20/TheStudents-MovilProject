package com.example.thestudents.data.local

import com.example.thestudents.data.CourseSection
import com.example.thestudents.data.Inscription

object localInscriptionProvider {
    val students = localStudentProvider.students
    val currentUser = localStudentProvider.currentUser

    val inscriptions: List<Inscription> = listOf(
        // Materia compartida universal en semilla oficial del backend en GitHub
        Inscription(id = "i_bd1", student = currentUser, className = "Bases de Datos", period = "2026-1"),
        Inscription(id = "i_bd2", student = students[1], className = "Bases de Datos", period = "2026-1"),
        Inscription(id = "i_bd3", student = students[2], className = "Bases de Datos", period = "2026-1"),
        Inscription(id = "i_bd4", student = students[3], className = "Bases de Datos", period = "2026-1"),
        Inscription(id = "i_bd5", student = students[4], className = "Bases de Datos", period = "2026-1"),

        // Materias locales / adicionales
        Inscription(id = "i1", student = currentUser, className = "Estructuras de Datos (ISIS1206)", period = "2025-1"),
        Inscription(id = "i2", student = currentUser, className = "Física Mecánica (FIS1027)", period = "2025-1"),
        Inscription(id = "i3", student = students[1], className = "Estructuras de Datos (ISIS1206)", period = "2025-1"),
        Inscription(id = "i4", student = students[2], className = "Física Mecánica (FIS1027)", period = "2025-1"),
        Inscription(id = "i5", student = students[3], className = "Estructuras de Datos (ISIS1206)", period = "2025-1"),
        Inscription(id = "i6", student = students[3], className = "Cálculo I (MATE1103)", period = "2025-1"),
        Inscription(id = "i7", student = students[4], className = "Cálculo I (MATE1103)", period = "2025-1")
    )

    fun getCourseSectionsForUser(currentUserId: String): List<CourseSection> {
        val myInscriptions = inscriptions.filter { it.student.id == currentUserId }
        val myClasses = myInscriptions.map { it.className to it.period }.toSet()

        val sharedInscriptions = inscriptions.filter { inscription ->
            inscription.student.id != currentUserId &&
            (inscription.className to inscription.period) in myClasses
        }

        val groupedByClass = sharedInscriptions.groupBy { it.className }

        return groupedByClass.map { (className, inscrs) ->
            val period = inscrs.firstOrNull()?.period ?: ""
            CourseSection(
                title = className,
                period = period,
                inscriptions = inscrs.distinctBy { it.student.id }
            )
        }
    }

    fun getSharedClassFor(userId1: String, userId2: String): Pair<String, String>? {
        val u1Inscriptions = inscriptions.filter { it.student.id == userId1 }
        val u2Inscriptions = inscriptions.filter { it.student.id == userId2 }
        for (i1 in u1Inscriptions) {
            val match = u2Inscriptions.find { it.className == i1.className && it.period == i1.period }
            if (match != null) return i1.className to i1.period
        }
        return "Estructuras de Datos (ISIS1206)" to "2025-1"
    }
}
