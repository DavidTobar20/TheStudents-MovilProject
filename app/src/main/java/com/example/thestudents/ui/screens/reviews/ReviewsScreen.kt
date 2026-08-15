package com.example.thestudents.ui.screens.reviews

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.R
import com.example.thestudents.ui.utils.DiamondDivider
import com.example.thestudents.ui.utils.FixedBottomBar
import com.example.thestudents.ui.Student

data class CourseSection(
    val title: String,
    val icon: ImageVector,
    val students: List<Student>
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
            color = colorResource(R.color.dark_green),
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Personas con quienes has compartido clase",
            fontSize = 14.sp,
            color = colorResource(R.color.medium_green).copy(alpha = 0.7f),
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
fun ReviewStudentItem(
    student: Student,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(student.profileColor),
            contentAlignment = Alignment.Center
        ) {
            if (student.profileImageRes != null) {
                Image(
                    painter = painterResource(id = student.profileImageRes),
                    contentDescription = "Imagen de perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = student.initials,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = student.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = colorResource(R.color.dark_green)
            )
            Text(
                text = student.period,
                fontSize = 13.sp,
                color = colorResource(R.color.sage)
            )
        }
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.dark_green)),
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
        Student(
            id = "mj",
            name = "María Jiménez",
            email = "maria@u.edu.co",
            program = "Ingeniería",
            semester = 4,
            bio = "",
            rating = 5f,
            reviewsCount = 10,
            initials = "MJ",
            profileColor = Color(0xFF4C8C64),
            period = "2025-2"
        )
    )
}

@Composable
fun CourseSectionCard(
    section: CourseSection,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = section.icon,
                contentDescription = null,
                tint = colorResource(R.color.dark_green),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = section.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = colorResource(R.color.dark_green)
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
                        HorizontalDivider(color = colorResource(R.color.sage).copy(alpha = 0.2f), thickness = 0.5.dp)
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
                Student("mj", "María Jiménez", "maria@u.edu.co", "Ingeniería", 4, "", 5f, 10, "MJ", Color(0xFF4C8C64), "2025-2"),
                Student("lm", "Laura Martínez", "laura@u.edu.co", "Ingeniería", 4, "", 5f, 10, "LM", Color(0xFF7B5CAB), "2025-2")
            )
        )
    )
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

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colorResource(R.color.cream),
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
