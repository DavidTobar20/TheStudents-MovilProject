package com.example.thestudents.data.local

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import com.example.thestudents.data.CourseSection
import com.example.thestudents.data.Student
import kotlin.random.Random

/**
 * Datos de ejemplo de las secciones de curso.
 *
 * Esta lista se construia dentro del composable de ReviewsScreen, asi que se creaba de nuevo en
 * cada recomposicion y estaba duplicada en la preview. Aqui se crea una sola vez.
 */
object localCourseSectionProvider {
    private val allStudents = localStudentProvider.students

    private fun getRandomStudents(count: Int): List<Student> {
        return allStudents.shuffled(Random).take(count)
    }

    val sections: List<CourseSection> = listOf(
        CourseSection(
            title = "Estructuras de Datos (ISIS1206)",
            icon = Icons.Default.Storage,
            students = getRandomStudents(2)
        ),
        CourseSection(
            title = "Física Mecánica (FIS1027)",
            icon = Icons.Default.Settings,
            students = getRandomStudents(2)
        ),
        CourseSection(
            title = "Cálculo I (MATE1103)",
            icon = Icons.Default.Calculate,
            students = getRandomStudents(2)
        )
    )
}
