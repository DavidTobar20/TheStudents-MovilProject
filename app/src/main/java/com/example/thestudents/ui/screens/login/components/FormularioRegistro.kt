package com.example.thestudents.ui.screens.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.TextFieldWhitIcon

@Composable
fun FormularioRegistro (
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onPasswordToggle: () -> Unit,
    onForgotPasswordClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
    ) {

        TextFieldWhitIcon(
            value = email,
            onValueChange = onEmailChange,
            label = stringResource(R.string.correo_institucional),
            placeholder = stringResource(R.string.ejemplo_email),
            leadingIcon = Icons.Default.Email,
            isPasswordField = false,
            isPasswordVisible = false,
            onPasswordToggle = {}
        )

        Spacer(modifier = Modifier.height(20.dp))


        TextFieldWhitIcon(
            value = password,
            onValueChange = onPasswordChange,
            label = stringResource(R.string.contrasena),
            placeholder = stringResource(R.string.ingresa_tu_contrasena),
            leadingIcon = Icons.Default.Lock,
            isPasswordField = true,
            isPasswordVisible = isPasswordVisible,
            onPasswordToggle = onPasswordToggle
        )

        Text(
            text = stringResource(R.string.olvidaste_tu_contrasena),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 12.sp,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 8.dp)
                .clickable { onForgotPasswordClick() }
        )

    }
}

@Preview(showBackground = true)
@Composable
fun FormularioRegistroPreview() {
    TheStudentsTheme {
        Surface {
            // En los Preview, creamos el estado localmente para poder visualizar el componente
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            FormularioRegistro(
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                onPasswordToggle = {},
                onForgotPasswordClick = {},
                isPasswordVisible = false
            )
        }
    }
}
