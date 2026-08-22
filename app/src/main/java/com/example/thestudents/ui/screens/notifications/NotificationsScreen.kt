package com.example.thestudents.ui.screens.notifications

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R
import com.example.thestudents.data.Notification
import com.example.thestudents.data.local.localNotificationProvider
import com.example.thestudents.ui.screens.notifications.components.NotificationItem
import com.example.thestudents.ui.theme.TheStudentsTheme

/**
 * Contenido de notificaciones.
 *
 * La cabecera va dentro del contenido, no en el topBar de un Scaffold: el unico Scaffold de la
 * app vive en AppNavigation y no conoce las cabeceras de cada pantalla.
 */
@Composable
fun BodyNotifications(
    notifications: List<Notification>,
    onAcceptClick: (Int) -> Unit,
    onRejectClick: (Int) -> Unit,
    onViewDetailClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        NotificationsTopBar()

        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.sin_notificaciones),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(notifications, key = { it.id }) { notification ->
                    NotificationItem(
                        notification = notification,
                        onAcceptClick = { onAcceptClick(notification.id) },
                        onRejectClick = { onRejectClick(notification.id) },
                        onViewDetailClick = { onViewDetailClick(notification.id) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsTopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.notificaciones),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun BodyNotificationsPreview() {
    TheStudentsTheme {
        Surface {
            BodyNotifications(
                notifications = localNotificationProvider.allNotifications,
                onAcceptClick = {},
                onRejectClick = {},
                onViewDetailClick = {}
            )
        }
    }
}

@Preview(name = "Vacio", showBackground = true)
@Composable
fun BodyNotificationsEmptyPreview() {
    TheStudentsTheme {
        Surface {
            BodyNotifications(
                notifications = emptyList(),
                onAcceptClick = {},
                onRejectClick = {},
                onViewDetailClick = {}
            )
        }
    }
}

/**
 * Pantalla de notificaciones.
 *
 * Las acciones de aceptar, rechazar y ver detalle siguen pendientes de logica; se dejan como
 * parametros para que quien las implemente no tenga que abrir el contenido.
 */
@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    onAcceptClick: (Int) -> Unit = {},
    onRejectClick: (Int) -> Unit = {},
    onViewDetailClick: (Int) -> Unit = {}
)
{
    var notifications  = localNotificationProvider.allNotifications
    BodyNotifications(
        notifications = notifications,
        onAcceptClick = onAcceptClick,
        onRejectClick = onRejectClick,
        onViewDetailClick = onViewDetailClick,
        modifier = modifier
    )
}

@Preview(name = "Claro", showBackground = true, showSystemUi = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun NotificationsScreenPreview() {
    TheStudentsTheme {
        Surface {
            NotificationsScreen()
        }
    }
}
