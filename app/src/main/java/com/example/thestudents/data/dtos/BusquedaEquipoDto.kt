package com.example.thestudents.data.dtos

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

/**
 * Documento de la colección "busquedasEquipo": un estudiante que busca equipo
 * para una materia que va a ver en un semestre.
 */
data class BusquedaEquipoDto(
    @get:Exclude val id: String? = null, // el id es el del documento, no se guarda como campo
    val materia: String = "",
    val periodo: String = "",
    val usuario: StudentSummaryDto? = null,
    val fechaCreacion: Timestamp? = null
)
