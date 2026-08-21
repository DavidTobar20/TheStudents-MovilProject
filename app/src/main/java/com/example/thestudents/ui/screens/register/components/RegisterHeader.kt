package com.example.thestudents.ui.screens.register.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
<<<<<<< HEAD
import androidx.compose.material3.MaterialTheme
=======
import androidx.compose.material3.HorizontalDivider
>>>>>>> origin/dark-mode
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
<<<<<<< HEAD
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.R
=======
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme
>>>>>>> origin/dark-mode

@Composable
fun RegisterHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
<<<<<<< HEAD
        Image(
            painter = painterResource(id = R.drawable.logosinfondo),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.crear_cuenta),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Únete a la comunidad estudiantil",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
=======
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
            style = TextStyle(
                fontSize = 24.sp,
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
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colorResource(id = R.color.dark_green)
            ),
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = "Regístrate para acceder a los beneficios de tu cuenta institucional.",
            style = TextStyle(
                fontSize = 14.sp,
                color = colorResource(id = R.color.dark_green).copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
>>>>>>> origin/dark-mode
        )
    }
}

<<<<<<< HEAD
@Preview(showBackground = true, name = "Light Mode")
@Composable
fun RegisterHeaderPreview() {
    TheStudentsTheme(darkTheme = false) {
        RegisterHeader(modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RegisterHeaderDarkPreview() {
    TheStudentsTheme(darkTheme = true) {
        RegisterHeader(modifier = Modifier.padding(16.dp))
=======
@Preview(showBackground = true)
@Composable
fun RegisterHeaderPreview() {
    TheStudentsTheme {
        RegisterHeader()
>>>>>>> origin/dark-mode
    }
}
