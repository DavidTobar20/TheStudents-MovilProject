package com.example.thestudents.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.ui.theme.TheStudentsTheme

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
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        Text(
            text = textoBoton,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ButtonWithoutIconPreviewLogin() {
    TheStudentsTheme {
        Surface {
            ButtonWithoutIcon(
                textoBoton = "INGRESAR",
                onClick = {},
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth()
                    .height(56.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ButtonWithoutIconPreviewReview() {
    TheStudentsTheme {
        Surface {
            ButtonWithoutIcon(
                textoBoton = "Reseñar",
                onClick = {},
                fontSize = 12.sp,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}