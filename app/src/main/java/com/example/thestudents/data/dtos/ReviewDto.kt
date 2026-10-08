package com.example.thestudents.data.dtos

import com.example.thestudents.data.Review
import com.google.gson.annotations.SerializedName

data class ReviewDto(
    val id: Int,
    val contenido: String,
    val likes: Int,
    val disLikes: Int,
    val rating: String,
    @SerializedName("fecha_creacion")
    val fechaCreacion: String,
    @SerializedName("fecha_edicion")
    val fechaEdicion: String?, // Puede ser null
    val estado: String,
    val autor: StudentDto,
    val resenado: StudentDto,
    val materia: String,
    val periodo: String
)

fun ReviewDto.toReview(): Review {
    return Review(
        id = id.toString(),
        reviewer = autor.toStudent(),
        reviewedStudent = resenado.toStudent(),
        classReviewed = materia,
        periodReviewed = periodo,
        content = contenido,
        time = fechaCreacion,
        likes = likes,
        disLikes = disLikes,
        rating = rating.ifEmpty { null },
        comments = emptyList()
    )
}
