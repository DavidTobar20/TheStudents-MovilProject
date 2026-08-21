package com.example.thestudents.ui.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.ui.screens.register.components.*
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    RegisterBody(
        name = name,
        onNameChange = { name = it },
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        confirmPassword = confirmPassword,
        onConfirmPasswordChange = { confirmPassword = it },
        onRegisterClick = onRegisterSuccess,
        onLoginClick = onLoginClick,
        onTermsClick = onTermsClick,
        onPrivacyClick = onPrivacyClick
    )
}

@Composable
fun RegisterBody(
    name: String,
    onNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            
            RegisterHeader()
            
            Spacer(modifier = Modifier.height(32.dp))
            
            RegisterTextField(
                value = name,
                onValueChange = onNameChange,
                label = "Nombre completo",
                leadingIcon = Icons.Default.Person
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            RegisterTextField(
                value = email,
                onValueChange = onEmailChange,
                label = "Correo institucional",
                leadingIcon = Icons.Default.Email
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            RegisterTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = "Contraseña",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            RegisterTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = "Confirmar contraseña",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            TermsAndConditions(
                onTermsClick = onTermsClick,
                onPrivacyClick = onPrivacyClick
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("REGISTRARSE")
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            RegisterFooter(onLoginClick = onLoginClick)
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
@Preview(showBackground = true, name = "Light Mode")
fun RegisterScreenPreview() {
    TheStudentsTheme(darkTheme = false) {
        RegisterScreen()
    }
}

@Composable
@Preview(showBackground = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
fun RegisterScreenDarkPreview() {
    TheStudentsTheme(darkTheme = true) {
        RegisterScreen()
    }
}
