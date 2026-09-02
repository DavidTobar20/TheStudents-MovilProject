package com.example.thestudents.ui.screens.reviews

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.data.CourseSection
import com.example.thestudents.data.local.localCourseSectionProvider
import com.example.thestudents.ui.screens.reviews.components.CourseSectionCard
import com.example.thestudents.ui.screens.reviews.components.HeaderReviews
import com.example.thestudents.ui.theme.TheStudentsTheme


@Composable
fun BodyReviews(
    sections: List<CourseSection>,
    onStudentClick: (String) -> Unit,
    onWriteReviewClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
            HeaderReviews()
        }
        items(sections, key = { it.title }) { section ->
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

/**
 * Pantalla de resenas. No tiene estado propio; las secciones llegan como parametro para poder
 * cambiarlas en previews y pruebas sin tocar el proveedor.
 */
@Composable
fun ReviewsScreen(
    onStudentClick: (String) -> Unit,
    onWriteReviewClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    reviewsViewModel: ReviewsViewModel
) {
    val state by reviewsViewModel.uiState.collectAsState()

    BodyReviews(
        sections = state.sections,
        onStudentClick = onStudentClick,
        onWriteReviewClick = onWriteReviewClick,
        modifier = modifier
    )
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
