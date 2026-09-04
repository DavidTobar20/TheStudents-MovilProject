package com.example.thestudents.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.thestudents.R
import com.example.thestudents.ui.screens.commentsReview.CommentsReviewScreen
import com.example.thestudents.ui.screens.commentsReview.CommentsReviewViewModel
import com.example.thestudents.ui.screens.editarPerfil.EditarPerfilScreen
import com.example.thestudents.ui.screens.editarPerfil.EditarPerfilViewModel
import com.example.thestudents.ui.screens.home.HomeScreen
import com.example.thestudents.ui.screens.login.LoginScreen
import com.example.thestudents.ui.screens.login.LoginViewModel
import com.example.thestudents.ui.screens.notifications.NotificationsScreen
import com.example.thestudents.ui.screens.notifications.NotificationsViewModel
import com.example.thestudents.ui.screens.profile.ProfileScreen
import com.example.thestudents.ui.screens.profile.ProfileViewModel
import com.example.thestudents.ui.screens.register.RegisterScreen
import com.example.thestudents.ui.screens.register.RegisterViewModel
import com.example.thestudents.ui.screens.reviews.ReviewsScreen
import com.example.thestudents.ui.screens.reviews.ReviewsViewModel
import com.example.thestudents.ui.screens.search.SearchScreen
import com.example.thestudents.ui.screens.search.SearchViewModel
import com.example.thestudents.ui.screens.studentDetail.StudentDetailScreen
import com.example.thestudents.ui.screens.studentDetail.StudentDetailViewModel
import com.example.thestudents.ui.screens.writeReview.WriteReviewScreen
import com.example.thestudents.ui.screens.writeReview.WriteReviewViewModel

// --- CONSTANTES DE NAVEGACIÓN ---
const val STUDENT_ID_ARG = "studentId"
const val REVIEW_ID_ARG = "reviewId"

/** Determina qué pestaña debe marcarse como activa en la barra inferior. */
fun selectedTabFor(route: String?): String? = when (route) {
    Screen.EditProfile.route,
    Screen.CommentsReview.route,
    Screen.StudentDetail.route,
    Screen.WriteReview.route -> Screen.Profile.route
    else -> route
}

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

/**
 * Estructura para los items de la barra de navegacion inferior.
 */
data class BottomNavItem(
    val iconFilled: ImageVector,
    val iconOutline: ImageVector,
    val labelRes: Int,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem(Icons.Filled.Home, Icons.Outlined.Home, R.string.inicio, Screen.Home.route),
    BottomNavItem(Icons.Filled.Search, Icons.Outlined.Search, R.string.explorar, Screen.Search.route),
    BottomNavItem(Icons.Filled.Notifications, Icons.Outlined.Notifications, R.string.notificaciones, Screen.Notifications.route),
    BottomNavItem(Icons.Filled.Person, Icons.Outlined.Person, R.string.perfil, Screen.Profile.route)
)

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
        val loginViewModel: LoginViewModel = viewModel()
        LoginScreen(
            onLoginSuccess = {
                navController.navigateToTab(Screen.Home.route)
            },
            onCreateAccountClick = { navController.navigate(Screen.Register.route) },
            loginViewModel = loginViewModel
        )
    }

    composable(Screen.Register.route) {
        val registerViewModel: RegisterViewModel = viewModel()
        RegisterScreen(
            onRegisterClick = {
                navController.navigateToTab(Screen.Home.route)
            },
            onSsoClick = { /* Pendiente */ },
            onNavigateToLogin = { navController.popBackStack() },
            registerViewModel = registerViewModel
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
        val searchViewModel: SearchViewModel = viewModel()
        SearchScreen(
            searchViewModel = searchViewModel,
            onStudentClick = { id -> navController.navigate(Screen.StudentDetail.createRoute(id)) }
        )
    }

    composable(Screen.Notifications.route) {
        val notificationsViewModel: NotificationsViewModel = viewModel()
        NotificationsScreen(
            onStudentClick = { id -> navController.navigate(Screen.StudentDetail.createRoute(id)) },
            notificationsViewModel = notificationsViewModel
        )
    }

    composable(Screen.Reviews.route) {
        val reviewsViewModel: ReviewsViewModel = viewModel()
        ReviewsScreen(
            reviewsViewModel = reviewsViewModel,
            onStudentClick = { id -> navController.navigate(Screen.StudentDetail.createRoute(id)) },
            onWriteReviewClick = { id -> navController.navigate(Screen.WriteReview.createRoute(id)) }
        )
    }

    composable(
        route = Screen.WriteReview.route,
        arguments = listOf(navArgument(STUDENT_ID_ARG) { type = NavType.StringType })
    ) { backStackEntry ->
        val studentId = backStackEntry.arguments?.getString(STUDENT_ID_ARG) ?: ""
        val writeReviewViewModel: WriteReviewViewModel = viewModel()
        WriteReviewScreen(
            writeReviewViewModel = writeReviewViewModel,
            studentId = studentId,
            onBackClick = { navController.popBackStack() },
            onStudentClick = { id -> navController.navigate(Screen.StudentDetail.createRoute(id)) }
        )
    }

    composable(Screen.Profile.route) {
        val profileViewModel: ProfileViewModel = viewModel()
        ProfileScreen(
            onBackClick = { navController.popBackStack() },
            onEditProfileClick = { navController.navigate(Screen.EditProfile.route) },
            onReviewClick = { id -> navController.navigate(Screen.CommentsReview.createRoute(id)) },
            profileViewModel = profileViewModel
        )
    }

    composable(Screen.EditProfile.route) {
        val editarPerfilViewModel : EditarPerfilViewModel  = viewModel()
        EditarPerfilScreen(
            editarPerfilViewModel = editarPerfilViewModel,
            onBackClick = { navController.popBackStack() },
            onSaveClick = { navController.popBackStack() }
        )
    }

    composable(
        route = Screen.StudentDetail.route,
        arguments = listOf(navArgument(STUDENT_ID_ARG) { type = NavType.StringType })
    ) { backStackEntry ->
        val studentId = backStackEntry.arguments?.getString(STUDENT_ID_ARG) ?: ""
        val studentDetailViewModel: StudentDetailViewModel = viewModel()
        StudentDetailScreen(
            studentDetailViewModel = studentDetailViewModel,
            studentId = studentId,
            onBackClick = { navController.popBackStack() },
            onReviewClick = { id -> navController.navigate(Screen.CommentsReview.createRoute(id)) }
        )
    }

    composable(
        route = Screen.CommentsReview.route,
        arguments = listOf(navArgument(REVIEW_ID_ARG) { type = NavType.StringType })
    ) { backStackEntry ->
        val reviewId = backStackEntry.arguments?.getString(REVIEW_ID_ARG) ?: ""
        val commentsReviewViewModel : CommentsReviewViewModel = viewModel()
        CommentsReviewScreen(
            commentsReviewViewModel = commentsReviewViewModel,
            reviewId = reviewId,
            onBackClick = { navController.popBackStack() }
        )
    }
}
