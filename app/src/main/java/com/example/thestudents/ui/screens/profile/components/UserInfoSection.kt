package com.example.thestudents.ui.screens.profile.components

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.BorderStroke
import com.example.thestudents.ui.components.GoldRingAvatar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ProfileIcon

// Estilo Threads: nombre, usuario y carrera a la izquierda, foto a la derecha; biografia y correo debajo
@Composable
fun UserInfoSection(
    student: Student,
    modifier: Modifier = Modifier,
    email: String? = null // solo se muestra en el perfil propio
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.name,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    lineHeight = 30.sp
                )
                if (student.username.isNotBlank()) {
                    Text(
                        text = "@${student.username}",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Capsula de carrera y semestre con borde fino
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                    modifier = Modifier.wrapContentSize()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.School, null, Modifier.size(15.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.programa_y_semestre, student.program, student.semester),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            GoldRingAvatar(
                initials = student.initials,
                profileImage = student.profileImage,
                backgroundColor = student.profileColor,
                size = 84.dp,
                fontSize = 30.sp
            )
        }

        if (student.bio.isNotBlank()) {
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = student.bio,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
            )
        }

        if (!email.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = email,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserInfoSectionPreview() {
    TheStudentsTheme {
        UserInfoSection(
            student = localStudentProvider.currentUser.copy(
                profileImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRCq6qha5YiJYI4ZIs3Sug9cpBKz23j-X5kWIMC6qU0jA&s=10"
            )
        )
    }
}
