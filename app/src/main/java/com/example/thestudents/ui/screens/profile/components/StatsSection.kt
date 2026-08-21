package com.example.thestudents.ui.screens.profile.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
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
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        StatItem("128", stringResource(R.string.seguidores_mayuscula))
        VerticalDivider(
            modifier = Modifier.height(40.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
        StatItem("96", stringResource(R.string.siguiendo_mayuscula))
        VerticalDivider(
            modifier = Modifier.height(40.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
        StatItem(student.reviewsCount.toString(), stringResource(R.string.resenas_mayuscula))
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

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StatsSectionDarkPreview() {
    TheStudentsTheme {
        StatsSection(
            student = localStudentProvider.currentUser
        )
    }
}
