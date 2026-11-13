package com.example.thestudents.ui.screens.map.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.thestudents.R
import com.example.thestudents.data.Review
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.ui.theme.OnAvatar
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ProfileIcon

@Composable
fun MapMultipleReviewsDialog(
    reviews: List<Review>,
    onReviewSelected: (Review) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("mapMultipleReviewsDialog")
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.resenas_en_esta_ubicacion),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(reviews) { review ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onReviewSelected(review) }
                                .padding(10.dp)
                                .testTag("mapMultipleReviewItem"),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProfileIcon(
                                initials = review.reviewer.initials,
                                profileImage = review.reviewer.profileImage,
                                backgroundColor = review.reviewer.profileColor,
                                contentColor = OnAvatar,
                                fontSize = 12.sp,
                                modifier = Modifier.size(36.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${review.reviewer.name} → ${review.reviewedStudent.name}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${review.classReviewed} • ${review.periodReviewed}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapMultipleReviewsDialogPreview() {
    TheStudentsTheme {
        Surface {
            MapMultipleReviewsDialog(
                reviews = localReviewsProvider.allReviews.take(3),
                onReviewSelected = {},
                onDismiss = {}
            )
        }
    }
}
