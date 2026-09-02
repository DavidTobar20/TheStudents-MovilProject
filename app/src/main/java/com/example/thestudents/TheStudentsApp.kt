package com.example.thestudents

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.navigation.AppNavigation
import com.example.thestudents.navigation.FixedBottomBar
import com.example.thestudents.navigation.Screen
import com.example.thestudents.navigation.selectedTabFor

@Composable
fun TheStudentsApp() {
    val navController = rememberNavController()

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route
    val selectedTab = selectedTabFor(currentRoute)

    val showBottomBar = selectedTab != Screen.Login.route && selectedTab != Screen.Register.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar){
                FixedBottomBar(
                    navController = navController
                )
            }
        }
    ) {
        AppNavigation(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            navController = navController
        )
    }

}
