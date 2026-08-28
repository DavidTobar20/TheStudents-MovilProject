package com.example.thestudents.data.local

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import com.example.thestudents.data.CourseSection
import com.example.thestudents.data.Student
import com.example.thestudents.ui.theme.Avatar1
import com.example.thestudents.ui.theme.Avatar2
import com.example.thestudents.ui.theme.Avatar3
import com.example.thestudents.ui.theme.Avatar4
import com.example.thestudents.ui.theme.Avatar6
import com.example.thestudents.ui.theme.Avatar7

/**
 * Datos de ejemplo de las secciones de curso.
 *
 * Esta lista se construia dentro del composable de ReviewsScreen, asi que se creaba de nuevo en
 * cada recomposicion y estaba duplicada en la preview. Aqui se crea una sola vez.
 */
object localCourseSectionProvider {
    val sections: List<CourseSection> = listOf(
        CourseSection(
            title = "Estructuras de Datos (ISIS1206)",
            icon = Icons.Default.Storage,
            students = listOf(
                localStudentProvider.students[3], // Maria Jimenez (ID 4)
                localStudentProvider.students[1]  // Valeria Gomez (ID 2)
            )
        ),
        CourseSection(
            title = "Física Mecánica (FIS1027)",
            icon = Icons.Default.Settings,
            students = listOf(
                localStudentProvider.students[2], // Daniel Ruiz (ID 3)
                localStudentProvider.students[4]  // Valentina Torres (ID 5)
            )
        ),
        CourseSection(
            title = "Cálculo I (MATE1103)",
            icon = Icons.Default.Calculate,
            students = listOf(
                localStudentProvider.students[0], // Juan Pablo (ID 1)
                localStudentProvider.students[2]  // Daniel Ruiz (ID 3)
            )
        )
    )
}
