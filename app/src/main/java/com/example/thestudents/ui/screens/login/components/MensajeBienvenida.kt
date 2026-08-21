package com.example.thestudents.ui.screens.login.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun MensajeBienvenida(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.the_students_mayuscula),
        modifier = modifier,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Serif,
        color = MaterialTheme.colorScheme.primary,
        letterSpacing = 4.sp,
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun MensajeBienvenidaPreview() {
    TheStudentsTheme(darkTheme = false) {
        MensajeBienvenida()
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MensajeBienvenidaDarkPreview() {
    TheStudentsTheme(darkTheme = true) {
        MensajeBienvenida()
    }
}
