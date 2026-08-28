package com.example.thestudents.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun CardReviewerInfo(
    student: Student,
    subtitle: String,
    rating: String?,
    modifier: Modifier = Modifier,
    onReviewerClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        ReviewerInfo(
            student = student,
            subtitle = subtitle,
            onClick = onReviewerClick
        )
        if(rating != null){
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rating,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardReviewerInfoPreviewWithRating(){
    val review = localReviewsProvider.allReviews[0]
    TheStudentsTheme {
        Surface {
            CardReviewerInfo(
                student = review.reviewedStudent,
                subtitle = "Materia: ${review.classReviewed}",
                rating = review.rating
            )
        }
    }
}
