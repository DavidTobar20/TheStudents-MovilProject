package com.example.thestudents.ui.screens.reviews.components

import androidx.compose.foundation.layout.*
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
import com.example.thestudents.model.Student
import com.example.thestudents.ui.utils.ButtonWithoutIcon
import com.example.thestudents.ui.utils.ProfileIcon

@Composable
fun ReviewStudentItem(
    student: Student,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfileIcon(
            initials = student.initials,
            profileImageRes = student.profileImageRes,
            backgroundColor = student.profileColor,
            contentColor = Color.White,
            fontSize = 18.sp,
            modifier = Modifier.size(48.dp)
        )
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

        ButtonWithoutIcon(
            textoBoton = "Reseñar",
            onClick = {},
            fontSize = 12.sp,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        )
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
