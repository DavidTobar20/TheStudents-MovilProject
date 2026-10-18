package com.example.thestudents.data.dtos

import com.google.gson.annotations.SerializedName

data class RegisterUserDto(
    val nombre: String,
    val nombre_usuario: String,
    val carrera: String,
    val semestre: Int,
    val biografia: String,
    val foto_url: String? = null,
    val color_perfil: String = ""
)
