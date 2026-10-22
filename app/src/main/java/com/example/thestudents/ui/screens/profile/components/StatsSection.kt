package com.example.thestudents.ui.screens.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun StatsSection(
    student: Student,
    modifier: Modifier = Modifier,
    followersCount: Int = student.followersCount,
    followingCount: Int = student.followingCount,
    reviewsCount: Int = student.reviewsCount,
    onFollowersClick: (() -> Unit)? = null,
    onFollowingClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Box(modifier = if (onFollowersClick != null) Modifier.clickable(onClick = onFollowersClick) else Modifier) {
            StatItem(followersCount.toString(), stringResource(R.string.seguidores_mayuscula))
        }
        VerticalDivider(
            modifier = Modifier.height(40.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
        Box(modifier = if (onFollowingClick != null) Modifier.clickable(onClick = onFollowingClick) else Modifier) {
            StatItem(followingCount.toString(), stringResource(R.string.siguiendo_mayuscula))
        }
        VerticalDivider(
            modifier = Modifier.height(40.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
        StatItem(reviewsCount.toString(), stringResource(R.string.resenas_mayuscula))
    }
}

@Preview(showBackground = true)
@Composable
fun StatsSectionPreview() {
    TheStudentsTheme {
        StatsSection(
            student = localStudentProvider.currentUser
        )
    }
}
