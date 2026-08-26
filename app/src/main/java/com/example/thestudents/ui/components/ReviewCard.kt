package com.example.thestudents.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.data.Review
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun ReviewCard(
    modifier: Modifier = Modifier,
    review: Review,
    isLiked: Boolean,
    isDisliked: Boolean,
    onLikeClick: () -> Unit,
    onDislikeClick: () -> Unit,
    onCommentClick: (() -> Unit)? = null,
    onCardClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onCardClick != null) Modifier.clickable(onClick = onCardClick) else Modifier),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            // ENCABEZADO: La persona que SIGO (el Autor del análisis)
            CardReviewerInfo(
                student = review.reviewer,
                subtitle = review.time,
                rating = review.rating
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // REFERENCIA: A quién se le hizo la reseña y en qué materia
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("reseñó a ")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            append(review.reviewedStudent.name)
                        }
                    },
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                InfoTag(text = review.classReviewed)
                InfoTag(text = review.periodReviewed)
            }

            Spacer(modifier = Modifier.height(12.dp))
            
            // Contenido de la reseña
            Text(
                text = review.content,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))
            
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 1.dp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Interacciones
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SelectableIconButton(
                    icon = Icons.Outlined.ThumbUp,
                    count = review.likes,
                    isSelected = isLiked,
                    selectedColor = MaterialTheme.colorScheme.tertiary,
                    onClick = onLikeClick
                )
                SelectableIconButton(
                    icon = Icons.Outlined.ThumbDown,
                    count = review.disLikes,
                    isSelected = isDisliked,
                    selectedColor = MaterialTheme.colorScheme.tertiary,
                    onClick = onDislikeClick
                )
                if (onCommentClick != null) {
                    SelectableIconButton(
                        icon = Icons.Outlined.ChatBubbleOutline,
                        count = review.comments.size,
                        isSelected = false,
                        selectedColor = MaterialTheme.colorScheme.onSurface,
                        onClick = onCommentClick
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewCardPreview() {
    TheStudentsTheme {
        Surface {
            ReviewCard(
                review = localReviewsProvider.allReviews[0],
                isLiked = false,
                isDisliked = false,
                onLikeClick = {},
                onDislikeClick = {},
                onCommentClick = {}
            )
        }
    }
}
