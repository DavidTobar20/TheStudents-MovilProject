package com.example.thestudents.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.RatingBadge

@Composable
fun CardReviewerInfo(
    modifier: Modifier = Modifier,
    student: Student,
    subtitle: String,
    rating: String?,
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
            onClick = onReviewerClick,
            modifier = Modifier.weight(1f, fill = false)
        )
        if (rating != null) {
            Spacer(modifier = Modifier.width(8.dp))
            RatingBadge(rating = rating)
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
