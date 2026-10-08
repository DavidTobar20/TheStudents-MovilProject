package com.example.thestudents.data.dtos

import androidx.compose.ui.graphics.Color
import com.example.thestudents.data.Student
import com.example.thestudents.ui.theme.avatarColorFor
import com.google.gson.annotations.SerializedName

data class StudentDto(
    val id: Int,
    val correo: String,
    @SerializedName("nombre_usuario")
    val nombreUsuario: String,
    val nombre: String,
    val biografia: String,
    @SerializedName("foto_url")
    val fotoUrl: String,
    @SerializedName("color_perfil")
    val colorPerfil: String,
    val carrera: String,
    val semestre: Int,
    val estado: String,
    @SerializedName("fecha_creacion")
    val fechaCreacion: String
)

fun StudentDto.toStudent(): Student {
    val parsedColor = try {
        Color(android.graphics.Color.parseColor(colorPerfil))
    } catch (e: Exception) {
        avatarColorFor(nombreUsuario.ifEmpty { id.toString() })
    }

    val initialsComputed = nombre.split(" ")
        .mapNotNull { it.firstOrNull()?.uppercase() }
        .take(2)
        .joinToString("")
        .ifEmpty { "US" }

    return Student(
        id = id.toString(),
        name = nombre,
        username = nombreUsuario,
        program = carrera,
        semester = semestre,
        bio = biografia,
        rating = 0f,
        reviewsCount = 0,
        initials = initialsComputed,
        profileColor = parsedColor,
        profileImage = fotoUrl.ifEmpty { null }
    )
}
