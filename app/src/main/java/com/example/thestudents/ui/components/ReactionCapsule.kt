package com.example.thestudents.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.theme.extended

// Capsula de like | dislike usada en la tarjeta de reseña y en los comentarios
@Composable
fun ReactionCapsule(
    likes: Int,
    dislikes: Int,
    isLiked: Boolean,
    isDisliked: Boolean,
    onLikeClick: () -> Unit,
    onDislikeClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 44.dp,
    iconSize: Dp = 18.dp,
    fontSize: TextUnit = 14.sp
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .border(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest, CircleShape)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ReactionButton(
            icon = Icons.Outlined.ThumbUp,
            count = likes,
            isSelected = isLiked,
            // Like: verde claro de la marca
            selectedContainerColor = MaterialTheme.extended.like,
            selectedContentColor = MaterialTheme.extended.onLike,
            contentDescription = "Me gusta",
            height = height,
            iconSize = iconSize,
            fontSize = fontSize,
            onClick = onLikeClick
        )
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(height / 2)
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
        )
        ReactionButton(
            icon = Icons.Outlined.ThumbDown,
            count = dislikes,
            isSelected = isDisliked,
            // Dislike: verde oscuro de la marca
            selectedContainerColor = MaterialTheme.extended.dislike,
            selectedContentColor = MaterialTheme.extended.onDislike,
            contentDescription = "No me gusta",
            height = height,
            iconSize = iconSize,
            fontSize = fontSize,
            onClick = onDislikeClick
        )
    }
}

@Composable
private fun ReactionButton(
    icon: ImageVector,
    count: Int,
    isSelected: Boolean,
    selectedContainerColor: Color,
    selectedContentColor: Color,
    contentDescription: String,
    height: Dp,
    iconSize: Dp,
    fontSize: TextUnit,
    onClick: () -> Unit
) {
    val contentColor = if (isSelected) selectedContentColor else MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = Modifier
            .height(height)
            .background(if (isSelected) selectedContainerColor else Color.Transparent)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = contentColor,
            modifier = Modifier.size(iconSize)
        )
        Text(
            text = count.toString(),
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold,
            color = contentColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReactionCapsulePreview() {
    TheStudentsTheme {
        Surface {
            ReactionCapsule(
                likes = 5,
                dislikes = 0,
                isLiked = true,
                isDisliked = false,
                onLikeClick = {},
                onDislikeClick = {}
            )
        }
    }
}
