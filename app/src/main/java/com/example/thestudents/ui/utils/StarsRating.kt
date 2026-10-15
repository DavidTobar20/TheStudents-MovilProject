package com.example.thestudents.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.theme.extended

@Composable
fun StarsRating(
    modifier: Modifier = Modifier,
    rating: Int,
    onRatingSelected: ((Int) -> Unit)?
) {
    Row() {
        for (i in 1..5) {
            val isSelected = i <= rating
            Icon(
                imageVector = if (isSelected) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "Estrella $i",
                tint = if (isSelected) MaterialTheme.extended.rating else MaterialTheme.extended.ratingInactive,
                modifier = modifier
                    .then(
                    if (onRatingSelected != null) {
                        Modifier.clickable { onRatingSelected(i) }
                    } else {
                        Modifier
                    }
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StarsRatingPreviewStudentCard() {
    TheStudentsTheme() {
        StarsRating(
            modifier = Modifier.size(16.dp),
            rating = 3,
            onRatingSelected = null)
    }
}

@Preview(showBackground = true)
@Composable
fun StarsRatingPreviewReviewCard() {
    TheStudentsTheme() {
        StarsRating(
            modifier = Modifier.size(32.dp),
            rating = 3,
            onRatingSelected = {})
    }
}
