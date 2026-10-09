package com.example.thestudents.data.dtos

import com.example.thestudents.data.Review
import com.google.gson.annotations.SerializedName

data class ReviewDto(
    val id: Int? = null,
    val contenido: String? = null,
    val likes: Int? = 0,
    val disLikes: Int? = 0,
    val rating: String? = null,
    @SerializedName("fecha_creacion")
    val fechaCreacion: String? = null,
    @SerializedName("fecha_edicion")
    val fechaEdicion: String? = null,
    val estado: String? = "activo",
    val autor: StudentDto? = null,
    val resenado: StudentDto? = null,
    val materia: String? = null,
    val periodo: String? = null
)

fun ReviewDto.toReview(): Review {
    return Review(
        id = (id ?: 0).toString(),
        reviewer = autor?.toStudent() ?: com.example.thestudents.data.local.localStudentProvider.currentUser,
        reviewedStudent = resenado?.toStudent() ?: com.example.thestudents.data.local.localStudentProvider.students[0],
        classReviewed = materia.orEmpty(),
        periodReviewed = periodo.orEmpty(),
        content = contenido.orEmpty(),
        time = fechaCreacion.orEmpty(),
        likes = likes ?: 0,
        disLikes = disLikes ?: 0,
        rating = if (rating.isNullOrBlank()) null else rating,
        comments = emptyList()
    )
}
