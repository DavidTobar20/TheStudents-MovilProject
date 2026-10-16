package com.example.thestudents.data.dtos

import com.google.gson.annotations.SerializedName

data class CreateReviewDto(

    @SerializedName("autor_id")
    var autorId: String?,
    @SerializedName("resenado_id")
    val resenadoId: String?,
    val materia: String?,
    val periodo: String?,
    val contenido: String?,
    val rating: String?,
    val estado: String?
)
