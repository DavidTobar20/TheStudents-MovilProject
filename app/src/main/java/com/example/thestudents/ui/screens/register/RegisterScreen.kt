package com.example.thestudents.ui.screens.register

import androidx.compose.foundation.layout.*
<<<<<<< HEAD
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
=======
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
>>>>>>> origin/dark-mode
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
<<<<<<< HEAD
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
=======
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
>>>>>>> origin/dark-mode
import com.example.thestudents.ui.screens.register.components.*
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun RegisterScreen(
<<<<<<< HEAD
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
=======
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
>>>>>>> origin/dark-mode
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        confirmPassword = confirmPassword,
        onConfirmPasswordChange = { confirmPassword = it },
<<<<<<< HEAD
        onRegisterClick = onRegisterSuccess,
        onLoginClick = onLoginClick,
        onTermsClick = onTermsClick,
        onPrivacyClick = onPrivacyClick
=======
        isPasswordVisible = isPasswordVisible,
        onPasswordToggle = { isPasswordVisible = !isPasswordVisible },
        isConfirmPasswordVisible = isConfirmPasswordVisible,
        onConfirmPasswordToggle = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
        termsAccepted = termsAccepted,
        onTermsAcceptedChange = { termsAccepted = it },
        onRegisterClick = onRegisterClick,
        onSsoClick = onSsoClick,
        onNavigateToLogin = onNavigateToLogin
>>>>>>> origin/dark-mode
    )
}

@Composable
fun RegisterBody(
<<<<<<< HEAD
    name: String,
    onNameChange: (String) -> Unit,
=======
    names: String,
    onNamesChange: (String) -> Unit,
    lastNames: String,
    onLastNamesChange: (String) -> Unit,
>>>>>>> origin/dark-mode
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
<<<<<<< HEAD
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
=======
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
        color = colorResource(id = R.color.light_tan)
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
                        containerColor = colorResource(id = R.color.medium_green),
                        disabledContainerColor = colorResource(id = R.color.medium_green).copy(alpha = 0.5f)
                    )
                ) {
                    Text(
                        text = "CREAR CUENTA",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.white)
                        )
                    )
                }

                SocialRegisterOptions(onSsoClick = onSsoClick)

                RegisterFooter(onNavigateToLogin = onNavigateToLogin)
                
                Spacer(modifier = Modifier.height(24.dp))
            }
>>>>>>> origin/dark-mode
        }
    }
}

<<<<<<< HEAD
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
=======
@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    TheStudentsTheme {
        RegisterScreen(
            onRegisterClick = {},
            onSsoClick = {},
            onNavigateToLogin = {}
        )
>>>>>>> origin/dark-mode
    }
}
