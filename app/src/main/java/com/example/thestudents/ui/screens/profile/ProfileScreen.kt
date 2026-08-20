package com.example.thestudents.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.R
import com.example.thestudents.ui.utils.FixedBottomBar
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.screens.profile.components.ProfileHeader
import com.example.thestudents.ui.screens.profile.components.ProfileTabs
import com.example.thestudents.ui.screens.profile.components.RatingChartSection
import com.example.thestudents.ui.screens.profile.components.ReviewItem
import com.example.thestudents.ui.screens.profile.components.StatsSection
import com.example.thestudents.ui.screens.profile.components.UserInfoSection
import com.example.thestudents.ui.utils.ButtonWithIcon


@Composable
fun BodyProfile(
    student: Student,
    onBackClick: () -> Unit,
    onEditProfileClick: () -> Unit,
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
            text = stringResource(R.string.editar_perfil),
            icon = Icons.Outlined.Edit,
            onClick = onEditProfileClick,
            borderColor = colorResource(R.color.dark_green),
            contentColor = colorResource(R.color.dark_green),
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
    BodyProfile(student = localStudentProvider.currentUser, onBackClick = {}, onEditProfileClick = {})
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController()
) {
    val currentUser = localStudentProvider.currentUser

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colorResource(R.color.cream),
        bottomBar = { FixedBottomBar(navController, "profile") }
    ) { padding ->
        BodyProfile(
            student = currentUser,
            onBackClick = { navController.popBackStack() },
            onEditProfileClick = { navController.navigate("edit_profile") },
            contentPadding = padding
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FullProfileScreenPreview() {
    ProfileScreen()
}
