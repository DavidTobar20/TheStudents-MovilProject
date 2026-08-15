package com.example.thestudents.ui.screens.reviews.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.CourseSection
import com.example.thestudents.ui.Student
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun CourseSectionCard(
    section: CourseSection,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = section.icon,
                contentDescription = null,
                tint = colorResource(R.color.dark_green),
                modifier = Modifier.size(20.dp),
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
                    if (index < (section.students.size - 1)) {
                        HorizontalDivider(
                            color = colorResource(R.color.sage).copy(alpha = 0.2f),
                            thickness = 0.5.dp
                        )
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
        section = CourseSection(
            title = "Estructuras de Datos (ISIS1206)",
            icon = Icons.Default.Storage,
            students = listOf(
                Student(
                    "mj",
                    "María Jiménez",
                    "maria@u.edu.co",
                    "Ingeniería",
                    4,
                    "",
                    5f,
                    10,
                    "MJ",
                    Color(0xFF4C8C64),
                    "2025-2"
                ),
                Student(
                    "lm",
                    "Laura Martínez",
                    "laura@u.edu.co",
                    "Ingeniería",
                    4,
                    "",
                    5f,
                    10,
                    "LM",
                    Color(0xFF7B5CAB),
                    "2025-2"
                )
            )
        )
    )
}
