package com.example.thestudents.ui.screens.studentDetail

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R
import com.example.thestudents.data.Review
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.screens.profile.components.ProfileHeader
import com.example.thestudents.ui.screens.profile.components.ProfileTab
import com.example.thestudents.ui.screens.profile.components.ProfileTabs
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
    selectedTab: ProfileTab,
    onTabSelected: (ProfileTab) -> Unit,
    onFollowClick: () -> Unit,
    onReviewClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    onStudentClick: (String) -> Unit = {},
    onFollowersClick: () -> Unit = {},
    onFollowingClick: () -> Unit = {},
    reviewsCount: Int = student.reviewsCount,
    followed: Boolean = false
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item { UserInfoSection(student = student) }
        item {
            StatsSection(
                student = student,
                followersCount = student.followersCount,
                followingCount = student.followingCount,
                reviewsCount = reviewsCount,
                onFollowersClick = onFollowersClick,
                onFollowingClick = onFollowingClick
            )
        }
        item {
            ButtonWithIcon(
                //  si ya lo sigo muestro "Dejar de seguir", si no "Seguir"
                text = if (followed) stringResource(R.string.dejar_de_seguir) else stringResource(R.string.seguir),
                icon = if (followed) Icons.Default.PersonRemove else Icons.Default.PersonAdd,
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
            ProfileTabs(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected
            )
        }
        if (reviews.isEmpty()) {
            item {
                Text(
                    text = if (selectedTab == ProfileTab.RECEIVED) {
                        stringResource(R.string.no_tiene_recibidas_resenas_aun)
                    } else {
                        stringResource(R.string.no_tiene_escritas_resenas_aun)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(reviews, key = { it.id }) { review ->
                ReviewItem(
                    review = review,
                    isWrittenTab = selectedTab == ProfileTab.WRITTEN,
                    showActions = false,
                    onClick = { onReviewClick(review.id) },
                    onStudentClick = onStudentClick
                )
            }
        }
    }
}

@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun BodyStudentDetailPreview() {
    TheStudentsTheme {
        var selectedTab by rememberSaveable { mutableStateOf(ProfileTab.RECEIVED) }
        BodyStudentDetail(
            student = localStudentProvider.students[1],
            reviews = localReviewsProvider.allReviews,
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            onFollowClick = {},
            onReviewClick = {}
        )
    }
}

/**
 * Pantalla de detalle de estudiante con ViewModel (MVVM).
 */
@Composable
fun StudentDetailScreen(
    studentDetailViewModel: StudentDetailViewModel,
    studentId: String,
    onBackClick: () -> Unit,
    onReviewClick: (String) -> Unit,
    onStudentClick: (String) -> Unit,
    onFollowersClick: (String) -> Unit,
    onFollowingClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by studentDetailViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        studentDetailViewModel.getStudentById(studentId)
    }

    Column(modifier = modifier.fillMaxSize()) {
        ProfileHeader(onBackClick = onBackClick)

        val userProfile = state.userProfile
        when {
            state.isLoading && userProfile == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            userProfile == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.errorMessage ?: stringResource(R.string.estudiante_no_encontrado),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> {
                val student = userProfile.student
                val reviews = if (state.selectedTab == ProfileTab.RECEIVED) userProfile.receivedReviews else userProfile.createdReviews

                BodyStudentDetail(
                    student = student,
                    reviews = reviews,
                    selectedTab = state.selectedTab,
                    onTabSelected = studentDetailViewModel::onTabSelected,
                    onFollowClick = { studentDetailViewModel.followOrUnfollowStudent(studentId) },
                    onReviewClick = onReviewClick,
                    onStudentClick = onStudentClick,
                    onFollowersClick = { onFollowersClick(student.id) },
                    onFollowingClick = { onFollowingClick(student.id) },
                    reviewsCount = state.reviewsCount,
                    followed = userProfile.followed
                )
            }
        }
    }
}

@Preview(name = "Claro", showBackground = true, showSystemUi = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun StudentDetailScreenPreview() {
    TheStudentsTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            ProfileHeader(onBackClick = {})
            BodyStudentDetail(
                student = localStudentProvider.students[1],
                reviews = localReviewsProvider.allReviews,
                selectedTab = ProfileTab.RECEIVED,
                onTabSelected = {},
                onFollowClick = {},
                onReviewClick = {}
            )
        }
    }
}
