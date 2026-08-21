package com.example.thestudents.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.ui.utils.FixedBottomBar
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.screens.profile.components.*
import com.example.thestudents.ui.utils.ButtonWithIcon
import com.example.thestudents.ui.theme.TheStudentsTheme


@Composable
fun BodyProfile(
    student: Student,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        item { ProfileHeader(onBackClick = onBackClick) }
        item { UserInfoSection(student = student) }
        item { StatsSection(student = student) }
        item { ButtonWithIcon(
            text = "EDITAR PERFIL",
            icon = Icons.Outlined.Edit,
            onClick = {},
            borderColor = MaterialTheme.colorScheme.primary,
            contentColor = if (androidx.compose.foundation.isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .height(48.dp)
                .padding(horizontal = 24.dp)
        ) }
        item { RatingChartSection() }
        item { ProfileTabs() }
        item { ReviewItem(review = com.example.thestudents.data.local.localReviewsProvider.allReviews[0]) }
        item { ReviewItem(review = com.example.thestudents.data.local.localReviewsProvider.allReviews[1]) }
    }
}

@Preview(showBackground = true)
@Composable
fun BodyProfilePreview() {
    BodyProfile(student = localStudentProvider.currentUser, onBackClick = {})
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController()
) {
    val currentUser = localStudentProvider.currentUser

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { FixedBottomBar(navController, "profile") }
    ) { padding ->
        BodyProfile(
            student = currentUser,
            onBackClick = { navController.popBackStack() },
            contentPadding = padding
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun FullProfileScreenPreview() {
    TheStudentsTheme(darkTheme = false) {
        ProfileScreen()
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FullProfileScreenDarkPreview() {
    TheStudentsTheme(darkTheme = true) {
        ProfileScreen()
    }
}
