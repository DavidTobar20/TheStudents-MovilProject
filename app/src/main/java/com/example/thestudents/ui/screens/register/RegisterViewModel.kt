package com.example.thestudents.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.example.thestudents.data.repository.AuthRepository
import com.example.thestudents.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
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

    fun onUsernameChange(newUsername: String) {
        _uiState.update { it.copy(username = newUsername) }
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

    fun onProgramChange(newProgram: String) {
        _uiState.update { it.copy(program = newProgram) }
    }

    fun onSemesterChange(newSemester: String) {
        _uiState.update { it.copy(semester = newSemester) }
    }

    fun onBioChange(newBio: String) {
        _uiState.update { it.copy(bio = newBio) }
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
        val state = _uiState.value

        val names = state.names
        val username = state.username
        val program = state.program
        val semester = state.semester
        val bio = state.bio
        val email = state.email
        val password = state.password
        val confirmPassword = state.confirmPassword

        if (names.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || program.isEmpty() || semester.isEmpty()) {
            _errorMessage.value = "Todos los campos son obligatorios excepto bio"
            _mostrarMensajeError.value = true
        } else if (password != confirmPassword) {
            _errorMessage.value = "Las contraseñas no coinciden"
            _mostrarMensajeError.value = true
        } else {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                val result = authRepository.signUp(email, password)
                if (result.isSuccess) {
                    val userId = authRepository.currentUser?.uid ?: ""
                    val registerResult = userRepository.registerUser(
                        name = names,
                        username = username,
                        program = program,
                        semester = semester.toIntOrNull() ?: 1,
                        bio = bio,
                        userId = userId
                    )
                    if (registerResult.isSuccess) {
                        _navigateToHome.value = true
                    } else {
                        _errorMessage.value = registerResult.exceptionOrNull()?.message ?: "Error al registrar en la base de datos"
                        _mostrarMensajeError.value = true
                    }
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "Error al registrar la cuenta en la autenticación"
                    _mostrarMensajeError.value = true
                }
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onErrorDismiss() {
        _mostrarMensajeError.value = false
    }
}
