package com.example.thestudents.ui.screens.commentsReview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.utils.ProfileIcon

@Composable
fun CommentInputFieldBar(
    modifier: Modifier = Modifier,
    commentText: String,
    onCommentChange: (String) -> Unit,
    onSendClick: () -> Unit,
    initialCommentator: String,
    profileImageCommentator: Int?,
    backgroundColorProfileIconCommentator: Color,
    contentColorProfileIconCommentator: Color
) {
    Surface(
        color = Color.White,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileIcon(
                initials = initialCommentator,
                profileImage = profileImageCommentator,
                backgroundColor = backgroundColorProfileIconCommentator,
                contentColor = contentColorProfileIconCommentator,
                fontSize = 19.sp,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            CommentInputField(
                modifier = Modifier.weight(1f),
                commentText = commentText,
                onCommentChange = onCommentChange
            )
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color = colorResource(R.color.sage))
                    .clickable(onClick = onSendClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Enviar comentario",
                    tint = Color.White,
                    modifier = Modifier.size(19.dp)
                )
            }


        }
    }
}

@Preview (showBackground = true)
@Composable
fun CommentInputFieldBarPreview(){
    var commentText by remember { mutableStateOf("") }
    CommentInputFieldBar(
        commentText = commentText,
        onCommentChange = {commentText = it},
        onSendClick = {},
        initialCommentator = "JP",
        profileImageCommentator = null,
        backgroundColorProfileIconCommentator = Color.Gray,
        contentColorProfileIconCommentator = Color.White
    )
}