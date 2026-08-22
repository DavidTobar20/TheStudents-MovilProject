package com.example.thestudents.ui.screens.login

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.thestudents.ui.screens.login.components.ContinueWithDivider
import com.example.thestudents.ui.screens.login.components.FormularioRegistro
import com.example.thestudents.ui.screens.login.components.LogoApp
import com.example.thestudents.ui.screens.login.components.MensajeBienvenida
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ButtonWithIcon
import com.example.thestudents.ui.utils.ButtonWithoutIcon
import com.example.thestudents.ui.utils.DiamondDivider


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
            .padding(horizontal = 32.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoApp()
        Spacer(modifier = Modifier.height(8.dp))
        MensajeBienvenida()
        DiamondDivider(modifier = Modifier.padding(horizontal = 48.dp))

        Text(
            text = stringResource(R.string.inicia_sesion_para_continuar),
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
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
            borderColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.height(56.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row {
            Text(
                text = stringResource(R.string.no_tienes_una_cuenta),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary
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


@Composable
@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
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
    // El estado vive aquí y baja al contenido (state hoisting).
    // El correo se guarda para que sobreviva a un giro de pantalla; la contraseña no, para no
    // dejarla en texto plano dentro del estado guardado de la instancia.
    var email by rememberSaveable { mutableStateOf("") }
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
@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun LoginScreenPreview() {
    TheStudentsTheme {
        LoginScreen()
    }
}
