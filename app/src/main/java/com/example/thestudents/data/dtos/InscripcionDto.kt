package com.example.thestudents.data.dtos

import com.example.thestudents.data.Inscription
import com.google.gson.annotations.SerializedName

// Todos los campos tienen valor por defecto para que Firestore pueda usar el constructor vacío.
// Los IDs son String (Gson convierte sin problema los IDs numéricos que vienen de Retrofit).
data class InscripcionDto(
    val id: String? = null,
    @SerializedName("usuario_id")
    val usuarioId: String? = null,
    val materia: String? = null,
    val periodo: String? = null,
    @SerializedName("fecha_creacion")
    val fechaCreacion: String? = null,
    val usuario: StudentDto? = null
)

fun InscripcionDto.toInscription(): Inscription {
    return Inscription(
        id = id.orEmpty(),
        student = (usuario ?: StudentDto()).copy(id = usuario?.id ?: usuarioId).toStudent(),
        className = materia.orEmpty(),
        period = periodo.orEmpty()
    )
}
