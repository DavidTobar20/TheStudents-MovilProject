package com.example.thestudents.data.dtos

import com.example.thestudents.data.Inscription
import com.google.gson.annotations.SerializedName

data class InscripcionDto(
    val id: Int? = null,
    @SerializedName("usuario_id")
    val usuarioId: Int? = null,
    val materia: String? = null,
    val periodo: String? = null,
    @SerializedName("fecha_creacion")
    val fechaCreacion: String? = null,
    val usuario: StudentDto? = null
)

fun InscripcionDto.toInscription(): Inscription {
    return Inscription(
        id = id?.toString() ?: "",
        student = usuario?.toStudent() ?: com.example.thestudents.data.local.localStudentProvider.currentUser,
        className = materia ?: "",
        period = periodo ?: ""
    )
}
