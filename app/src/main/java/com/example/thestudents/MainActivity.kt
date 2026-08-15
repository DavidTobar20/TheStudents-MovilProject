package com.example.thestudents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.thestudents.navigation.AppNavigation
import com.example.thestudents.ui.screens.login.LoginScreen
import com.example.thestudents.ui.screens.search.SearchScreen
import com.example.thestudents.ui.theme.TheStudentsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TheStudentsTheme {
                AppNavigation()
            }
        }
    }
}