package com.example.thestudents.ui.screens.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.data.Student
import com.example.thestudents.data.NotificationType
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.theme.extended
import com.example.thestudents.ui.utils.ProfileIcon

/** Apariencia del avatar segun el tipo de notificacion, resuelta contra el tema activo. */
private data class NotificationAvatarStyle(
    val badgeIcon: ImageVector,
    val avatarContainer: Color,
    val onAvatarContainer: Color
)

@Composable
private fun styleFor(type: NotificationType): NotificationAvatarStyle {
    val scheme = MaterialTheme.colorScheme
    return when (type) {
        NotificationType.REVIEW -> NotificationAvatarStyle(
            Icons.Default.Star, scheme.primaryContainer, scheme.onPrimaryContainer
        )
        NotificationType.LIKE -> NotificationAvatarStyle(
            Icons.Default.ThumbUp, scheme.secondaryContainer, scheme.onSecondaryContainer
        )
        NotificationType.FOLLOW_REQUEST -> NotificationAvatarStyle(
            Icons.Default.PersonAdd, scheme.tertiaryContainer, scheme.onTertiaryContainer
        )
        NotificationType.COMMENT -> NotificationAvatarStyle(
            Icons.Default.ChatBubble, scheme.surfaceContainerHighest, scheme.onSurface
        )
    }
}

@Composable
fun NotificationAvatar(
    student: Student,
    type: NotificationType,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val style = styleFor(type)
    val badgeIcon = style.badgeIcon
    val badgeColor = MaterialTheme.extended.rating
    val avatarBg = style.avatarContainer

    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.BottomEnd
    ) {
        ProfileIcon(
            initials = student.initials,
            profileImage = student.profileImage,
            backgroundColor = avatarBg,
            contentColor = style.onAvatarContainer,
            fontSize = 18.sp,
            modifier = Modifier.size(48.dp)
        )
        
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .border(1.dp, MaterialTheme.colorScheme.surfaceContainerLowest, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = badgeIcon,
                contentDescription = null,
                tint = badgeColor,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationAvatarPreview() {
    TheStudentsTheme {
        NotificationAvatar(
            student = localStudentProvider.students[3],
            type = NotificationType.REVIEW
        )
    }
}
