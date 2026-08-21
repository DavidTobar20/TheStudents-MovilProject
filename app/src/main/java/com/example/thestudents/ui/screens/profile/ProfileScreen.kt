package com.example.thestudents.ui.screens.profile

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.R
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.screens.profile.components.ProfileHeader
import com.example.thestudents.ui.screens.profile.components.ProfileTabs
import com.example.thestudents.ui.screens.profile.components.RatingChartSection
import com.example.thestudents.ui.screens.profile.components.ReviewItem
import com.example.thestudents.ui.screens.profile.components.StatsSection
import com.example.thestudents.ui.screens.profile.components.UserInfoSection
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ButtonWithIcon
import com.example.thestudents.ui.utils.FixedBottomBar


@Composable
fun BodyProfile(
    student: Student,
    onBackClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onReviewClick: (Int) -> Unit,
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
            borderColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .height(48.dp)
                .padding(horizontal = 24.dp)
        ) }
        item { RatingChartSection() }
        item { ProfileTabs() }
        item { 
            ReviewItem(
                review = localReviewsProvider.allReviews[0],
                onClick = { onReviewClick(0) }
            ) 
        }
        item { 
            ReviewItem(
                review = localReviewsProvider.allReviews[1],
                onClick = { onReviewClick(1) }
            ) 
        }
    }
}

@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun BodyProfilePreview() {
    TheStudentsTheme {
        Surface {
            BodyProfile(
                student = localStudentProvider.currentUser, 
                onBackClick = {}, 
                onEditProfileClick = {},
                onReviewClick = {}
            )
        }
    }
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
            onEditProfileClick = { navController.navigate("edit_profile") },
            onReviewClick = { index -> navController.navigate("comments_review/$index") },
            contentPadding = padding
        )
    }
}

@Preview(name = "Claro", showBackground = true, showSystemUi = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun FullProfileScreenPreview() {
    TheStudentsTheme {
        Surface {
            ProfileScreen()
        }
    }
}
