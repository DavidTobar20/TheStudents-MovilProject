package com.example.thestudents.data.dtos

import com.example.thestudents.data.Inscription

data class InscripcionDto(
    val id: String? = null,
    val materia: String = "",
    val periodo: String = "",
    val usuario: StudentSummaryDto? = null
)

data class MateriaDto(
    val id: String? = null,
    val nombre: String = "",
    val periodos: List<String> = emptyList()
)

fun InscripcionDto.toInscription(): Inscription {
    return Inscription(
        id = id.orEmpty(),
        student = usuario?.toStudent() ?: StudentSummaryDto().toStudent(),
        className = materia,
        period = periodo
    )
}
