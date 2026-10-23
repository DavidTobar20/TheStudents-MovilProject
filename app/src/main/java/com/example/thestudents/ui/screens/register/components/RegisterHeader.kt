package com.example.thestudents.ui.screens.register.components

import com.example.thestudents.ui.utils.DiamondDivider
import com.example.thestudents.ui.screens.login.components.MensajeBienvenida
import com.example.thestudents.ui.screens.login.components.LogoApp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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

        // Mismo encabezado que el Login: logo, nombre y divisor con rombo
        LogoApp()
        Spacer(modifier = Modifier.height(8.dp))
        MensajeBienvenida()
        DiamondDivider(modifier = Modifier.padding(horizontal = 48.dp))

        Text(
            text = stringResource(R.string.crear_nueva_cuenta),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = stringResource(R.string.registrate_beneficios),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
