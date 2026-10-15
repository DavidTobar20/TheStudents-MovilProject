package com.example.thestudents.ui.screens.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val errorMessage: String = "",
    val mostrarMensaje: Boolean = false,
    val navigate: Boolean = false
)
