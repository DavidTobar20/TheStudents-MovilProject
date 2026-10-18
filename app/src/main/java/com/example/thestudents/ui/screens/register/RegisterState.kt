package com.example.thestudents.ui.screens.register

data class RegisterState(
    val names: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val program: String = "",
    val semester: String = "",
    val bio: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val termsAccepted: Boolean = false,
    val isLoading: Boolean = false
)
