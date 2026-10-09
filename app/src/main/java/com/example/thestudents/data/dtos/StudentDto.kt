package com.example.thestudents.data.dtos

import androidx.compose.ui.graphics.Color
import com.example.thestudents.data.Student
import com.example.thestudents.ui.theme.avatarColorFor
import com.google.gson.annotations.SerializedName

data class StudentDto(
    val id: Int? = null,
    val correo: String? = null,
    @SerializedName("nombre_usuario")
    val nombreUsuario: String? = null,
    val nombre: String? = null,
    val biografia: String? = null,
    @SerializedName("foto_url")
    val fotoUrl: String? = null,
    @SerializedName("color_perfil")
    val colorPerfil: String? = null,
    val carrera: String? = null,
    val semestre: Int? = null,
    val estado: String? = null,
    @SerializedName("fecha_creacion")
    val fechaCreacion: String? = null
)

fun StudentDto.toStudent(): Student {
    val usernameSafe = nombreUsuario.orEmpty()
    val nameSafe = nombre.orEmpty()
    val idSafe = id?.toString() ?: "0"

    val parsedColor = try {
        if (!colorPerfil.isNullOrBlank()) {
            Color(android.graphics.Color.parseColor(colorPerfil))
        } else {
            avatarColorFor(usernameSafe.ifEmpty { idSafe })
        }
    } catch (e: Exception) {
        avatarColorFor(usernameSafe.ifEmpty { idSafe })
    }

    val initialsComputed = nameSafe.split(" ")
        .filter { it.isNotBlank() }
        .mapNotNull { it.firstOrNull()?.uppercase() }
        .take(2)
        .joinToString("")
        .ifEmpty { "US" }

    return Student(
        id = idSafe,
        name = nameSafe,
        username = usernameSafe,
        program = carrera.orEmpty(),
        semester = semestre ?: 1,
        bio = biografia.orEmpty(),
        rating = 0f,
        reviewsCount = 0,
        initials = initialsComputed,
        profileColor = parsedColor,
        profileImage = if (fotoUrl.isNullOrBlank()) null else fotoUrl
    )
}
