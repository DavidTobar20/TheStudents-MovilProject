package com.example.thestudents.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.ui.screens.home.HomeScreen
import com.example.thestudents.ui.screens.login.LoginScreen
import com.example.thestudents.ui.screens.profile.ProfileScreen
import com.example.thestudents.ui.screens.reviews.ReviewsScreen
import com.example.thestudents.ui.screens.search.SearchScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier.fillMaxSize()
    ) {
        composable("login") { 
            LoginScreen(onLoginSuccess = { navController.navigate("search") }) 
        }
        composable("home") { HomeScreen(navController = navController) }
        composable("search") { SearchScreen(navController = navController) }
        composable("profile") { ProfileScreen(navController = navController) }
        composable("reviews") { ReviewsScreen(navController = navController) }
    }
}