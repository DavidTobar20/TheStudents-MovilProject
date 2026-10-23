package com.example.thestudents.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.data.Review
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.ui.theme.OnAvatar
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.theme.extended
import com.example.thestudents.ui.utils.ProfileIcon

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
            // Like y dislike en una sola capsula
            ReactionCapsule(
                likes = review.likesCount,
                dislikes = review.disLikes,
                isLiked = isLiked,
                isDisliked = isDisliked,
                onLikeClick = onLikeClick,
                onDislikeClick = onDislikeClick
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

@Composable
private fun ReviewHeader(
    reviewer: Student,
    time: String,
    rating: String?,
    onReviewerClick: (() -> Unit)?
) {
    val clickModifier = if (onReviewerClick != null) Modifier.clickable(onClick = onReviewerClick) else Modifier

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar con aro dorado fino
        Box(
            modifier = Modifier
                .size(48.dp)
                .border(1.5.dp, MaterialTheme.extended.rating, CircleShape)
                .padding(3.dp)
                .clip(CircleShape)
                .then(clickModifier)
        ) {
            ProfileIcon(
                initials = reviewer.initials,
                profileImage = reviewer.profileImage,
                backgroundColor = reviewer.profileColor,
                contentColor = OnAvatar,
                fontSize = 15.sp,
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .then(clickModifier),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = reviewer.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1
            )
            if (time.isNotBlank()) {
                Text(
                    text = time,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (rating != null) {
            RatingPill(rating = rating)
        }
    }
}

// Pildora verde oscuro con estrella dorada: "★ 4,5" (tambien se usa en Notificaciones)
@Composable
fun RatingPill(rating: String) {
    val value = rating.toDoubleOrNull()
    val ratingText = when {
        value == null -> rating
        value % 1.0 == 0.0 -> value.toInt().toString()
        else -> value.toString().replace('.', ',')
    }

    Row(
        modifier = Modifier
            .height(32.dp)
            .clip(CircleShape)
            .background(MaterialTheme.extended.ratingPill)
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = MaterialTheme.extended.ratingPillStar,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = ratingText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.extended.onRatingPill
        )
    }
}

// Recuadro "RESEÑA PARA Valeria" con la materia y el periodo a la derecha
@Composable
private fun ReviewTarget(review: Review) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileIcon(
            initials = review.reviewedStudent.initials,
            profileImage = review.reviewedStudent.profileImage,
            backgroundColor = review.reviewedStudent.profileColor,
            contentColor = OnAvatar,
            fontSize = 13.sp,
            modifier = Modifier.size(32.dp)
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Text(
                text = stringResource(R.string.resena_para),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.4.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = review.reviewedStudent.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1
            )
        }
        if (review.classReviewed.isNotBlank() || review.periodReviewed.isNotBlank()) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                if (review.classReviewed.isNotBlank()) {
                    Text(
                        text = review.classReviewed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                if (review.periodReviewed.isNotBlank()) {
                    Text(
                        text = stringResource(R.string.periodo_x, review.periodReviewed),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// Contenido en cursiva con comillas doradas
@Composable
private fun ReviewQuote(content: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "\u201C",
            fontSize = 44.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.extended.rating
        )
        Text(
            text = content,
            fontSize = 15.sp,
            lineHeight = 24.sp,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.onSurface
        )
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
