package com.example.thestudents.ui.screens.commentsReview.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.data.Comment
import com.example.thestudents.data.local.localCommentsProvider
import com.example.thestudents.ui.utils.ProfileIcon

@Composable
fun CommentItem(
    comment: Comment,
    isLiked: Boolean,
    isDisliked: Boolean,
    onLikeClick: () -> Unit,
    onDislikeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        ProfileIcon(
            initials = comment.commentator.initials,
            profileImage = comment.commentator.profileImage,
            backgroundColor = comment.commentator.profileColor,
            contentColor = Color.White,
            fontSize = 18.sp,
            modifier = Modifier.size(43.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = comment.commentator.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = comment.createdAt,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comment.content,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SelectableIconButton(
                    icon = Icons.Outlined.ThumbUp,
                    count = comment.likes,
                    isSelected = isLiked,
                    onClick = onLikeClick,
                    selectedColor = Color.Black,
                )
                SelectableIconButton(
                    icon = Icons.Outlined.ThumbDown,
                    count = comment.disLikes,
                    isSelected = isDisliked,
                    onClick = onDislikeClick,
                    selectedColor = Color.Red
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommentItemPreview() {
    var isLiked by remember { mutableStateOf(false) }
    var isDisliked by remember { mutableStateOf(false) }
    CommentItem(
        comment = localCommentsProvider.allComments[1],
        isLiked = isLiked,
        isDisliked = isDisliked,
        onLikeClick = {isLiked=!isLiked},
        onDislikeClick = {isDisliked=!isDisliked}
    )
}