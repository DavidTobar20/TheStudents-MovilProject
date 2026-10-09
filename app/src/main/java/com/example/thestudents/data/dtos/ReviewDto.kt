package com.example.thestudents.data.dtos

import com.example.thestudents.data.Review
import com.google.gson.annotations.SerializedName

data class ReviewDto(
    val id: Int,
    val contenido: String,
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
        id = id.toString(),
        reviewer = autor?.toStudent() ?: com.example.thestudents.data.local.localStudentProvider.currentUser,
        reviewedStudent = resenado?.toStudent() ?: com.example.thestudents.data.local.localStudentProvider.students[0],
        classReviewed = materia ?: "",
        periodReviewed = periodo ?: "",
        content = contenido,
        time = fechaCreacion ?: "",
        likes = likes ?: 0,
        disLikes = disLikes ?: 0,
        rating = rating?.ifEmpty { null },
        comments = emptyList()
    )
}
