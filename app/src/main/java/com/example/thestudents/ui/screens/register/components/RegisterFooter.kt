package com.example.thestudents.ui.screens.register.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun RegisterFooter(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¿Ya tienes una cuenta? ",
            style = MaterialTheme.typography.bodyMedium.copy(color = colorResource(id = R.color.dark_green))
        )
        Text(
            text = "Inicia sesión",
            modifier = Modifier.clickable { onNavigateToLogin() },
            style = MaterialTheme.typography.bodyMedium.copy(
                color = colorResource(id = R.color.medium_green),
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterFooterPreview() {
    TheStudentsTheme {
        RegisterFooter(onNavigateToLogin = {})
    }
}
