package com.example.thestudents.data.local

import com.example.thestudents.data.CourseSection
import com.example.thestudents.data.Inscription

object localInscriptionProvider {
    val students = localStudentProvider.students
    val currentUser = localStudentProvider.currentUser

    val inscriptions: List<Inscription> = listOf(
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
}
