package com.example.thestudents.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
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
    onCardClick: (() -> Unit)? = null,
    onReviewerClick: (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(24.dp)

    Card(
        modifier = modifier
            .fillMaxWidth()
            // Sombra en capas + borde fino para separar cada reseña del fondo crema
            .shadow(
                elevation = 18.dp,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.12f),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
            )
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f), shape)
            .then(if (onCardClick != null) Modifier.clickable(onClick = onCardClick) else Modifier),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ENCABEZADO: autor (con aro dorado), fecha y calificacion
            ReviewHeader(
                reviewer = review.reviewer,
                time = review.time,
                rating = review.rating,
                onReviewerClick = onReviewerClick
            )

            // A quien se le hizo la reseña, en que materia y periodo
            ReviewTarget(review = review)

            // Contenido de la reseña como cita
            ReviewQuote(content = review.content)
        }

        // Franja inferior de borde a borde (reemplaza la linea suelta)
        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHigh, thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Like y dislike en una sola capsula con tags de reseña
            ReactionCapsule(
                likes = review.likesCount,
                dislikes = review.disLikes,
                isLiked = isLiked,
                isDisliked = isDisliked,
                onLikeClick = onLikeClick,
                onDislikeClick = onDislikeClick,
                likeButtonModifier = Modifier.testTag("reviewLikeButton"),
                likeCountModifier = Modifier.testTag("reviewLikesCount"),
                dislikeButtonModifier = Modifier.testTag("reviewDislikeButton"),
                dislikeCountModifier = Modifier.testTag("reviewDislikesCount")
            )

            if (onCommentClick != null) {
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(role = Role.Button, onClick = onCommentClick)
                        .height(44.dp)
                        .padding(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = pluralStringResource(R.plurals.comentarios_total, review.commentsCount, review.commentsCount),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
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
