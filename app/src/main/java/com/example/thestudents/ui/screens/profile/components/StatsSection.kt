package com.example.thestudents.ui.screens.profile.components

import com.example.thestudents.ui.theme.extended
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.Alignment
import com.example.thestudents.ui.components.luxuryCard
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

// Estilo Threads: los contadores van en una sola linea de texto, sin tarjeta.
// Seguidores y siguiendo se pueden tocar para abrir la lista.
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
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatText(
            count = followersCount,
            label = pluralStringResource(R.plurals.seguidores_label, followersCount),
            onClick = onFollowersClick
        )
        StatDot()
        StatText(
            count = followingCount,
            label = stringResource(R.string.siguiendo_mayuscula).lowercase(),
            onClick = onFollowingClick
        )
        StatDot()
        StatText(
            count = reviewsCount,
            label = pluralStringResource(R.plurals.resenas_label, reviewsCount),
            onClick = null
        )
    }
}

@Composable
private fun StatText(count: Int, label: String, onClick: (() -> Unit)?) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 4.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = count.toString(),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StatDot() {
    Text(
        text = "·",
        fontSize = 15.sp,
        color = MaterialTheme.extended.rating,
        modifier = Modifier.padding(horizontal = 2.dp)
    )
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
