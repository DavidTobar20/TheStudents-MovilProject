package com.example.thestudents.ui.screens.register.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

import androidx.compose.ui.tooling.preview.Preview
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun RegisterFooter(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(vertical = 16.dp)
    ) {
        Text(
            text = "¿Ya tienes una cuenta? ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Inicia sesión",
            modifier = Modifier.clickable { onLoginClick() },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun RegisterFooterPreview() {
    TheStudentsTheme(darkTheme = false) {
        RegisterFooter(onLoginClick = {})
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RegisterFooterDarkPreview() {
    TheStudentsTheme(darkTheme = true) {
        RegisterFooter(onLoginClick = {})
    }
}
