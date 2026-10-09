package com.example.thestudents.ui.screens.updateReview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.thestudents.ui.screens.updateReview.components.UpdateReviewAnonymousCard
import com.example.thestudents.ui.screens.updateReview.components.UpdateReviewHeader
import com.example.thestudents.ui.screens.writeReview.components.RatingCard
import com.example.thestudents.ui.screens.writeReview.components.ReviewField
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ButtonWithoutIcon
import com.example.thestudents.ui.utils.HeaderBack

@Composable
fun BodyUpdateReviewScreen(
    rating: Int,
    onRatingSelected: (Int) -> Unit,
    reviewContent: String,
    onReviewContentChange: (String) -> Unit,
    isAnonymous: Boolean,
    onAnonymousChange: (Boolean) -> Unit,
    nameReviewed: String,
    initialsReviewed: String,
    courseInfoReviewed: String,
    profileImageReviewed: String?,
    onUpdateClick: () -> Unit,
    onBackClick: () -> Unit,
    onStudentClick: () -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        HeaderBack(
            title = stringResource(R.string.actualizar_resena),
            onBackClick = onBackClick
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            UpdateReviewHeader(
                name = nameReviewed,
                initials = initialsReviewed,
                courseInfo = courseInfoReviewed,
                profileImage = profileImageReviewed,
                onAvatarClick = onStudentClick
            )
            RatingCard(
                rating = rating,
                onRatingSelected = onRatingSelected
            )
            ReviewField(
                review = reviewContent,
                onReviewChange = onReviewContentChange
            )
            UpdateReviewAnonymousCard(
                isAnonymous = isAnonymous,
                onAnonymousChange = onAnonymousChange
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            ButtonWithoutIcon(
                textoBoton = stringResource(R.string.actualizar_resena_mayuscula),
                onClick = onUpdateClick,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp)
            )
        }
    }
}

@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun BodyUpdateReviewScreenPreview() {
    TheStudentsTheme {
        Surface {
            BodyUpdateReviewScreen(
                rating = 4,
                onRatingSelected = {},
                reviewContent = "Excelente compañero de trabajo, muy responsable y puntual.",
                onReviewContentChange = {},
                isAnonymous = false,
                onAnonymousChange = {},
                nameReviewed = "Valeria Gómez",
                initialsReviewed = "VG",
                courseInfoReviewed = "Matemáticas Discretas (MATE120) · 2025-3",
                profileImageReviewed = null,
                onUpdateClick = {},
                onBackClick = {},
                onStudentClick = {}
            )
        }
    }
}

@Composable
fun UpdateReviewScreen(
    updateReviewViewModel: UpdateReviewViewModel,
    reviewId: String,
    onBackClick: () -> Unit,
    onUpdateSuccess: () -> Unit,
    onStudentClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val state by updateReviewViewModel.uiState.collectAsState()

    LaunchedEffect(reviewId) {
        updateReviewViewModel.loadReview(reviewId)
    }

    val review = state.review
    when {
        state.isLoading && review == null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        review == null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.errorMessage ?: stringResource(R.string.resena_no_encontrada),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
        else -> {
            val reviewedStudent = review.reviewedStudent
            BodyUpdateReviewScreen(
                modifier = modifier,
                rating = state.rating,
                onRatingSelected = { updateReviewViewModel.updateRating(it) },
                reviewContent = state.reviewContent,
                onReviewContentChange = { updateReviewViewModel.updateReviewContent(it) },
                isAnonymous = state.isAnonymous,
                onAnonymousChange = { updateReviewViewModel.updateIsAnonymous(it) },
                nameReviewed = reviewedStudent.name,
                initialsReviewed = reviewedStudent.initials,
                courseInfoReviewed = "${review.classReviewed} · ${review.periodReviewed}",
                profileImageReviewed = reviewedStudent.profileImage,
                onUpdateClick = {
                    updateReviewViewModel.submitUpdateReview(onSuccess = onUpdateSuccess)
                },
                onBackClick = onBackClick,
                onStudentClick = { onStudentClick(reviewedStudent.id) },
                isLoading = state.isLoading
            )
        }
    }
}

@Preview(name = "Claro", showBackground = true, showSystemUi = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun UpdateReviewScreenPreview() {
    TheStudentsTheme {
        Surface {
            UpdateReviewScreen(
                updateReviewViewModel = viewModel(),
                reviewId = "1",
                onBackClick = {},
                onUpdateSuccess = {}
            )
        }
    }
}
