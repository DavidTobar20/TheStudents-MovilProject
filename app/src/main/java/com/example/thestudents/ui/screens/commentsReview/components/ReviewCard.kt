package com.example.thestudents.ui.screens.commentsReview.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.data.Review
import com.example.thestudents.data.local.localReviewsProvider

@Composable
fun ReviewCard(
    modifier: Modifier = Modifier,
    review: Review,
    isLiked: Boolean,
    isDisliked: Boolean,
    onLikeClick: () -> Unit,
    onDislikeClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            CardReviewerInfo(
                nameReviewer = review.reviewer.name,
                usernameReviewer = review.reviewer.username,
                timeAgo = review.time,
                initialsReviewer = review.reviewer.initials,
                rating = review.rating
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("sobre ")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        ) {
                            append(review.nameReviewed)
                        }
                    },
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                InfoTag(text = review.classReviewed)
                InfoTag(text = review.periodReviewed)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = review.content,
                fontSize = 14.sp,
                color = Color(0xFF2C2C2C),
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color.Black, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SelectableIconButton(
                    icon = Icons.Outlined.ThumbUp,
                    count = review.likes,
                    isSelected = isLiked,
                    selectedColor = Color.Black,
                    onClick = onLikeClick
                )
                SelectableIconButton(
                    icon = Icons.Outlined.ThumbDown,
                    count = review.disLikes,
                    isSelected = isDisliked,
                    selectedColor = Color.Red,
                    onClick = onDislikeClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewCardPreview() {
    var isLiked by remember { mutableStateOf(false) }
    var isDisliked by remember { mutableStateOf(false) }
    ReviewCard(
        review = localReviewsProvider.allReviews[1],
        isLiked = isLiked,
        isDisliked = isDisliked,
        onLikeClick = {isLiked=!isLiked},
        onDislikeClick = {isDisliked=!isDisliked}
    )
}