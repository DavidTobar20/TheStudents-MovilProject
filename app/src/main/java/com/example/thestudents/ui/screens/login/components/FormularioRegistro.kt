package com.example.thestudents.ui.screens.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R

@Composable
fun FormularioRegistro (
    modifier: Modifier = Modifier,
    email: String, // Valor recibido del padre
    password: String,
    onEmailChange: (String) -> Unit, // Callback para notificar cambios
    onPasswordChange: (String) -> Unit,
    onForgotPasswordClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
    ) {

        EmailInput(email = email, onEmailChange = onEmailChange)

        Spacer(modifier = Modifier.height(20.dp))


        PasswordInput(password = password, onPasswordChange = onPasswordChange)

        Text(
            text = stringResource(R.string.olvidaste_tu_contrasena),
            color = colorResource(R.color.dark_green),
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
    // En los Preview, creamos el estado localmente para poder visualizar el componente
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    FormularioRegistro(
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it }
    )
}
