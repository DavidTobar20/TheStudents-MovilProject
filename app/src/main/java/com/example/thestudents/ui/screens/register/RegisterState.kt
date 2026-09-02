package com.example.thestudents.ui.screens.register

data class RegisterState(
    val names: String = "",
    val lastNames: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val termsAccepted: Boolean = false
)
