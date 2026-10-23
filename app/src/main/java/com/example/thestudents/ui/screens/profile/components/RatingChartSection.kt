package com.example.thestudents.ui.screens.profile.components

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
            .padding(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
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
