package com.example.thestudents

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.navigation.AppNavigation
import com.example.thestudents.navigation.FixedBottomBar
import com.example.thestudents.navigation.Screen
import com.example.thestudents.navigation.selectedTabFor
import com.google.firebase.messaging.FirebaseMessaging

@Composable
fun TheStudentsApp() {
    val navController = rememberNavController()

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route
    val selectedTab = selectedTabFor(currentRoute)

    val showBottomBar = selectedTab != Screen.Login.route && selectedTab != Screen.Register.route

    val context = LocalContext.current
    // Launcher para pedir permiso
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Log.d("FCM", "Permiso de notificaciones concedido ✅")
            } else {
                Log.d("FCM", "Permiso de notificaciones denegado ❌")
            }
        }
    )
        // Al entrar al Composable, pedimos el permiso si hace falta
            LaunchedEffect(Unit) {

              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
              }
            }

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Token: ${task.result}")
                } else {
                    Log.e("FCM", "Error obteniendo token", task.exception)
                }
            }

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
