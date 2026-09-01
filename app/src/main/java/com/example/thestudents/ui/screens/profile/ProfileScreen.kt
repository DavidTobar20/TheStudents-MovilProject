package com.example.thestudents.ui.screens.profile

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
 * Contenido del perfil. Sin estado propio: la pestana activa y las resenas llegan desde arriba.
 */
@Composable
fun BodyProfile(
    student: Student,
    reviews: List<Review>,
    selectedTab: ProfileTab,
    onTabSelected: (ProfileTab) -> Unit,
    onBackClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onReviewClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        ProfileHeader(onBackClick = onBackClick)
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item { UserInfoSection(student = student) }
            item { StatsSection(student = student) }
            item {
                ButtonWithIcon(
                    text = stringResource(R.string.editar_perfil),
                    icon = Icons.Outlined.Edit,
                    onClick = onEditProfileClick,
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
            items(reviews) { review ->
                ReviewItem(
                    review = review,
                    onClick = { onReviewClick(review.id) }
                )
            }
        }
    }
}

@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun BodyProfilePreview() {
    TheStudentsTheme {
        Surface {
            var selectedTab by rememberSaveable { mutableStateOf(ProfileTab.RECEIVED) }
            val student = localStudentProvider.currentUser
            BodyProfile(
                student = student,
                reviews = localReviewsProvider.getReviewsForStudent(student.id),
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                onBackClick = {},
                onEditProfileClick = {},
                onReviewClick = {}
            )
        }
    }
}

/**
 * Pantalla de perfil. Guarda la pestana activa y traduce los gestos a llamadas de navegacion,
 * que le llegan como lambdas: no conoce el NavController.
 */
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onReviewClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val state by profileViewModel.uiState.collectAsState()
    
    // Si el estudiante es nulo (cargando), podríamos mostrar un placeholder o cargador
    // Pero asumiendo que localStudentProvider.currentUser siempre existe:
    state.student?.let { student ->
        BodyProfile(
            student = student,
            reviews = state.reviews,
            selectedTab = state.selectedTab,
            onTabSelected = { profileViewModel.onTabSelected(it) },
            onBackClick = onBackClick,
            onEditProfileClick = onEditProfileClick,
            onReviewClick = onReviewClick,
            modifier = modifier
        )
    }
}

@Preview(name = "Claro", showBackground = true, showSystemUi = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun FullProfileScreenPreview() {
    TheStudentsTheme {
        Surface {
            ProfileScreen(
                onBackClick = {},
                onEditProfileClick = {},
                onReviewClick = {}
            )
        }
    }
}
