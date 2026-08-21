package com.example.thestudents.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R

import androidx.compose.material3.MaterialTheme
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun DiamondDivider(modifier: Modifier = Modifier) {
    val color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = color,
            thickness = 1.dp)
        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(10.dp)
                .rotate(45f)
                .background(color)
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = color,
            thickness = 1.dp)
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun DiamondDividerPreview() {
    TheStudentsTheme(darkTheme = false) {
        DiamondDivider()
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DiamondDividerDarkPreview() {
    TheStudentsTheme(darkTheme = true) {
        DiamondDivider()
    }
}
