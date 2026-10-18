package com.example.thestudents.data.dtos

import com.google.gson.annotations.SerializedName

data class CreateReviewDto(
    val autor_id: Int?,
    val resenado_id: Int?,
    val materia: String?,
    val periodo: String?,
    val contenido: String?,
    val rating: String?,
)
