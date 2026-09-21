package com.example.thestudents.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.example.thestudents.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _mostrarMensajeError = MutableStateFlow(false)
    val mostrarMensajeError: StateFlow<Boolean> = _mostrarMensajeError

    private val _navigateToHome = MutableStateFlow(false)
    val navigateToHome: StateFlow<Boolean> = _navigateToHome

    private val _mostrarPassword = MutableStateFlow(false)
    val mostrarPassword: StateFlow<Boolean> = _mostrarPassword

    fun mostrarEsconderPassword() {
        _mostrarPassword.value = !_mostrarPassword.value
    }

    fun onNamesChange(newNames: String) {
        _uiState.update { it.copy(names = newNames) }
    }

    fun onLastNamesChange(newLastNames: String) {
        _uiState.update { it.copy(lastNames = newLastNames) }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChange(newPassword: String) {
        Log.d("RegisterViewModel", _uiState.value.password.length.toString())
        _uiState.update { it.copy(password = newPassword) }
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = newConfirmPassword) }
    }

    fun onPasswordToggle() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onConfirmPasswordToggle() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    fun onTermsAcceptedChange(accepted: Boolean) {
        _uiState.update { it.copy(termsAccepted = accepted) }
    }

    fun onRegisterClick() {
        val names = _uiState.value.names
        val lastNames = _uiState.value.lastNames
        val email = _uiState.value.email
        val password = _uiState.value.password
        val confirmPassword = _uiState.value.confirmPassword

        if (names.isEmpty() || lastNames.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            _errorMessage.value = "Todos los campos son obligatorios"
            _mostrarMensajeError.value = true
        } else if (password.length < 6) {
            _errorMessage.value = "La contraseña debe tener al menos 6 caracteres"
            _mostrarMensajeError.value = true
        } else if (email == "admin@admin.com") {
            _errorMessage.value = "El correo ya esta en uso"
            _mostrarMensajeError.value = true
        } else {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                try {
                    authRepository.signUp(email, password)
                    _navigateToHome.value = true
                    _uiState.update { it.copy(isLoading = false) }
                } catch (e: Exception) {
                    _errorMessage.value = e.message.toString()
                    _mostrarMensajeError.value = true
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun onErrorDismiss() {
        _mostrarMensajeError.value = false
    }
}
