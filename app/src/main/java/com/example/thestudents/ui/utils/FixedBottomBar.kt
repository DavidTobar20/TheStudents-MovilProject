package com.example.thestudents.ui.utils

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
import com.example.thestudents.R
import com.example.thestudents.navigation.Screen
import com.example.thestudents.navigation.bottomNavItems
import com.example.thestudents.ui.theme.TheStudentsTheme

/**
 * Barra de navegacion inferior dinamica.
 *
 * Itera sobre la lista [bottomNavItems] para mostrar las pestañas laterales y
 * mantiene un boton central para la accion de "Reseñar".
 */
@Composable
fun FixedBottomBar(
    selectedRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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
                // Iteramos por los primeros dos items (Home, Search)
                bottomNavItems.take(2).forEach { item ->
                    val isSelected = selectedRoute == item.route
                    NavItem(
                        icon = if (isSelected) item.iconFilled else item.iconOutline,
                        label = stringResource(item.labelRes),
                        selected = isSelected,
                        onClick = { onNavigate(item.route) }
                    )
                }

                // Hueco reservado para que el boton central no tape ningun item.
                Box(modifier = Modifier.size(64.dp))

                // Iteramos por los ultimos dos items (Notifications, Profile)
                bottomNavItems.takeLast(2).forEach { item ->
                    val isSelected = selectedRoute == item.route
                    NavItem(
                        icon = if (isSelected) item.iconFilled else item.iconOutline,
                        label = stringResource(item.labelRes),
                        selected = isSelected,
                        onClick = { onNavigate(item.route) }
                    )
                }
            }
        }

        // Boton central elevado media altura sobre la barra.
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(64.dp)
                .offset(y = (-32).dp),
            onClick = { onNavigate(Screen.Reviews.route) },
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
            FixedBottomBar(selectedRoute = Screen.Home.route, onNavigate = {})
        }
    }
}
