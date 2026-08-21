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
                Student("mj", "María Jiménez", "maria@u.edu.co", "Ingeniería", 4, "", 5f, 10, "MJ", Avatar4, "2025-2"),
                Student("lm", "Laura Martínez", "laura@u.edu.co", "Ingeniería", 4, "", 5f, 10, "LM", Avatar7, "2025-2")
            )
        ),
        CourseSection(
            title = "Física Mecánica (FIS1027)",
            icon = Icons.Default.Settings,
            students = listOf(
                Student("dr", "Daniel Ruiz", "daniel@u.edu.co", "Física", 3, "", 4f, 8, "DR", Avatar3, "2025-2"),
                Student("sp", "Sofía Pérez", "sofia@u.edu.co", "Física", 3, "", 5f, 12, "SP", Avatar6, "2025-2")
            )
        ),
        CourseSection(
            title = "Cálculo I (MATE1103)",
            icon = Icons.Default.Calculate,
            students = listOf(
                Student("cg", "Carlos Gómez", "carlos@u.edu.co", "Matemáticas", 2, "", 4f, 5, "CG", Avatar1, "2025-1"),
                Student("av", "Andrés Vargas", "andres@u.edu.co", "Matemáticas", 2, "", 3f, 4, "AV", Avatar2, "2025-1")
            )
        )
    )
}
