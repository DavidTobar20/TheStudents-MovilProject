package com.example.thestudents.ui.screens.reviews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.R
import com.example.thestudents.ui.CourseSection
import com.example.thestudents.ui.utils.DiamondDivider
import com.example.thestudents.ui.utils.FixedBottomBar
import com.example.thestudents.ui.utils.ProfileIcon
import com.example.thestudents.ui.screens.reviews.components.HeaderReviews
import com.example.thestudents.ui.screens.reviews.components.ReviewStudentItem
import com.example.thestudents.ui.screens.reviews.components.CourseSectionCard
import com.example.thestudents.ui.Student
import com.example.thestudents.ui.utils.ButtonWithoutLogo
import com.example.thestudents.ui.theme.TheStudentsTheme

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

@Preview(showBackground = true)
@Composable
fun BodyReviewsPreview() {
    val mockSections = listOf(
        CourseSection(
            title = "Estructuras de Datos (ISIS1206)",
            icon = Icons.Default.Storage,
            students = listOf(
                Student("mj", "María Jiménez", "maria@u.edu.co", "Ingeniería", 4, "", 5f, 10, "MJ", Color(0xFF4C8C64), "2025-2"),
                Student("lm", "Laura Martínez", "laura@u.edu.co", "Ingeniería", 4, "", 5f, 10, "LM", Color(0xFF7B5CAB), "2025-2")
            )
        )
    )
    BodyReviews(sections = mockSections)
}

@Composable
fun ReviewsScreen(
    sections: List<CourseSection>,
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController()
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colorResource(R.color.cream),
        bottomBar = { FixedBottomBar(navController, "reviews") }
    ) { padding ->
        BodyReviews(
            sections = sections,
            contentPadding = padding
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReviewsScreenPreview() {
    val sections = listOf(
        CourseSection(
            title = "Estructuras de Datos (ISIS1206)",
            icon = Icons.Default.Storage,
            students = listOf(
                Student("mj", "María Jiménez", "maria@u.edu.co", "Ingeniería", 4, "", 5f, 10, "MJ", Color(0xFF4C8C64), "2025-2"),
                Student("lm", "Laura Martínez", "laura@u.edu.co", "Ingeniería", 4, "", 5f, 10, "LM", Color(0xFF7B5CAB), "2025-2")
            )
        ),
        CourseSection(
            title = "Física Mecánica (FIS1027)",
            icon = Icons.Default.Settings,
            students = listOf(
                Student("dr", "Daniel Ruiz", "daniel@u.edu.co", "Física", 3, "", 4f, 8, "DR", Color(0xFF1E3D2A), "2025-2"),
                Student("sp", "Sofía Pérez", "sofia@u.edu.co", "Física", 3, "", 5f, 12, "SP", Color(0xFF2C55A0), "2025-2")
            )
        ),
        CourseSection(
            title = "Cálculo I (MATE1103)",
            icon = Icons.Default.Calculate,
            students = listOf(
                Student("cg", "Carlos Gómez", "carlos@u.edu.co", "Matemáticas", 2, "", 4f, 5, "CG", Color(0xFF9E4B31), "2025-1"),
                Student("av", "Andrés Vargas", "andres@u.edu.co", "Matemáticas", 2, "", 3f, 4, "AV", Color(0xFF8B5E3C), "2025-1")
            )
        )
    )
    ReviewsScreen(sections)
}
