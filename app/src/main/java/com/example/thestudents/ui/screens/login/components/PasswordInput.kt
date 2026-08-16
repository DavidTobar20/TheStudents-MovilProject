package com.example.thestudents.ui.screens.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R

/**
 * PasswordInput (Stateless para datos, Stateful para UI interna)
 *
 * 1. El ESTADO DE DATOS (password) está elevado porque el padre necesita validarlo.
 * 2. El ESTADO DE UI (passwordVisible) es interno porque al padre no le interesa
 *    saber si el usuario está viendo los puntitos o el texto, es un detalle visual.
 */
@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    password: String, // Elevado (Hoisted)
    onPasswordChange: (String) -> Unit // Elevado (Hoisted)
) {
    // Estado interno: Solo afecta a cómo se ve este componente, no al negocio de la app.
    var passwordVisible by remember { mutableStateOf(false) }
    
    val icono = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
    val description = if (passwordVisible) stringResource(R.string.ocultar_contrasena) else stringResource(
        R.string.mostrar_contrasena
    )

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.contrasena),
            color = colorResource(R.color.dark_green),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = {
                Text(
                    stringResource(R.string.ingresa_tu_contrasena),
                    color = colorResource(R.color.sage)
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    tint = colorResource(R.color.dark_green)
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = icono,
                        contentDescription = description
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.sage),
                unfocusedBorderColor = colorResource(R.color.sage).copy(alpha = 0.5f),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordInputPreview() {
    var password by remember { mutableStateOf("") }
    PasswordInput(
        password = password,
        onPasswordChange = { password = it }
    )
}
