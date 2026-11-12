package com.example.thestudents.ui.screens.map.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.data.Review
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.ui.components.RatingPill
import com.example.thestudents.ui.theme.OnAvatar
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.theme.extended
import com.example.thestudents.ui.utils.ButtonWithoutIcon
import com.example.thestudents.ui.utils.ProfileIcon

@Composable
fun MapReviewInfoCard(
    review: Review,
    onReviewClick: (String) -> Unit,
    onStudentClick: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(24.dp)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.2f),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f), shape)
            .testTag("mapReviewInfoCard"),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Encabezado: Autor, fecha, calificacion y boton de cerrar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .border(1.5.dp, MaterialTheme.extended.rating, CircleShape)
                        .padding(3.dp)
                        .clip(CircleShape)
                        .clickable { onStudentClick(review.reviewer.id) }
                ) {
                    ProfileIcon(
                        initials = review.reviewer.initials,
                        profileImage = review.reviewer.profileImage,
                        backgroundColor = review.reviewer.profileColor,
                        contentColor = OnAvatar,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onStudentClick(review.reviewer.id) }
                ) {
                    Text(
                        text = review.reviewer.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.testTag("mapReviewAuthorName")
                    )
                    if (review.time.isNotBlank()) {
                        Text(
                            text = review.time,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (review.rating != null) {
                    RatingPill(rating = review.rating)
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.cerrar),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // A quien se reseño y en que materia
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .clickable { onStudentClick(review.reviewedStudent.id) }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileIcon(
                    initials = review.reviewedStudent.initials,
                    profileImage = review.reviewedStudent.profileImage,
                    backgroundColor = review.reviewedStudent.profileColor,
                    contentColor = OnAvatar,
                    fontSize = 12.sp,
                    modifier = Modifier.size(30.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.resena_para),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.2.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = review.reviewedStudent.name,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.testTag("mapReviewTargetName")
                    )
                }
                if (review.classReviewed.isNotBlank() || review.periodReviewed.isNotBlank()) {
                    Column(horizontalAlignment = Alignment.End) {
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
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Contenido de la reseña
            Text(
                text = stringResource(R.string.contenido_citado, review.content),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.testTag("mapReviewContent")
            )

            Spacer(modifier = Modifier.height(2.dp))

            ButtonWithoutIcon(
                textoBoton = stringResource(R.string.ver_resena_completa).uppercase(),
                onClick = { onReviewClick(review.id) },
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("btnOpenReviewDetail")
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapReviewInfoCardPreview() {
    TheStudentsTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            MapReviewInfoCard(
                review = localReviewsProvider.allReviews[0],
                onReviewClick = {},
                onStudentClick = {},
                onDismiss = {}
            )
        }
    }
}
