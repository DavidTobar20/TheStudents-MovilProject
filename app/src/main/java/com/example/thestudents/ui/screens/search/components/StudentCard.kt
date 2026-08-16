package com.example.thestudents.ui.screens.search.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.data.Student
import com.example.thestudents.ui.utils.ButtonWithoutIcon
import com.example.thestudents.ui.utils.ProfileIcon

@Composable
fun StudentCard(
    student: Student,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileIcon(
                initials = student.initials,
                profileImageRes = student.profileImageRes,
                backgroundColor = student.profileColor,
                contentColor = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.size(56.dp)
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
                    text = "${student.program} · ${student.reviewsCount} reseñas",
                    fontSize = 13.sp,
                    color = colorResource(R.color.medium_green).copy(alpha = 0.6f)
                )
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (index < student.rating.toInt()) colorResource(R.color.gold) else colorResource(R.color.sage).copy(alpha = 0.3f)
                        )
                    }
                }
            }

            ButtonWithoutIcon(
                textoBoton = stringResource(R.string.seguir_mayuscula),
                onClick = {},
                fontSize = 11.sp,
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentCardPreview() {
    StudentCard(
        Student(
            id = "1",
            initials = "VT",
            name = "Valentina Torres",
            program = "Psicología",
            reviewsCount = 28,
            rating = 5f,
            profileColor = Color(0xFF7B5CAB),
            email = "valentina@u.edu.co",
            semester = 5,
            bio = "Estudiante de psicología"
        )
    )
}
