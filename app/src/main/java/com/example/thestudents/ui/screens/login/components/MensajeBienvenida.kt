package com.example.thestudents.ui.screens.login.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.thestudents.R

@Composable
fun MensajeBienvenida(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.the_students_mayuscula),
        modifier = modifier,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = colorResource(R.color.dark_green),
        letterSpacing = 4.sp,
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
fun MensajeBienvenidaPreview() {
    MensajeBienvenida()
}
