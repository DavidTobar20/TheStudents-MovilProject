package com.example.thestudents.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.ui.screens.home.HomeScreen
import com.example.thestudents.ui.screens.login.LoginScreen
import com.example.thestudents.ui.screens.profile.ProfileScreen
import com.example.thestudents.ui.screens.reviews.ReviewsScreen
import com.example.thestudents.ui.screens.search.SearchScreen
import com.example.thestudents.ui.screens.EditarPerfil.EditarPerfilScreen
import com.example.thestudents.ui.screens.commentsReview.CommentsReviewScreen
import com.example.thestudents.ui.screens.notifications.NotificationsScreen
import com.example.thestudents.ui.screens.register.RegisterScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier.fillMaxSize()
    ) {
        composable("login") { 
            LoginScreen(
                onLoginSuccess = { navController.navigate("home") },
                onCreateAccountClick = { navController.navigate("register") }
            )
        }
        
        composable("register") {
            RegisterScreen(
                onRegisterClick = { navController.navigate("home") },
                onSsoClick = { /* Handle SSO */ },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable("home") { HomeScreen(navController = navController) }
        
        composable("search") { SearchScreen(navController = navController) }
        
        composable("notifications") { 
            NotificationsScreen(navController = navController) 
        }

        composable("profile") { ProfileScreen(navController = navController) }
        
        composable("edit_profile") { 
            EditarPerfilScreen(navController = navController) 
        }

        composable("reviews") { ReviewsScreen(navController = navController) }

        // Ruta para comentar reseña, pasamos un índice por ahora para simplificar
        composable(
            route = "comments_review/{reviewIndex}",
            arguments = listOf(navArgument("reviewIndex") { type = NavType.IntType })
        ) { backStackEntry ->
            val reviewIndex = backStackEntry.arguments?.getInt("reviewIndex") ?: 0
            val review = localReviewsProvider.allReviews.getOrNull(reviewIndex) 
                ?: localReviewsProvider.allReviews[0]
            
            CommentsReviewScreen(
                navController = navController,
                review = review
            )
        }
    }
}