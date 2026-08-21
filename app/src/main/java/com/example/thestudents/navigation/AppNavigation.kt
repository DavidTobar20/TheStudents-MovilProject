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
import com.example.thestudents.data.local.localCourseSectionProvider
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
import com.example.thestudents.ui.utils.FixedBottomBar

/**
 * Shell de la aplicacion: el unico Scaffold que existe.
 *
 * Antes cada pantalla montaba el suyo y repetia el color de fondo y la barra inferior, lo que
 * ademas producia Scaffolds anidados en la pantalla de comentarios. Ahora el Scaffold vive aqui
 * y las pantallas solo aportan contenido; el desplazamiento que dejan la barra inferior y las
 * barras del sistema llega al NavHost como padding, asi que ninguna pantalla vuelve a calcularlo.
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
            startDestination = Routes.LOGIN,
            modifier = Modifier.padding(innerPadding)
        ) {
            authGraph(navController)
            mainGraph(navController)
        }
    }
}

/**
 * Navegacion entre pestanas: una sola instancia de cada destino y el estado de cada pestana
 * se conserva al volver.
 */
private fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

private fun NavGraphBuilder.authGraph(navController: NavHostController) {
    composable(Routes.LOGIN) {
        LoginScreen(
            onLoginSuccess = { navController.navigateToTab(Routes.HOME) },
            onCreateAccountClick = { navController.navigate(Routes.REGISTER) }
        )
    }

    composable(Routes.REGISTER) {
        RegisterScreen(
            onRegisterClick = { navController.navigateToTab(Routes.HOME) },
            onSsoClick = { /* Pendiente: autenticacion institucional */ },
            onNavigateToLogin = { navController.popBackStack() }
        )
    }
}

private fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    composable(Routes.HOME) { HomeScreen() }

    composable(Routes.SEARCH) { SearchScreen() }

    composable(Routes.NOTIFICATIONS) { NotificationsScreen() }

    composable(Routes.REVIEWS) { ReviewsScreen(sections = localCourseSectionProvider.sections) }

    composable(Routes.PROFILE) {
        ProfileScreen(
            onBackClick = { navController.popBackStack() },
            onEditProfileClick = { navController.navigate(Routes.EDIT_PROFILE) },
            onReviewClick = { index -> navController.navigate(Routes.commentsReview(index)) }
        )
    }

    composable(Routes.EDIT_PROFILE) {
        EditarPerfilScreen(
            onCancelClick = { navController.popBackStack() },
            onSaveClick = { navController.popBackStack() }
        )
    }

    composable(
        route = Routes.COMMENTS_REVIEW,
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
