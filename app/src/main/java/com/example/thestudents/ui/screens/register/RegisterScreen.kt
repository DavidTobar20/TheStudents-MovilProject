package com.example.thestudents.ui.screens.register

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.ui.screens.register.components.*
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.R
import com.example.thestudents.ui.utils.TextFieldWhitIcon


@Composable
fun RegisterScreen(
    onRegisterClick: () -> Unit,
    onSsoClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
    registerViewModel: RegisterViewModel = viewModel()
) {
    val state by registerViewModel.uiState.collectAsState()

    RegisterBody(
        names = state.names,
        onNamesChange = { registerViewModel.onNamesChange(it) },
        lastNames = state.lastNames,
        onLastNamesChange = { registerViewModel.onLastNamesChange(it) },
        email = state.email,
        onEmailChange = { registerViewModel.onEmailChange(it) },
        password = state.password,
        onPasswordChange = { registerViewModel.onPasswordChange(it) },
        confirmPassword = state.confirmPassword,
        onConfirmPasswordChange = { registerViewModel.onConfirmPasswordChange(it) },
        isPasswordVisible = state.isPasswordVisible,
        onPasswordToggle = { registerViewModel.onPasswordToggle() },
        isConfirmPasswordVisible = state.isConfirmPasswordVisible,
        onConfirmPasswordToggle = { registerViewModel.onConfirmPasswordToggle() },
        termsAccepted = state.termsAccepted,
        onTermsAcceptedChange = { registerViewModel.onTermsAcceptedChange(it) },
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
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            RegisterHeader()

            // Form Fields
            TextFieldWhitIcon(
                value = names,
                onValueChange = onNamesChange,
                label = stringResource(R.string.nombres),
                placeholder = stringResource(R.string.ingresa_tus_nombres_completos),
                leadingIcon = Icons.Default.Person,
                isPasswordField = false,
                isPasswordVisible = false,
                onPasswordToggle = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldWhitIcon(
                value = lastNames,
                onValueChange = onLastNamesChange,
                label = stringResource(R.string.apellidos),
                placeholder = stringResource(R.string.ingresa_tus_apellidos_completos),
                leadingIcon = Icons.Default.Person,
                isPasswordField = false,
                isPasswordVisible = false,
                onPasswordToggle = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldWhitIcon(
                value = email,
                onValueChange = onEmailChange,
                label = stringResource(R.string.correo_institucional),
                placeholder = stringResource(R.string.nombre_universidad_edu_co),
                leadingIcon = Icons.Default.Email,
                isPasswordField = false,
                isPasswordVisible = false,
                onPasswordToggle = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldWhitIcon(
                value = password,
                onValueChange = onPasswordChange,
                label = stringResource(R.string.crea_tu_contrase_a),
                placeholder = stringResource(R.string.crea_una_contrase_a_segura),
                leadingIcon = Icons.Default.Lock,
                isPasswordField = true,
                isPasswordVisible = isPasswordVisible,
                onPasswordToggle = onPasswordToggle
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldWhitIcon(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = stringResource(R.string.confirma_tu_contrase_a),
                placeholder = stringResource(R.string.repite_tu_contrase_a),
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
                    text = stringResource(R.string.crear_cuenta),
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
