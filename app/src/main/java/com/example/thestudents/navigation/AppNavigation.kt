package com.example.thestudents.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.thestudents.R
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
import com.example.thestudents.ui.screens.writeReview.WriteReviewScreen
import com.example.thestudents.ui.utils.FixedBottomBar

// --- CONSTANTES Y LÓGICA DE NAVEGACIÓN (Nivel Superior) ---

const val STUDENT_ID_ARG = "studentId"
const val REVIEW_INDEX_ARG = "reviewIndex"

/** Determina si una ruta debe mostrar la barra inferior. */
fun shouldShowBottomBar(route: String?): Boolean {
    return route in listOf(
        Screen.Home.route, Screen.Search.route, Screen.Notifications.route, Screen.Profile.route,
        Screen.EditProfile.route, Screen.Reviews.route, Screen.CommentsReview.route, Screen.StudentDetail.route,
        Screen.WriteReview.route
    )
}

/** Determina que pestaña debe marcarse como activa. */
fun selectedTabFor(route: String?): String? = when (route) {
    Screen.EditProfile.route, Screen.CommentsReview.route, Screen.StudentDetail.route, Screen.WriteReview.route -> Screen.Profile.route
    else -> route
}

/**
 * Representacion de cada pantalla en la navegacion.
 */
sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object Search : Screen("search")
    data object Notifications : Screen("notifications")
    data object Profile : Screen("profile")
    data object EditProfile : Screen("edit_profile")
    data object Reviews : Screen("reviews")
    data object WriteReview : Screen("write_review/{studentId}") {
        fun createRoute(studentId: String) = "write_review/$studentId"
    }

    data object StudentDetail : Screen("student_detail/{$STUDENT_ID_ARG}") {
        fun createRoute(studentId: String) = "student_detail/$studentId"
    }

    data object CommentsReview : Screen("comments_review/{$REVIEW_INDEX_ARG}") {
        fun createRoute(reviewIndex: Int) = "comments_review/$reviewIndex"
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

// --- COMPONENTE PRINCIPAL DE NAVEGACIÓN ---

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
            if (shouldShowBottomBar(currentRoute)) {
                FixedBottomBar(
                    selectedRoute = selectedTabFor(currentRoute),
                    onNavigate = navController::navigateToTab
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            authGraph(navController)
            mainGraph(navController)
        }
    }
}

private fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

private fun NavGraphBuilder.authGraph(navController: NavHostController) {
    composable(Screen.Login.route) {
        LoginScreen(
            onLoginSuccess = { navController.navigateToTab(Screen.Home.route) },
            onCreateAccountClick = { navController.navigate(Screen.Register.route) }
        )
    }

    composable(Screen.Register.route) {
        RegisterScreen(
            onRegisterClick = { navController.navigateToTab(Screen.Home.route) },
            onSsoClick = { /* Pendiente */ },
            onNavigateToLogin = { navController.popBackStack() }
        )
    }
}

private fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    composable(Screen.Home.route) {
        HomeScreen(
            onReviewClick = { index -> navController.navigate(Screen.CommentsReview.createRoute(index)) },
            onStudentClick = { id -> navController.navigate(Screen.StudentDetail.createRoute(id)) }
        )
    }

    composable(Screen.Search.route) {
        SearchScreen(
            onStudentClick = { id -> navController.navigate(Screen.StudentDetail.createRoute(id)) },
            onWriteReviewClick = { id -> navController.navigate(Screen.WriteReview.createRoute(id)) }
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
        arguments = listOf(navArgument("studentId") { type = NavType.StringType })
    ) { backStackEntry ->
        val studentId = backStackEntry.arguments?.getString("studentId") ?: ""
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
            onReviewClick = { index -> navController.navigate(Screen.CommentsReview.createRoute(index)) }
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
    ) { backStackEntry ->
        val studentId = backStackEntry.arguments?.getString(STUDENT_ID_ARG) ?: ""
        StudentDetailScreen(
            studentId = studentId,
            onBackClick = { navController.popBackStack() },
            onReviewClick = { index -> navController.navigate(Screen.CommentsReview.createRoute(index)) }
        )
    }

    composable(
        route = Screen.CommentsReview.route,
        arguments = listOf(navArgument(REVIEW_INDEX_ARG) { type = NavType.IntType })
    ) { backStackEntry ->
        val reviewIndex = backStackEntry.arguments?.getInt(REVIEW_INDEX_ARG) ?: 0
        val review = localReviewsProvider.allReviews.getOrElse(reviewIndex) {
            localReviewsProvider.allReviews.first()
        }

        CommentsReviewScreen(
            review = review,
            onBackClick = { navController.popBackStack() }
        )
    }
}
