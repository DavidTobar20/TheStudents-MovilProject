package com.example.thestudents.data.dtos

import com.example.thestudents.data.Inscription

data class InscripcionDto(
    val id: Int,
    val materia: String,
    val periodo: String,
    val usuario: StudentDto
)

fun InscripcionDto.toInscription(): Inscription {
    return Inscription(
        id = id.toString(),
        student = usuario.toStudent(),
        className = materia,
        period = periodo
    )
}
