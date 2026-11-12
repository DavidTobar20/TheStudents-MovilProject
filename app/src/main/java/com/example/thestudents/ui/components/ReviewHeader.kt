package com.example.thestudents.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.theme.OnAvatar
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.theme.extended
import com.example.thestudents.ui.utils.ProfileIcon

@Composable
fun ReviewHeader(
    modifier: Modifier = Modifier,
    reviewer: Student,
    time: String,
    rating: String?,
    onReviewerClick: (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    val clickModifier = if (onReviewerClick != null) Modifier.clickable(onClick = onReviewerClick) else Modifier

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar con aro dorado fino
        Box(
            modifier = Modifier
                .size(48.dp)
                .border(1.5.dp, MaterialTheme.extended.rating, CircleShape)
                .padding(3.dp)
                .clip(CircleShape)
                .then(clickModifier)
        ) {
            ProfileIcon(
                initials = reviewer.initials,
                profileImage = reviewer.profileImage,
                backgroundColor = reviewer.profileColor,
                contentColor = OnAvatar,
                fontSize = 15.sp,
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .then(clickModifier),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = reviewer.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                modifier = Modifier.testTag("reviewAuthorName")
            )
            if (time.isNotBlank()) {
                Text(
                    text = time,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (rating != null) {
            RatingPill(rating = rating)
        }
        if (trailingContent != null) {
            trailingContent()
        }
    }
}

// Pildora verde oscuro con estrella dorada: "★ 4,5"
@Composable
fun RatingPill(rating: String) {
    val value = rating.toDoubleOrNull()
    val ratingText = when {
        value == null -> rating
        value % 1.0 == 0.0 -> value.toInt().toString()
        else -> value.toString().replace('.', ',')
    }

    Row(
        modifier = Modifier
            .height(32.dp)
            .clip(CircleShape)
            .background(MaterialTheme.extended.ratingPill)
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = MaterialTheme.extended.ratingPillStar,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = ratingText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.extended.onRatingPill
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewHeaderPreview() {
    TheStudentsTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            ReviewHeader(
                reviewer = localStudentProvider.students[0],
                time = "Hace 2 horas",
                rating = "4.5",
                onReviewerClick = {}
            )
        }
    }
}
