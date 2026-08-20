package com.example.thestudents.ui.screens.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.data.NotificationType
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun NotificationAvatar(
    initials: String,
    type: NotificationType,
    modifier: Modifier = Modifier
) {
    val (badgeIcon, badgeColor, avatarBg) = when (type) {
        NotificationType.REVIEW -> Triple(Icons.Default.Star, colorResource(R.color.gold), Color(0xFF4E7D6B))
        NotificationType.LIKE -> Triple(Icons.Default.ThumbUp, colorResource(R.color.gold), Color(0xFF1B3935))
        NotificationType.FOLLOW_REQUEST -> Triple(Icons.Default.PersonAdd, colorResource(R.color.gold), Color(0xFF6A5ACD))
        NotificationType.COMMENT -> Triple(Icons.Default.ChatBubble, colorResource(R.color.gold), Color(0xFFA0522D))
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(avatarBg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
        
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, Color.White, CircleShape),
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
        NotificationAvatar(initials = "MJ", type = NotificationType.REVIEW)
    }
}
