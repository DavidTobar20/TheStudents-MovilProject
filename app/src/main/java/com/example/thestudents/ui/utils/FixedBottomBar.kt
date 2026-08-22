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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import com.example.thestudents.navigation.Routes
import com.example.thestudents.ui.theme.TheStudentsTheme

/**
 * Barra de navegacion inferior.
 *
 * Recibe la ruta activa y una funcion para navegar en vez del NavController, asi que no depende
 * de la navegacion: se puede previsualizar y probar pasandole lambdas vacias.
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
                NavItem(
                    icon = Icons.Default.Home,
                    label = stringResource(R.string.inicio),
                    selected = selectedRoute == Routes.HOME,
                    onClick = { onNavigate(Routes.HOME) }
                )
                NavItem(
                    icon = Icons.Default.Search,
                    label = stringResource(R.string.explorar),
                    selected = selectedRoute == Routes.SEARCH,
                    onClick = { onNavigate(Routes.SEARCH) }
                )

                // Hueco reservado para que el boton central no tape ningun item.
                Box(modifier = Modifier.size(64.dp))

                NavItem(
                    icon = Icons.Default.Notifications,
                    label = stringResource(R.string.notificaciones),
                    selected = selectedRoute == Routes.NOTIFICATIONS,
                    onClick = { onNavigate(Routes.NOTIFICATIONS) }
                )
                NavItem(
                    icon = Icons.Default.Person,
                    label = stringResource(R.string.perfil),
                    selected = selectedRoute == Routes.PROFILE,
                    onClick = { onNavigate(Routes.PROFILE) }
                )
            }
        }

        // Boton central elevado media altura sobre la barra.
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(64.dp)
                .offset(y = (-32).dp),
            onClick = { onNavigate(Routes.REVIEWS) },
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
            FixedBottomBar(selectedRoute = Routes.HOME, onNavigate = {})
        }
    }
}
