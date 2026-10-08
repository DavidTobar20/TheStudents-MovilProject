package com.example.thestudents.data.dtos

import com.google.gson.annotations.SerializedName

data class CreateReviewDto(
    @SerializedName("autor_id")
    val autorId: Int?,
    @SerializedName("resenado_id")
    val resenadoId: Int?,
    val materia: String?,
    val periodo: String?,
    val contenido: String?,
    val rating: String?,
    val estado: String?
)
