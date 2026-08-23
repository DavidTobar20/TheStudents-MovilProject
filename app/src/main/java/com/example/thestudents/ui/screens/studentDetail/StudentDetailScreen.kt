package com.example.thestudents.ui.screens.studentDetail

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.data.Review
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.screens.profile.components.ProfileHeader
import com.example.thestudents.ui.screens.profile.components.RatingChartSection
import com.example.thestudents.ui.screens.profile.components.ReviewItem
import com.example.thestudents.ui.screens.profile.components.StatsSection
import com.example.thestudents.ui.screens.profile.components.UserInfoSection
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ButtonWithIcon

/**
 * Contenido del detalle del estudiante.
 */
@Composable
fun BodyStudentDetail(
    student: Student,
    reviews: List<Review>,
    onBackClick: () -> Unit,
    onFollowClick: () -> Unit,
    onReviewClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item { ProfileHeader(onBackClick = onBackClick) }
        item { UserInfoSection(student = student) }
        item { StatsSection(student = student) }
        item {
            ButtonWithIcon(
                text = stringResource(R.string.seguir_mayuscula),
                icon = Icons.Default.PersonAdd,
                onClick = onFollowClick,
                borderColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .height(48.dp)
                    .padding(horizontal = 24.dp)
            )
        }
        item { RatingChartSection() }
        item {
            Text(
                text = stringResource(R.string.resenas_mayuscula),
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        itemsIndexed(reviews) { index, review ->
            ReviewItem(
                review = review,
                onClick = { onReviewClick(index) }
            )
        }
    }
}

@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun BodyStudentDetailPreview() {
    TheStudentsTheme {
        Surface {
            BodyStudentDetail(
                student = localStudentProvider.students[1],
                reviews = localReviewsProvider.allReviews,
                onBackClick = {},
                onFollowClick = {},
                onReviewClick = {}
            )
        }
    }
}

/**
 * Pantalla de detalle de estudiante. Recupera los datos por ID.
 */
@Composable
fun StudentDetailScreen(
    studentId: String,
    onBackClick: () -> Unit,
    onReviewClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val student = localStudentProvider.getStudentById(studentId)
    val reviews = localReviewsProvider.getReviewsForStudent(studentId)

    if (student != null) {
        BodyStudentDetail(
            student = student,
            reviews = reviews,
            onBackClick = onBackClick,
            onFollowClick = { /* Handle follow */ },
            onReviewClick = onReviewClick,
            modifier = modifier
        )
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text(text = "Estudiante no encontrado")
        }
    }
}
