package com.example.thestudents.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.utils.ButtonWithoutLogo
import com.example.thestudents.ui.screens.login.components.ContinueWithDivider
import com.example.thestudents.ui.utils.DiamondDivider
import com.example.thestudents.ui.screens.login.components.FormularioRegistro
import com.example.thestudents.ui.screens.login.components.LogoApp
import com.example.thestudents.ui.screens.login.components.MensajeBienvenida
import com.example.thestudents.ui.utils.ButtonWithLogo
import com.example.thestudents.ui.theme.TheStudentsTheme


@Composable
fun BodyLoginScreen(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onSSOClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.cream))
            .padding(horizontal = 32.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LogoApp()
        
        Spacer(modifier = Modifier.height(8.dp))
        
        MensajeBienvenida()
        
        DiamondDivider(modifier = Modifier.padding(horizontal = 48.dp))
        
        Text(
            text = "Inicia sesión para continuar",
            color = colorResource(R.color.medium_green).copy(alpha = 0.7f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        FormularioRegistro(
            email = email,
            onEmailChange = onEmailChange,
            password = password,
            onPasswordChange = onPasswordChange,
            onForgotPasswordClick = onForgotPasswordClick
        )

        Spacer(modifier = Modifier.height(32.dp))

        ButtonWithoutLogo(
            textoBoton = "INGRESAR",
            onClick = onLoginClick,
            fontSize = 16,
            modifier = Modifier.fillMaxWidth()
                .height(56.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        ContinueWithDivider()

        ButtonWithLogo(
            text = "Cuenta institucional (SSO)",
            iconRes = R.drawable.logosinfondo,
            onClick = onSSOClick,
            borderColor = colorResource(R.color.medium_green),
            contentColor = colorResource(R.color.dark_green),
            modifier = Modifier.height(56.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row {
            Text(text = "¿No tienes una cuenta? ", fontSize = 14.sp, color = colorResource(R.color.medium_green))
            Text(
                text = "Crear cuenta",
                modifier = Modifier.clickable { onCreateAccountClick() },
                color = colorResource(R.color.dark_green),
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun BodyLoginScreenPreview() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    TheStudentsTheme {
        BodyLoginScreen(
            email = email,
            onEmailChange = { email = it },
            password = password,
            onPasswordChange = { password = it },
            onLoginClick = {},
            onSSOClick = {},
            onCreateAccountClick = {},
            onForgotPasswordClick = {}
        )
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit = {},
    onSSOClick: () -> Unit = {},
    onCreateAccountClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    BodyLoginScreen(
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        onLoginClick = onLoginSuccess,
        onSSOClick = onSSOClick,
        onCreateAccountClick = onCreateAccountClick,
        onForgotPasswordClick = onForgotPasswordClick,
        modifier = modifier
    )
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    TheStudentsTheme {
        LoginScreen()
    }
}
