package com.example.thestudents.ui.screens.map.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.data.Review
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.ui.components.ReviewHeader
import com.example.thestudents.ui.components.ReviewQuote
import com.example.thestudents.ui.components.ReviewTarget
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ButtonWithoutIcon

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
            // Encabezado reutilizando ReviewHeader
            ReviewHeader(
                reviewer = review.reviewer,
                time = review.time,
                rating = review.rating,
                onReviewerClick = { onStudentClick(review.reviewer.id) },
                trailingContent = {
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
            )

            // A quien se reseño reutilizando ReviewTarget
            ReviewTarget(
                review = review,
                onClick = { onStudentClick(review.reviewedStudent.id) }
            )

            // Cita de la reseña reutilizando ReviewQuote con maximo 3 lineas
            ReviewQuote(
                content = review.content,
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
