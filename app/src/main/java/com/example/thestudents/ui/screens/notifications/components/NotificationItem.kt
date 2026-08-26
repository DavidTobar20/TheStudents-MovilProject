package com.example.thestudents.ui.screens.notifications.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R
import com.example.thestudents.data.Notification
import com.example.thestudents.data.NotificationType
import com.example.thestudents.data.local.localNotificationProvider
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun NotificationItem(
    notification: Notification,
    modifier: Modifier = Modifier,
    onAcceptClick: () -> Unit = {},
    onRejectClick: () -> Unit = {},
    onViewDetailClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            NotificationAvatar(
                initials = notification.userInitials,
                type = notification.type,
                onClick = onAvatarClick
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                NotificationContentSwitcher(
                    notification = notification,
                    onAcceptClick = onAcceptClick,
                    onRejectClick = onRejectClick,
                    onViewDetailClick = onViewDetailClick
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = stringResource(R.string.hace_tiempo, notification.timeAgo),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun NotificationContentSwitcher(
    notification: Notification,
    onAcceptClick: () -> Unit,
    onRejectClick: () -> Unit,
    onViewDetailClick: () -> Unit
) {
    when (notification.type) {
        NotificationType.REVIEW -> ReviewNotificationContent(
            userName = notification.userName,
            subject = notification.subject ?: "",
            comment = notification.comment ?: "",
            rating = notification.rating ?: 0,
            onViewDetailClick = onViewDetailClick
        )
        NotificationType.LIKE -> LikeNotificationContent(
            userName = notification.userName,
            reviewOf = notification.reviewOf ?: ""
        )
        NotificationType.FOLLOW_REQUEST -> FollowRequestContent(
            userName = notification.userName,
            onAcceptClick = onAcceptClick,
            onRejectClick = onRejectClick
        )
        NotificationType.COMMENT -> CommentNotificationContent(
            userName = notification.userName,
            subject = notification.subject ?: "",
            courseCode = notification.courseCode ?: "",
            snippet = notification.snippet ?: "",
            onViewDetailClick = onViewDetailClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationItemPreview() {
    TheStudentsTheme {
        NotificationItem(notification = localNotificationProvider.allNotifications[0])
    }
}
