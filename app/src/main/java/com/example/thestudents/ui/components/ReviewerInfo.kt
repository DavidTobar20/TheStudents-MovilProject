package com.example.thestudents.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.ui.theme.OnAvatar
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ProfileIcon

@Composable
fun ReviewerInfo(
    modifier: Modifier = Modifier,
    student: Student,
    subtitle: String, // Cambiado de timeAgo a subtitle genérico para mayor flexibilidad
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileIcon(
            initials = student.initials,
            profileImage = student.profileImage,
            backgroundColor = student.profileColor,
            contentColor = OnAvatar,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(end = 12.dp)
                .size(40.dp)
        )
        Column {
            Text(
                text = student.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewerInfoPreview() {
    val review = localReviewsProvider.allReviews[0]
    TheStudentsTheme {
        Surface {
            ReviewerInfo(
                student = review.reviewedStudent,
                subtitle = "Materia: ${review.classReviewed}"
            )
        }
    }
}
