package com.example.thestudents.ui.screens.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.data.Review
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.ui.utils.ProfileIcon

@Composable
fun ReviewItem(
    review: Review,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            ProfileIcon(
                initials = review.reviewer.initials,
                profileImage = review.reviewer.profileImage,
                backgroundColor = colorResource(R.color.dark_green),
                contentColor = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = review.reviewer.name,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.dark_green),
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${review.classReviewed}, ${review.periodReviewed}  ·  ${review.time}",
                        color = colorResource(R.color.sage),
                        fontSize = 10.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "“${review.content}”",
                    fontSize = 14.sp,
                    color = colorResource(R.color.medium_green).copy(alpha = 0.9f),
                    fontStyle = FontStyle.Italic
                )
            }
            IconButton(
                onClick = { /* Opciones */ },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = null,
                    tint = colorResource(R.color.sage)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewItemPreview() {
    ReviewItem(review = localReviewsProvider.allReviews[0])
}
