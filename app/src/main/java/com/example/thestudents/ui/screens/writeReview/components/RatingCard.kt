package com.example.thestudents.ui.screens.writeReview.components

import com.example.thestudents.ui.components.luxuryCard
import com.example.thestudents.ui.components.SectionLabel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.StarsRating

@Composable
fun RatingCard(
    modifier: Modifier = Modifier,
    rating: Int,
    onRatingSelected: (Int) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .luxuryCard(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionLabel(text = stringResource(R.string.calificacion_opcional))
            Spacer(modifier = Modifier.height(10.dp))
            StarsRating(
                modifier = Modifier
                    .size(32.dp),
                rating = rating,
                onRatingSelected = onRatingSelected
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RatingCardPreview() {
    TheStudentsTheme() {
        RatingCard(
            rating = 3 ,
            onRatingSelected = {}
        )
    }
}