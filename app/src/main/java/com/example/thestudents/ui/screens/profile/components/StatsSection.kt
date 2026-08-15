package com.example.thestudents.ui.screens.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R
import com.example.thestudents.ui.Student
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun StatsSection(
    student: Student,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        StatItem("128", "SEGUIDORES")
        VerticalDivider(
            modifier = Modifier.height(40.dp),
            color = colorResource(R.color.sage).copy(alpha = 0.3f)
        )
        StatItem("96", "SIGUIENDO")
        VerticalDivider(
            modifier = Modifier.height(40.dp),
            color = colorResource(R.color.sage).copy(alpha = 0.3f)
        )
        StatItem(student.reviewsCount.toString(), "RESEÑAS")
    }
}

@Preview(showBackground = true)
@Composable
fun StatsSectionPreview() {
    TheStudentsTheme {
        StatsSection(
            student = Student(
                id = "1",
                name = "Juan Pablo Mejía",
                email = "juan.pablo.m@u.edu.co",
                program = "Ingeniería de Sistemas",
                semester = 7,
                bio = "",
                rating = 4.8f,
                reviewsCount = 21,
                initials = "JP",
                profileColor = colorResource(R.color.light_tan)
            )
        )
    }
}
