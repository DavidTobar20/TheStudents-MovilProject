package com.example.thestudents.data.dtos

import com.example.thestudents.data.Review
import com.google.gson.annotations.SerializedName

data class UserProfileResponseDto(
    val usuario: StudentDto,
    @SerializedName("resenas_recibidas")
    val resenasRecibidas: List<ReceivedReviewDto>,
    @SerializedName("resenas_creadas")
    val resenasCreadas: List<CreatedReviewDto>
)

data class ReceivedReviewDto(
    val id: Int,
    val materia: String,
    val periodo: String,
    val contenido: String,
    val likes: Int,
    @SerializedName("disLikes")
    val disLikes: Int,
    val rating: String?,
    @SerializedName("fecha_creacion")
    val fechaCreacion: String,
    @SerializedName("fecha_edicion")
    val fechaEdicion: String?,
    val estado: String,
    val autor: StudentDto
)

data class CreatedReviewDto(
    val id: Int,
    val materia: String,
    val periodo: String,
    val contenido: String,
    val likes: Int,
    @SerializedName("disLikes")
    val disLikes: Int,
    val rating: String?,
    @SerializedName("fecha_creacion")
    val fechaCreacion: String,
    @SerializedName("fecha_edicion")
    val fechaEdicion: String?,
    val estado: String,
    val resenado: StudentDto
)

fun ReceivedReviewDto.toReview(profileUser: StudentDto): Review {
    return Review(
        id = id.toString(),
        reviewer = autor.toStudent(),
        reviewedStudent = profileUser.toStudent(),
        classReviewed = materia,
        periodReviewed = periodo,
        content = contenido,
        time = formatReviewTime(fechaCreacion).orEmpty(),
        likes = likes,
        disLikes = disLikes,
        rating = rating,
        comments = emptyList()
    )
}

fun CreatedReviewDto.toReview(profileUser: StudentDto): Review {
    return Review(
        id = id.toString(),
        reviewer = profileUser.toStudent(),
        reviewedStudent = resenado.toStudent(),
        classReviewed = materia,
        periodReviewed = periodo,
        content = contenido,
        time = formatReviewTime(fechaCreacion).orEmpty(),
        likes = likes,
        disLikes = disLikes,
        rating = rating,
        comments = emptyList()
    )
}
