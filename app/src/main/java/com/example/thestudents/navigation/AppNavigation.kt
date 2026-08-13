package com.example.thestudents.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.ui.screens.home.HomeScreen
import com.example.thestudents.ui.screens.login.LoginScreen
import com.example.thestudents.ui.screens.profile.ProfileScreen
import com.example.thestudents.ui.screens.reviews.ReviewsScreen
import com.example.thestudents.ui.screens.search.SearchScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen()
        }
        composable("home") {
            HomeScreen()
        }
        composable("search") {
            SearchScreen()
        }
        composable("profile") {
            ProfileScreen()
        }
        composable("reviews") {
            ReviewsScreen()
        }
    }
}
