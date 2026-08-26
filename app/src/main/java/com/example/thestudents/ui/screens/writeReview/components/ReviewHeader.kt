package com.example.thestudents.ui.screens.writeReview.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ProfileIcon

@Composable
fun ReviewHeader(
    modifier: Modifier = Modifier,
    name: String,
    initials: String,
    courseInfo: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileIcon(
            modifier = Modifier.size(43.dp),
            initials = initials,
            profileImage = null,
            backgroundColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = name,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp)
            Text(
                text = courseInfo,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ReviewHeaderPreview() {
    TheStudentsTheme() {
        ReviewHeader(
            name = "Juan Pérez",
            initials = "JP",
            courseInfo = "Física Mecánica (FISI1027) · 2025-2",
        )
    }
}
