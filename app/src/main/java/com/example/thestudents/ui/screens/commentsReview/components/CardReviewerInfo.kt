package com.example.thestudents.ui.screens.commentsReview.components

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
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun CardReviewerInfo(
    nameReviewer: String,
    usernameReviewer: String,
    timeAgo: String,
    initialsReviewer: String,
    rating: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ReviewerInfo(
            initialsReviewer = initialsReviewer,
            nameReviewer = nameReviewer,
            usernameReviewer = usernameReviewer,
            timeAgo = timeAgo
        )
        if(rating != null){
            Box(
                modifier = Modifier
                    .padding(top = 5.dp)
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
    TheStudentsTheme {
        Surface {
            CardReviewerInfo(
                nameReviewer = "Luisa Mendoza",
                usernameReviewer = "luisa.mendoza",
                timeAgo = "hace 6 h",
                initialsReviewer = "LM",
                rating = "4.5"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardReviewerInfoPreviewWithoutRating(){
    TheStudentsTheme {
        Surface {
            CardReviewerInfo(
                nameReviewer = "Luisa Mendoza",
                usernameReviewer = "luisa.mendoza",
                timeAgo = "hace 6 h",
                initialsReviewer = "LM",
                rating = null
            )
        }
    }
}