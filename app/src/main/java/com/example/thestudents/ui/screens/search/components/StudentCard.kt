package com.example.thestudents.ui.screens.search.components

import com.example.thestudents.ui.theme.extended
import com.example.thestudents.ui.theme.OnAvatar
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.border
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.FollowButton
import com.example.thestudents.ui.utils.ProfileIcon
import com.example.thestudents.ui.utils.StarsRating
import kotlin.math.roundToInt

@Composable
fun StudentCard(
    student: Student,
    isFollowing: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showFollowButton: Boolean = true,
    onFollowToggle: (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(20.dp)

    // Misma sombra y borde que la tarjeta de reseña
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .shadow(
                elevation = 10.dp,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.08f),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
            )
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), shape)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar con aro dorado fino
        Box(
            modifier = Modifier
                .size(56.dp)
                .border(1.5.dp, MaterialTheme.extended.rating, CircleShape)
                .padding(3.dp)
                .clip(CircleShape)
        ) {
            ProfileIcon(
                initials = student.initials,
                profileImage = student.profileImage,
                backgroundColor = student.profileColor,
                contentColor = OnAvatar,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = student.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1
            )
            if (student.program.isNotBlank()) {
                Text(
                    text = student.program,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            // Estrellas y cantidad de reseñas en la misma linea
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StarsRating(
                    modifier = Modifier.size(14.dp),
                    rating = student.rating.roundToInt(),
                    onRatingSelected = null
                )
                Text(
                    text = pluralStringResource(R.plurals.resenas_count, student.reviewsCount, student.reviewsCount),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (showFollowButton && onFollowToggle != null) {
            Spacer(modifier = Modifier.width(8.dp))
            FollowButton(
                isFollowing = isFollowing,
                onFollowToggle = onFollowToggle,
                fontSize = 11.sp,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
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
                isFollowing = false,
                onClick = {}
            ) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentCardFollowingPreview() {
    TheStudentsTheme {
        Surface {
            StudentCard(
                student = localStudentProvider.students[4],
                isFollowing = true,
                onClick = {}
            ) {}
        }
    }
}
