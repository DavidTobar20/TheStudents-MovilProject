package com.example.thestudents.ui.screens.login.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.thestudents.R

@Composable
fun MensajeBienvenida(modifier: Modifier = Modifier) {
    Text(
        text = "THE STUDENTS",
        modifier = modifier,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Serif,
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