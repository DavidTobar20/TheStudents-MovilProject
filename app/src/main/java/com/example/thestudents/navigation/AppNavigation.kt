package com.example.thestudents.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.example.thestudents.ui.screens.commentsReview.CommentsReviewScreen
import com.example.thestudents.ui.screens.editarPerfil.EditarPerfilScreen
import com.example.thestudents.ui.screens.home.HomeScreen
import com.example.thestudents.ui.screens.login.LoginScreen
import com.example.thestudents.ui.screens.notifications.NotificationsScreen
import com.example.thestudents.ui.screens.profile.ProfileScreen
import com.example.thestudents.ui.screens.register.RegisterScreen
import com.example.thestudents.ui.screens.reviews.ReviewsScreen
import com.example.thestudents.ui.screens.search.SearchScreen
import com.example.thestudents.ui.screens.studentDetail.StudentDetailScreen
import com.example.thestudents.ui.screens.writeReview.WriteReviewScreen

// --- CONSTANTES DE NAVEGACIÓN ---
const val STUDENT_ID_ARG = "studentId"
const val REVIEW_ID_ARG = "reviewId"


// --- ESTRUCTURA DE RUTAS ---
sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object Search : Screen("search")
    data object Notifications : Screen("notifications")
    data object Profile : Screen("profile")
    data object EditProfile : Screen("edit_profile")
    data object Reviews : Screen("reviews")

    data object WriteReview : Screen("write_review/{$STUDENT_ID_ARG}") {
        fun createRoute(studentId: String) = "write_review/$studentId"
    }

    data object StudentDetail : Screen("student_detail/{$STUDENT_ID_ARG}") {
        fun createRoute(studentId: String) = "student_detail/$studentId"
    }

    data object CommentsReview : Screen("comments_review/{$REVIEW_ID_ARG}") {
        fun createRoute(reviewId: String) = "comments_review/$reviewId"
    }
}


/** Determina qué pestaña debe marcarse como activa en la barra inferior. */
fun selectedTabFor(route: String?): String? = when (route) {
    Screen.EditProfile.route,
    Screen.CommentsReview.route,
    Screen.StudentDetail.route,
    Screen.WriteReview.route -> Screen.Profile.route
    else -> route
}

// --- COMPOSABLE PRINCIPAL DE NAVEGACIÓN ---

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        authGraph(navController)
        mainGraph(navController)
    }
}

// --- NAVEGACIÓN ENTRE TABS ---

fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

// --- GRAFO DE AUTENTICACIÓN ---
private fun NavGraphBuilder.authGraph(navController: NavHostController) {
    composable(Screen.Login.route) {
        LoginScreen(
            onLoginSuccess = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            },
            onCreateAccountClick = { navController.navigate(Screen.Register.route) }
        )
    }

    composable(Screen.Register.route) {
        RegisterScreen(
            onRegisterClick = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            },
            onSsoClick = { /* Pendiente */ },
            onNavigateToLogin = { navController.popBackStack() }
        )
    }
}


// --- GRAFO PRINCIPAL DE LA APLICACIÓN ---
private fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    composable(Screen.Home.route) {
        HomeScreen(
            onReviewClick = { id -> navController.navigate(Screen.CommentsReview.createRoute(id)) },
            onStudentClick = { id -> navController.navigate(Screen.StudentDetail.createRoute(id)) }
        )
    }

    composable(Screen.Search.route) {
        SearchScreen(
            onStudentClick = { id -> navController.navigate(Screen.StudentDetail.createRoute(id)) }
        )
    }

    composable(Screen.Notifications.route) {
        NotificationsScreen(
            onStudentClick = { id -> navController.navigate(Screen.StudentDetail.createRoute(id)) }
        )
    }

    composable(Screen.Reviews.route) {
        ReviewsScreen(
            onStudentClick = { id -> navController.navigate(Screen.StudentDetail.createRoute(id)) },
            onWriteReviewClick = { id -> navController.navigate(Screen.WriteReview.createRoute(id)) }
        )
    }

    composable(
        route = Screen.WriteReview.route,
        arguments = listOf(navArgument(STUDENT_ID_ARG) { type = NavType.StringType })
    ) {
        val studentId = it.arguments?.getString(STUDENT_ID_ARG) ?: ""
        WriteReviewScreen(
            studentId = studentId,
            onBackClick = { navController.popBackStack() },
            onStudentClick = { id -> navController.navigate(Screen.StudentDetail.createRoute(id)) }
        )
    }

    composable(Screen.Profile.route) {
        ProfileScreen(
            onBackClick = { navController.popBackStack() },
            onEditProfileClick = { navController.navigate(Screen.EditProfile.route) },
            onReviewClick = { id -> navController.navigate(Screen.CommentsReview.createRoute(id)) }
        )
    }

    composable(Screen.EditProfile.route) {
        EditarPerfilScreen(
            onCancelClick = { navController.popBackStack() },
            onSaveClick = { navController.popBackStack() }
        )
    }

    composable(
        route = Screen.StudentDetail.route,
        arguments = listOf(navArgument(STUDENT_ID_ARG) { type = NavType.StringType })
    ) {
        val studentId = it.arguments?.getString(STUDENT_ID_ARG) ?: ""
        StudentDetailScreen(
            studentId = studentId,
            onBackClick = { navController.popBackStack() },
            onReviewClick = { id -> navController.navigate(Screen.CommentsReview.createRoute(id)) }
        )
    }

    composable(
        route = Screen.CommentsReview.route,
        arguments = listOf(navArgument(REVIEW_ID_ARG) { type = NavType.StringType })
    ) {
        val reviewId = it.arguments?.getString(REVIEW_ID_ARG) ?: ""
        CommentsReviewScreen(
            reviewId = reviewId,
            onBackClick = { navController.popBackStack() }
        )
    }
}