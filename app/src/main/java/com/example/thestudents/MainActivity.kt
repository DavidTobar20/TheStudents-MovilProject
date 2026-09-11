package com.example.thestudents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.thestudents.ui.theme.TheStudentsTheme
import dagger.hilt.android.AndroidEntryPoint

// Habilita la inyección de dependencias (y hiltViewModel) en esta Activity
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TheStudentsTheme {
                TheStudentsApp()
            }
        }
    }
}