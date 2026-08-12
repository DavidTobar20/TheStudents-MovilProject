package com.example.thestudents.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.thestudents.ui.theme.TheStudentsTheme

//composables: son funciones que tienen un componente gráfico de mi pantalla como piezas de rompecabezas
@Composable
fun MensajeBienvenida(modifier: Modifier = Modifier) {
    Text(
        text = "Bienvenido a The Students",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun MensajeBienvenidaPreview() {
    TheStudentsTheme {
        MensajeBienvenida()
    }
}
