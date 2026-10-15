package com.example.thestudents.ui.screens.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.data.NotificationType
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.theme.extended
import com.example.thestudents.ui.utils.ProfileIcon

/** Obtiene el icono del badge segun el tipo de notificacion. */
private fun badgeIconFor(type: NotificationType): ImageVector {
    return when (type) {
        NotificationType.REVIEW -> Icons.Default.Star
        NotificationType.LIKE -> Icons.Default.ThumbUp
        NotificationType.FOLLOW_REQUEST -> Icons.Default.PersonAdd
        NotificationType.COMMENT -> Icons.Default.ChatBubble
    }
}

/**
 * Avatar de notificaciones: muestra la foto/iniciales del estudiante usando [ProfileIcon]
 * e incluye una pequeña insignia (badge) en la esquina inferior derecha que indica el tipo de notificacion.
 */
@Composable
fun NotificationAvatar(
    student: Student,
    type: NotificationType,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val badgeIcon = badgeIconFor(type)
    val badgeColor = MaterialTheme.extended.rating

    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.BottomEnd
    ) {
        ProfileIcon(
            initials = student.initials,
            profileImage = student.profileImage,
            backgroundColor = student.profileColor,
            contentColor = MaterialTheme.colorScheme.onPrimary,
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
