package com.example.thestudents.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.R
import com.example.thestudents.ui.theme.*

@Composable
fun DiamondDivider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = Tan, thickness = 1.dp)
        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(10.dp)
                .rotate(45f)
                .background(Tan)
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = Tan, thickness = 1.dp)
    }
}

@Composable
fun FixedBottomBar(
    navController: NavController,
    currentRoute: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Fondo de la barra - Aumentamos altura y ajustamos padding
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            color = Color.White,
            tonalElevation = 8.dp,
            shadowElevation = 16.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 8.dp), // Padding para alejar el texto del borde inferior
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                NavItem(Icons.Default.Home, "Inicio", currentRoute == "home") {
                    navController.navigate("home") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                NavItem(Icons.Default.Search, "Explorar", currentRoute == "search") {
                    navController.navigate("search") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                
                // Espacio exacto para que el botón central no tape nada
                Spacer(modifier = Modifier.size(64.dp))

                NavItem(Icons.Default.Edit, "Publicar", currentRoute == "reviews") {
                    navController.navigate("reviews") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                NavItem(Icons.Default.Person, "Perfil", currentRoute == "profile") {
                    navController.navigate("profile") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }

        // Botón central flotante - Ajustado para que no se corte y luzca perfecto
        Surface(
            shape = CircleShape, 
            color = DarkGreen, 
            modifier = Modifier
                .size(64.dp)
                .offset(y = (-32).dp), // Elevado exactamente la mitad para un look circular perfecto
            onClick = {
                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            shadowElevation = 8.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(R.drawable.logosinfondo),
                    contentDescription = "Inicio rápido",
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
    FixedBottomBar(rememberNavController(), "home")
}

@Preview(showBackground = true)
@Composable
fun DiamondDividerPreview() {
    DiamondDivider()
}

@Composable
fun NavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp), // Padding más compacto
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            icon, 
            contentDescription = label, 
            tint = if (selected) DarkGreen else MediumGreen, 
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label, 
            fontSize = 11.sp, 
            color = if (selected) DarkGreen else MediumGreen,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1
        )
    }
}
