package com.example.thestudents.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R

@Composable
fun ButtonWithoutIcon(
    textoBoton: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    fontSize: TextUnit,
    contentPadding: PaddingValues = PaddingValues()
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.dark_green)
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        Text(
            text = textoBoton,
            color = Color.White,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ButtonWithoutIconPreviewLogin() {
    ButtonWithoutIcon(
        textoBoton = "INGRESAR",
        onClick = {},
        fontSize = 16.sp,
        modifier = Modifier.fillMaxWidth()
            .height(56.dp)
    )
}

@Composable
@Preview(showBackground = true)
fun ButtonWithoutIconPreviewReview() {
    ButtonWithoutIcon(
        textoBoton = "Reseñar",
        onClick = {},
        fontSize = 12.sp,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    )
}