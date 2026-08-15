package com.example.thestudents.ui.screens.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RatingChartSection(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            val ratings = listOf(0.7f, 0.2f, 0.1f, 0.05f, 0.02f)
            ratings.forEachIndexed { index, progress ->
                RatingBarItem(
                    rating = 5 - index,
                    progress = progress
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RatingChartSectionPreview() {
    RatingChartSection()
}
