package com.example.thestudents.data.dtos

import androidx.compose.ui.graphics.Color
import com.example.thestudents.data.Student
import com.example.thestudents.ui.theme.avatarColorFor

data class StudentDto(
    val id: Int? = null,
    val nombre_usuario: String? = null,
    val nombre: String? = null,
    val biografia: String? = null,
    val foto_url: String? = null,
    val color_perfil: String? = null,
    val carrera: String? = null,
    val semestre: Int? = null
)

fun StudentDto.toStudent(): Student {
    val usernameSafe = nombre_usuario.orEmpty()
    val nameSafe = nombre.orEmpty()
    val idSafe = id?.toString() ?: "0"

    val parsedColor = try {
        if (!color_perfil.isNullOrBlank()) {
            Color(android.graphics.Color.parseColor(color_perfil))
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
        profileImage = if (foto_url.isNullOrBlank()) null else foto_url
    )
}
