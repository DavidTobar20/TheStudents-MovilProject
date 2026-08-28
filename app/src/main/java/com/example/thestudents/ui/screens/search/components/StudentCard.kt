package com.example.thestudents.ui.screens.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ButtonWithoutIcon
import com.example.thestudents.ui.utils.ProfileIcon
import com.example.thestudents.ui.utils.StarsRating

@Composable
fun StudentCard(
    student: Student,
    onClick: () -> Unit,
    onWriteReviewClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileIcon(
                initials = student.initials,
                profileImage = student.profileImage,
                backgroundColor = student.profileColor,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                fontSize = 20.sp,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onClick)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onClick)
            ) {
                Text(
                    text = student.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = pluralStringResource(
                        R.plurals.programa_y_resenas,
                        student.reviewsCount,
                        student.program,
                        student.reviewsCount
                    ),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
                )
                StarsRating(
                    modifier = Modifier.size(16.dp),
                    rating = student.rating.toInt(),
                    onRatingSelected = null
                )
            }

            ButtonWithoutIcon(
                textoBoton = stringResource(R.string.resenar),
                onClick = onWriteReviewClick,
                fontSize = 11.sp,
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentCardPreview() {
    TheStudentsTheme {
        Surface {
            StudentCard(
                student = localStudentProvider.students[4],
                onClick = {},
                onWriteReviewClick = {}
            )
        }
    }
}
