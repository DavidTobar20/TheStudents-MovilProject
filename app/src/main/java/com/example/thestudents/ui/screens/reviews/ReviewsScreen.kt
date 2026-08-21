package com.example.thestudents.ui.screens.reviews

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.data.CourseSection
import com.example.thestudents.data.Student
import com.example.thestudents.ui.screens.reviews.components.CourseSectionCard
import com.example.thestudents.ui.screens.reviews.components.HeaderReviews
import com.example.thestudents.ui.theme.Avatar1
import com.example.thestudents.ui.theme.Avatar2
import com.example.thestudents.ui.theme.Avatar3
import com.example.thestudents.ui.theme.Avatar4
import com.example.thestudents.ui.theme.Avatar6
import com.example.thestudents.ui.theme.Avatar7
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.FixedBottomBar

@Composable
fun BodyReviews(
    sections: List<CourseSection>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 24.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
            HeaderReviews()
        }
        items(sections) { section ->
            CourseSectionCard(section)
        }
    }
}

@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun BodyReviewsPreview() {
    TheStudentsTheme {
        Surface {
            val mockSections = listOf(
                CourseSection(
                    title = "Estructuras de Datos (ISIS1206)",
                    icon = Icons.Default.Storage,
                    students = listOf(
                        Student("mj", "María Jiménez", "maria@u.edu.co", "Ingeniería", 4, "", 5f, 10, "MJ", Avatar4, "2025-2"),
                        Student("lm", "Laura Martínez", "laura@u.edu.co", "Ingeniería", 4, "", 5f, 10, "LM", Avatar7, "2025-2")
                    )
                )
            )
            BodyReviews(sections = mockSections)
        }
    }
}

@Composable
fun ReviewsScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController()
) {
    val sections = listOf(
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
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { FixedBottomBar(navController, "reviews") }
    ) { padding ->
        BodyReviews(
            sections = sections,
            contentPadding = padding
        )
    }
}

@Preview(name = "Claro", showBackground = true, showSystemUi = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun ReviewsScreenPreview() {
    TheStudentsTheme {
        Surface {
            ReviewsScreen()
        }
    }
}
