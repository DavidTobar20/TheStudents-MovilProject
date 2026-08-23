package com.example.thestudents.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.ui.Modifier
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.ui.screens.EditarPerfil.EditarPerfilScreen
import com.example.thestudents.ui.screens.commentsReview.CommentsReviewScreen
import com.example.thestudents.ui.screens.home.HomeScreen
import com.example.thestudents.ui.screens.login.LoginScreen
import com.example.thestudents.ui.screens.notifications.NotificationsScreen
import com.example.thestudents.ui.screens.profile.ProfileScreen
import com.example.thestudents.ui.screens.register.RegisterScreen
import com.example.thestudents.ui.screens.reviews.ReviewsScreen
import com.example.thestudents.ui.screens.search.SearchScreen
import com.example.thestudents.ui.screens.studentDetail.StudentDetailScreen
import com.example.thestudents.ui.utils.FixedBottomBar

/**
 * Shell de la aplicacion: el unico Scaffold que existe.
 */
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (currentRoute in Routes.withBottomBar) {
                FixedBottomBar(
                    selectedRoute = Routes.selectedTabFor(currentRoute),
                    onNavigate = navController::navigateToTab
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            authGraph(navController)
            mainGraph(navController)
        }
    }
}

/**
 * Navegacion entre pestanas.
 */
private fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

private fun NavGraphBuilder.authGraph(navController: NavHostController) {
    composable(Routes.Login.route) {
        LoginScreen(
            onLoginSuccess = { navController.navigateToTab(Routes.Home.route) },
            onCreateAccountClick = { navController.navigate(Routes.Register.route) }
        )
    }

    composable(Routes.Register.route) {
        RegisterScreen(
            onRegisterClick = { navController.navigateToTab(Routes.Home.route) },
            onSsoClick = { /* Pendiente */ },
            onNavigateToLogin = { navController.popBackStack() }
        )
    }
}

private fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    composable(Routes.Home.route) {
        HomeScreen(
            onReviewClick = { index -> navController.navigate(Routes.CommentsReview.createRoute(index)) }
        )
    }

    composable(Routes.Search.route) {
        SearchScreen(
            onStudentClick = { id -> navController.navigate(Routes.StudentDetail.createRoute(id)) }
        )
    }

    composable(Routes.Notifications.route) {
        NotificationsScreen(
            onStudentClick = { id -> navController.navigate(Routes.StudentDetail.createRoute(id)) }
        )
    }

    composable(Routes.Reviews.route) { ReviewsScreen() }

    composable(Routes.Profile.route) {
        ProfileScreen(
            onBackClick = { navController.popBackStack() },
            onEditProfileClick = { navController.navigate(Routes.EditProfile.route) },
            onReviewClick = { index -> navController.navigate(Routes.CommentsReview.createRoute(index)) }
        )
    }

    composable(Routes.EditProfile.route) {
        EditarPerfilScreen(
            onCancelClick = { navController.popBackStack() },
            onSaveClick = { navController.popBackStack() }
        )
    }

    composable(
        route = Routes.StudentDetail.route,
        arguments = listOf(navArgument(Routes.STUDENT_ID_ARG) { type = NavType.StringType })
    ) { backStackEntry ->
        val studentId = backStackEntry.arguments?.getString(Routes.STUDENT_ID_ARG) ?: ""
        StudentDetailScreen(
            studentId = studentId,
            onBackClick = { navController.popBackStack() },
            onReviewClick = { index -> navController.navigate(Routes.CommentsReview.createRoute(index)) }
        )
    }

    composable(
        route = Routes.CommentsReview.route,
        arguments = listOf(navArgument(Routes.REVIEW_INDEX_ARG) { type = NavType.IntType })
    ) { backStackEntry ->
        val reviewIndex = backStackEntry.arguments?.getInt(Routes.REVIEW_INDEX_ARG) ?: 0
        val review = localReviewsProvider.allReviews.getOrElse(reviewIndex) {
            localReviewsProvider.allReviews.first()
        }

        CommentsReviewScreen(
            review = review,
            onBackClick = { navController.popBackStack() }
        )
    }
}
