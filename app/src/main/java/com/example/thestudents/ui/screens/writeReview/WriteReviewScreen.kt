package com.example.thestudents.ui.screens.writeReview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thestudents.R
import com.example.thestudents.ui.screens.writeReview.components.RatingCard
import com.example.thestudents.ui.screens.writeReview.components.ReviewField
import com.example.thestudents.ui.screens.writeReview.components.ReviewHeader
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ButtonWithoutIcon
import com.example.thestudents.ui.utils.HeaderBack
import com.example.thestudents.ui.utils.SettingSwitchRow

@Composable
fun BodyWriteReviewScreen(
    modifier: Modifier = Modifier,
    rating: Int,
    onRatingSelected: (Int) -> Unit,
    onReviewChange: (String) -> Unit,
    onAnonymousChange: (Boolean) -> Unit,
    onStudentClick: () -> Unit,
    review: String,
    isAnonymous: Boolean,
    onPublishClick: () -> Unit,
    onBackClick: () -> Unit,
    nameReviewed: String,
    initialsReviewed: String,
    courseInfoReviewed: String,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        HeaderBack(
            title = stringResource(R.string.nueva_resena),
            onBackClick = onBackClick
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ReviewHeader(
                name = nameReviewed,
                initials = initialsReviewed,
                courseInfo = courseInfoReviewed,
                onAvatarClick = onStudentClick
            )
            RatingCard(
                rating = rating,
                onRatingSelected = onRatingSelected
            )
            ReviewField(
                review = review,
                onReviewChange = onReviewChange
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                SettingSwitchRow(
                    modifier = Modifier.padding(16.dp),
                    title = stringResource(R.string.publicar_de_forma_anonima),
                    description = stringResource(R.string.tu_nombre_no_aparecer_en_la_resena),
                    checked = isAnonymous,
                    onCheckedChange = onAnonymousChange
                )
            }
        }
        ButtonWithoutIcon(
            textoBoton = stringResource(R.string.publicar_resena),
            onClick = onPublishClick,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(48.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BodyWriteReviewScreenPreview() {
    TheStudentsTheme {
        BodyWriteReviewScreen(
            rating = 3,
            onRatingSelected = {},
            onReviewChange = {},
            onAnonymousChange = {},
            onStudentClick = {},
            review = "Esta es una reseña de prueba",
            isAnonymous = true,
            onPublishClick = {},
            onBackClick = {},
            nameReviewed = "Laura Martínez",
            initialsReviewed = "LM",
            courseInfoReviewed = "Estructuras de Datos (ISIS1206) • 2025-2",
        )
    }
}

@Composable
fun WriteReviewScreen(
    writeReviewViewModel: WriteReviewViewModel,
    studentId: String,
    onBackClick: () -> Unit,
    onStudentClick: (String) -> Unit,
    modifier: Modifier = Modifier

) {
    val state by writeReviewViewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        writeReviewViewModel.getStudentById(studentId)
    }

    if (state.student == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.estudiante_no_encontrado),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        BodyWriteReviewScreen(
            modifier = modifier,
            rating = state.rating,
            onRatingSelected = { writeReviewViewModel.updateRating(it) },
            onReviewChange = { writeReviewViewModel.updateReview(it) },
            onAnonymousChange = { writeReviewViewModel.updateIsAnonymous(it) },
            onStudentClick = { onStudentClick(state.student!!.id) },
            review = state.review,
            isAnonymous = state.isAnonymous,
            onPublishClick = { /* Handle publish */ },
            onBackClick = onBackClick,
            nameReviewed = state.student!!.name,
            initialsReviewed = state.student!!.initials,
            courseInfoReviewed = "${state.student!!.program} • ${state.student!!.period}",
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WriteReviewScreenPreview() {
    TheStudentsTheme {
        Surface {
            WriteReviewScreen(
                writeReviewViewModel = viewModel(),
                studentId = "1",
                onBackClick = {},
                onStudentClick = {},
            )
        }
    }
}
