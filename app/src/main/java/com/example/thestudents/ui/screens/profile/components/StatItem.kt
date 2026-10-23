package com.example.thestudents.ui.screens.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun StatItem(
    count: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        // Etiqueta en letras doradas pequeñas y espaciadas
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.tertiary,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.2.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatItemPreview() {
    TheStudentsTheme {
        Surface {
            StatItem(count = "128", label = "SEGUIDORES")
        }
    }
}
