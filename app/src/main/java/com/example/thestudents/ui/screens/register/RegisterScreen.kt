package com.example.thestudents.ui.screens.register

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.ui.screens.register.components.*
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun RegisterScreen(
    onRegisterClick: () -> Unit,
    onSsoClick: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    // State management (State Hoisting)
    var names by remember { mutableStateOf("") }
    var lastNames by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var termsAccepted by remember { mutableStateOf(false) }

    RegisterBody(
        names = names,
        onNamesChange = { names = it },
        lastNames = lastNames,
        onLastNamesChange = { lastNames = it },
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        confirmPassword = confirmPassword,
        onConfirmPasswordChange = { confirmPassword = it },
        isPasswordVisible = isPasswordVisible,
        onPasswordToggle = { isPasswordVisible = !isPasswordVisible },
        isConfirmPasswordVisible = isConfirmPasswordVisible,
        onConfirmPasswordToggle = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
        termsAccepted = termsAccepted,
        onTermsAcceptedChange = { termsAccepted = it },
        onRegisterClick = onRegisterClick,
        onSsoClick = onSsoClick,
        onNavigateToLogin = onNavigateToLogin
    )
}

@Composable
fun RegisterBody(
    names: String,
    onNamesChange: (String) -> Unit,
    lastNames: String,
    onLastNamesChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onPasswordToggle: () -> Unit,
    isConfirmPasswordVisible: Boolean,
    onConfirmPasswordToggle: () -> Unit,
    termsAccepted: Boolean,
    onTermsAcceptedChange: (Boolean) -> Unit,
    onRegisterClick: () -> Unit,
    onSsoClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                RegisterHeader()

                // Form Fields
                RegisterTextField(
                    value = names,
                    onValueChange = onNamesChange,
                    label = "Nombres",
                    placeholder = "Ingresa tus nombres completos",
                    leadingIcon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(16.dp))

                RegisterTextField(
                    value = lastNames,
                    onValueChange = onLastNamesChange,
                    label = "Apellidos",
                    placeholder = "Ingresa tus apellidos completos",
                    leadingIcon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(16.dp))

                RegisterTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = "Correo institucional",
                    placeholder = "nombre@universidad.edu.co",
                    leadingIcon = Icons.Default.Email,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(16.dp))

                RegisterTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = "Crea tu contraseña",
                    placeholder = "Crea una contraseña segura",
                    leadingIcon = Icons.Default.Lock,
                    isPasswordField = true,
                    isPasswordVisible = isPasswordVisible,
                    onPasswordToggle = onPasswordToggle
                )

                Spacer(modifier = Modifier.height(16.dp))

                RegisterTextField(
                    value = confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = "Confirma tu contraseña",
                    placeholder = "Repite tu contraseña",
                    leadingIcon = Icons.Default.Lock,
                    isPasswordField = true,
                    isPasswordVisible = isConfirmPasswordVisible,
                    onPasswordToggle = onConfirmPasswordToggle
                )

                Spacer(modifier = Modifier.height(16.dp))

                TermsAndConditions(
                    termsAccepted = termsAccepted,
                    onTermsAcceptedChange = onTermsAcceptedChange
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Register Button
                Button(
                    onClick = onRegisterClick,
                    enabled = termsAccepted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                    )
                ) {
                    Text(
                        text = "CREAR CUENTA",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                }

                SocialRegisterOptions(onSsoClick = onSsoClick)

                RegisterFooter(onNavigateToLogin = onNavigateToLogin)
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun RegisterScreenPreview() {
    TheStudentsTheme {
        RegisterScreen(
            onRegisterClick = {},
            onSsoClick = {},
            onNavigateToLogin = {}
        )
    }
}
