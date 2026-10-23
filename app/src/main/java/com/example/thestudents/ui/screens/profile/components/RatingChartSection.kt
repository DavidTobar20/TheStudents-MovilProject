package com.example.thestudents.ui.screens.profile.components

import com.example.thestudents.R
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import com.example.thestudents.ui.components.SectionLabel
import com.example.thestudents.ui.components.luxuryCard
import kotlin.math.roundToInt
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.data.Review
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun RatingChartSection(
    receivedReviews: List<Review>, // reseñas que RECIBIO el estudiante
    modifier: Modifier = Modifier,
) {
    // Calificacion de cada reseña redondeada a 1..5 (ej. "4.5" -> 5). Las que no tienen calificacion no cuentan.
    val ratings = receivedReviews
        .mapNotNull { it.rating?.toFloatOrNull() }
        .map { it.roundToInt().coerceIn(1, 5) }
    val total = ratings.size

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .luxuryCard(RoundedCornerShape(20.dp)),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            SectionLabel(text = stringResource(R.string.calificaciones_recibidas))
            Spacer(modifier = Modifier.height(12.dp))
            // De 5 a 1 estrellas: la barra es el porcentaje de reseñas con esa calificacion
            for (star in 5 downTo 1) {
                val count = ratings.count { it == star }
                RatingBarItem(
                    rating = star,
                    progress = if (total == 0) 0f else count.toFloat() / total,
                    count = count
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RatingChartSectionPreview() {
    TheStudentsTheme {
        Surface {
            RatingChartSection(receivedReviews = localReviewsProvider.allReviews)
        }
    }
}
