package com.example.thestudents.ui.screens.reviews

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thestudents.R
import com.example.thestudents.data.CourseSection
import com.example.thestudents.data.local.localCourseSectionProvider
import com.example.thestudents.ui.screens.reviews.components.CourseSectionCard
import com.example.thestudents.ui.screens.reviews.components.EnrollSubjectDialog
import com.example.thestudents.ui.screens.reviews.components.HeaderReviews
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ButtonWithIcon

@Composable
fun BodyReviews(
    sections: List<CourseSection>,
    onStudentClick: (String) -> Unit,
    onWriteReviewClick: (String) -> Unit,
    onEnrollClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // El margen va en contentPadding para que las sombras de las tarjetas no se corten
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp)
    ) {
        item {
            HeaderReviews()
            Spacer(modifier = Modifier.height(8.dp))
            ButtonWithIcon(
                text = stringResource(R.string.inscribir_materia),
                icon = Icons.Default.Add,
                onClick = onEnrollClick,
                borderColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (sections.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, start = 8.dp, end = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.sin_inscripciones),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        items(sections, key = { "${it.title}_${it.period}" }) { section ->
            CourseSectionCard(
                section = section,
                onStudentClick = onStudentClick,
                onWriteReviewClick = onWriteReviewClick
            )
        }
    }
}

@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun BodyReviewsPreview() {
    TheStudentsTheme {
        Surface {
            BodyReviews(
                sections = localCourseSectionProvider.sections.take(1),
                onStudentClick = {},
                onWriteReviewClick = {}
            )
        }
    }
}

@Composable
fun ReviewsScreen(
    onStudentClick: (String) -> Unit,
    onWriteReviewClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    reviewsViewModel: ReviewsViewModel
) {
    val state by reviewsViewModel.uiState.collectAsState()

    if (state.showEnrollDialog) {
        EnrollSubjectDialog(
            catalogoMaterias = state.catalogoMaterias,
            materiasYaInscritas = state.materiasYaInscritas,
            selectedMateria = state.selectedMateria,
            selectedPeriodo = state.selectedPeriodo,
            isEnrolling = state.isEnrolling,
            errorMessage = state.enrollErrorMessage,
            onMateriaSelected = { reviewsViewModel.onMateriaSelected(it) },
            onPeriodoSelected = { reviewsViewModel.onPeriodoSelected(it) },
            onConfirm = { reviewsViewModel.inscribirMateria() },
            onDismiss = { reviewsViewModel.closeEnrollDialog() }
        )
    }

    when {
        state.isLoading -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        state.errorMessage != null -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.errorMessage ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
        else -> {
            BodyReviews(
                sections = state.sections,
                onStudentClick = onStudentClick,
                onWriteReviewClick = onWriteReviewClick,
                onEnrollClick = { reviewsViewModel.openEnrollDialog() },
                modifier = modifier
            )
        }
    }
}

@Preview(name = "Claro", showBackground = true, showSystemUi = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun ReviewsScreenPreview() {
    TheStudentsTheme {
        Surface {
            ReviewsScreen(
                reviewsViewModel = viewModel(),
                onStudentClick = {},
                onWriteReviewClick = {}
            )
        }
    }
}
