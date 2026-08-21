package com.example.thestudents.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.utils.ButtonWithoutIcon
import com.example.thestudents.ui.screens.login.components.ContinueWithDivider
import com.example.thestudents.ui.utils.DiamondDivider
import com.example.thestudents.ui.screens.login.components.FormularioRegistro
import com.example.thestudents.ui.screens.login.components.LogoApp
import com.example.thestudents.ui.screens.login.components.MensajeBienvenida
import com.example.thestudents.ui.utils.ButtonWithIcon
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
    Box(
        modifier = modifier
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .align(Alignment.Center)
                .padding(horizontal = 32.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally

        ) {
            LogoApp()
            Spacer(modifier = Modifier.height(8.dp))
            MensajeBienvenida()
            DiamondDivider(
                modifier = Modifier.padding(horizontal = 48.dp)
            )

            Text(
                text = stringResource(R.string.inicia_sesion_para_continuar),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Pasamos el estado hacia abajo
            FormularioRegistro(
                email = email,
                onEmailChange = onEmailChange,
                password = password,
                onPasswordChange = onPasswordChange,
                onForgotPasswordClick = onForgotPasswordClick
            )

            Spacer(modifier = Modifier.height(32.dp))

            ButtonWithoutIcon(
                textoBoton = stringResource(R.string.ingresar_mayuscula),
                onClick = onLoginClick,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
            ContinueWithDivider()

            ButtonWithIcon(
                text = stringResource(R.string.cuenta_institucional_sso),
                icon = Icons.Default.Home,
                onClick = onSSOClick,
                borderColor = MaterialTheme.colorScheme.primary,
                contentColor = if (androidx.compose.foundation.isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                modifier = Modifier.height(56.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row {
                Text(
                    text = stringResource(R.string.no_tienes_una_cuenta),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(R.string.crear_cuenta),
                    modifier = Modifier.clickable { onCreateAccountClick() },
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    fontSize = 14.sp
                )
            }
        }
    }
}


@Composable
@Preview(showBackground = true, name = "Light Mode")
fun BodyLoginScreenPreview() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    TheStudentsTheme(darkTheme = false) {
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
@Preview(showBackground = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
fun BodyLoginScreenDarkPreview() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    TheStudentsTheme(darkTheme = true) {
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

/**
 * LOGIN SCREEN (Stateful - Con estado)
 * 
 * Este es el "Padre"  Aquí es donde se declaran las
 * variables que controlan toda la pantalla.
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit = {},
    onSSOClick: () -> Unit = {},
    onCreateAccountClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    // Declaración del estado (State Hoisting: el estado vive aquí)
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
@Preview(showBackground = true, name = "Light Mode")
fun LoginScreenPreview() {
    TheStudentsTheme(darkTheme = false) {
        LoginScreen()
    }
}

@Composable
@Preview(showBackground = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
fun LoginScreenDarkPreview() {
    TheStudentsTheme(darkTheme = true) {
        LoginScreen()
    }
}
