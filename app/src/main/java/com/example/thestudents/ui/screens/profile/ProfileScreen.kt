package com.example.thestudents.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.ui.components.FixedBottomBar
import com.example.thestudents.ui.theme.Cream

@Composable
fun ProfileScreen(navController: NavController = rememberNavController()) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Cream,
        bottomBar = { FixedBottomBar(navController, "profile") }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Profile Screen")
        }
    }
}
