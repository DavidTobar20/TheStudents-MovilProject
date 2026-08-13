package com.example.thestudents.ui.screens.reviews

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.ui.components.DiamondDivider
import com.example.thestudents.ui.components.FixedBottomBar
import com.example.thestudents.ui.theme.*

data class ReviewStudent(
    val initials: String,
    val name: String,
    val period: String,
    val color: Color
)

data class CourseSection(
    val title: String,
    val icon: ImageVector,
    val students: List<ReviewStudent>
)

@Composable
fun HeaderReviews(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "RESEÑAR COMPAÑEROS",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = DarkGreen,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Personas con quienes has compartido clase",
            fontSize = 14.sp,
            color = MediumGreen.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 4.dp),
            textAlign = TextAlign.Center
        )
        DiamondDivider(modifier = Modifier.padding(horizontal = 48.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderReviewsPreview() {
    HeaderReviews(modifier = Modifier.padding(16.dp))
}

@Composable
fun ReviewStudentItem(student: ReviewStudent) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(student.color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = student.initials,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = student.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = DarkGreen
            )
            Text(
                text = student.period,
                fontSize = 13.sp,
                color = Sage
            )
        }
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("Reseñar", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewStudentItemPreview() {
    ReviewStudentItem(
        ReviewStudent("MJ", "María Jiménez", "2025-2", Color(0xFF4C8C64))
    )
}

@Composable
fun CourseSectionCard(section: CourseSection) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = section.icon,
                contentDescription = null,
                tint = DarkGreen,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = section.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = DarkGreen
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                section.students.forEachIndexed { index, student ->
                    ReviewStudentItem(student)
                    if (index < section.students.size - 1) {
                        HorizontalDivider(color = Sage.copy(alpha = 0.2f), thickness = 0.5.dp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CourseSectionCardPreview() {
    CourseSectionCard(
        CourseSection(
            title = "Estructuras de Datos (ISIS1206)",
            icon = Icons.Default.Storage,
            students = listOf(
                ReviewStudent("MJ", "María Jiménez", "2025-2", Color(0xFF4C8C64)),
                ReviewStudent("LM", "Laura Martínez", "2025-2", Color(0xFF7B5CAB))
            )
        )
    )
}

@Composable
fun ReviewsScreen(navController: NavController = rememberNavController()) {
    val sections = listOf(
        CourseSection(
            title = "Estructuras de Datos (ISIS1206)",
            icon = Icons.Default.Storage,
            students = listOf(
                ReviewStudent("MJ", "María Jiménez", "2025-2", Color(0xFF4C8C64)),
                ReviewStudent("LM", "Laura Martínez", "2025-2", Color(0xFF7B5CAB))
            )
        ),
        CourseSection(
            title = "Física Mecánica (FIS1027)",
            icon = Icons.Default.Settings,
            students = listOf(
                ReviewStudent("DR", "Daniel Ruiz", "2025-2", Color(0xFF1E3D2A)),
                ReviewStudent("SP", "Sofía Pérez", "2025-2", Color(0xFF2C55A0))
            )
        ),
        CourseSection(
            title = "Cálculo I (MATE1103)",
            icon = Icons.Default.Calculate,
            students = listOf(
                ReviewStudent("CG", "Carlos Gómez", "2025-1", Color(0xFF9E4B31)),
                ReviewStudent("AV", "Andrés Vargas", "2025-1", Color(0xFF8B5E3C))
            )
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Cream,
        bottomBar = { FixedBottomBar(navController, "reviews") }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReviewsScreenPreview() {
    ReviewsScreen()
}
