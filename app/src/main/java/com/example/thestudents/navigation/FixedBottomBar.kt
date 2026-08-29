package com.example.thestudents.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme

/**
 * Barra de navegación inferior que recibe directamente el NavController.
 */
@Composable
fun FixedBottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    val selectedRoute = selectedTabFor(currentRoute)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            color = MaterialTheme.colorScheme.surfaceContainer,
            tonalElevation = 8.dp,
            shadowElevation = 16.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // 1. Home
                NavItem(
                    label = "Inicio",
                    filledIcon = Icons.Filled.Home,
                    outlinedIcon = Icons.Outlined.Home,
                    selected = selectedRoute == Screen.Home.route,
                    onClick = { navController.navigateToTab(Screen.Home.route) }
                )

                // 2. Search / Explorar
                NavItem(
                    label = "Explorar",
                    filledIcon = Icons.Filled.Search,
                    outlinedIcon = Icons.Outlined.Search,
                    selected = selectedRoute == Screen.Search.route,
                    onClick = { navController.navigateToTab(Screen.Search.route) }
                )

                // Hueco reservado para el botón flotante central
                Box(modifier = Modifier.size(64.dp))

                // 3. Notifications
                NavItem(
                    label = "Notificaciones",
                    filledIcon = Icons.Filled.Notifications,
                    outlinedIcon = Icons.Outlined.Notifications,
                    selected = selectedRoute == Screen.Notifications.route,
                    onClick = { navController.navigateToTab(Screen.Notifications.route) }
                )

                // 4. Profile
                NavItem(
                    label = "Perfil",
                    filledIcon = Icons.Filled.Person,
                    outlinedIcon = Icons.Outlined.Person,
                    selected = selectedRoute == Screen.Profile.route,
                    onClick = { navController.navigateToTab(Screen.Profile.route) }
                )
            }
        }

        // Botón central flotante (Reseñas)
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(64.dp)
                .offset(y = (-32).dp),
            onClick = { navController.navigateToTab(Screen.Reviews.route) },
            shadowElevation = 8.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(R.drawable.logosinfondo),
                    contentDescription = stringResource(R.string.resenar),
                    modifier = Modifier.size(42.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FixedBottomBarPreview() {
    TheStudentsTheme {
        Surface {
            FixedBottomBar(
                navController = rememberNavController()
            )
        }
    }
}
