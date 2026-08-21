package com.example.thestudents.ui.screens.register.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun RegisterHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.logosinfondo),
            contentDescription = "Logo The Students",
            modifier = Modifier.size(120.dp),
            contentScale = ContentScale.Fit
        )
        
        Text(
            text = "THE STUDENTS",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.dark_green),
                letterSpacing = 2.sp
            ),
            textAlign = TextAlign.Center
        )
        
        HorizontalDivider(
            modifier = Modifier
                .width(80.dp)
                .padding(vertical = 8.dp),
            thickness = 1.5.dp,
            color = colorResource(id = R.color.tan)
        )

        Text(
            text = "Crear nueva cuenta",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                color = colorResource(id = R.color.dark_green)
            ),
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = "Regístrate para acceder a los beneficios de tu cuenta institucional.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = colorResource(id = R.color.dark_green).copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterHeaderPreview() {
    TheStudentsTheme {
        RegisterHeader()
    }
}
