package com.example.thestudents.ui.screens.notifications

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.R
import com.example.thestudents.data.Notification
import com.example.thestudents.data.local.localNotificationProvider
import com.example.thestudents.ui.screens.notifications.components.NotificationItem
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.FixedBottomBar

@Composable
fun BodyNotifications(
    notifications: List<Notification>,
    onAcceptClick: (Int) -> Unit,
    onRejectClick: (Int) -> Unit,
    onViewDetailClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    if (notifications.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No tienes notificaciones por ahora.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            contentPadding = contentPadding
        ) {
            items(notifications, key = { it.id }) { notification ->
                NotificationItem(
                    notification = notification,
                    onAcceptClick = { onAcceptClick(notification.id) },
                    onRejectClick = { onRejectClick(notification.id) },
                    onViewDetailClick = { onViewDetailClick(notification.id) }
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun BodyNotificationsPreview() {
    TheStudentsTheme {
        BodyNotifications(
            notifications = localNotificationProvider.allNotifications,
            onAcceptClick = {},
            onRejectClick = {},
            onViewDetailClick = {}
        )
    }
}

@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController()
) {
    val notifications = localNotificationProvider.allNotifications

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            NotificationsTopBar()
        },
        bottomBar = {
            FixedBottomBar(navController, "notifications")
        }
    ) { padding ->
        BodyNotifications(
            notifications = notifications,
            onAcceptClick = { /* Handle accept */ },
            onRejectClick = { /* Handle reject */ },
            onViewDetailClick = { /* Handle view detail */ },
            contentPadding = padding
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.notificaciones),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))

            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun NotificationBadge(count: Int) {
    Box(contentAlignment = Alignment.TopEnd) {
        Icon(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        )
            }
        }



@Preview(name = "Claro", showBackground = true, showSystemUi = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun NotificationsScreenPreview() {
    TheStudentsTheme {
        NotificationsScreen()
    }
}
