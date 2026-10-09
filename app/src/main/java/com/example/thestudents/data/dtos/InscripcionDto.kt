package com.example.thestudents.data.dtos

import com.example.thestudents.data.Inscription
import com.google.gson.annotations.SerializedName

data class InscripcionDto(
    val id: Int,
    @SerializedName("usuario_id")
    val usuarioId: Int,
    val materia: String,
    val periodo: String,
    @SerializedName("fecha_creacion")
    val fechaCreacion: String,
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
