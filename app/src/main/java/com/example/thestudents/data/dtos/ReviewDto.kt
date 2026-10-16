package com.example.thestudents.data.dtos

import com.example.thestudents.data.Comment
import com.example.thestudents.data.Review
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

data class CommentDto(
    val autor: StudentDto? = null,
    val contenido: String? = null,
    val likes: Int? = 0,
    val disLikes: Int? = 0,
    val fechaCreacion: String? = null
) {
    constructor() : this(
        autor = null,
        contenido = null,
        likes = 0,
        disLikes = 0,
        fechaCreacion = null
    )
}

fun CommentDto.toComment(): Comment {
    return Comment(
        commentator = autor?.toStudent() ?: com.example.thestudents.data.local.localStudentProvider.currentUser,
        content = contenido.orEmpty(),
        likes = likes ?: 0,
        disLikes = disLikes ?: 0,
        createdAt = formatReviewTime(fechaCreacion).ifEmpty { "Reciente" }
    )
}

data class ReviewDto(
    val id: String? = null,
    val autorId: String? = null,
    val resenadoId: String? = null,
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
    val periodo: String? = null,
    val comentarios: List<CommentDto>? = null
) {
    constructor() : this(
        id = null,
        autorId = null,
        resenadoId = null,
        contenido = null,
        likes = 0,
        disLikes = 0,
        rating = null,
        fechaCreacion = null,
        fechaEdicion = null,
        estado = "activo",
        autor = null,
        resenado = null,
        materia = null,
        periodo = null,
        comentarios = null
    )
}

fun formatReviewTime(rawTime: String?): String {
    if (rawTime.isNullOrBlank()) return ""
    return try {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        isoFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = isoFormat.parse(rawTime)
        if (date != null) {
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            outputFormat.format(date)
        } else {
            rawTime.substringBefore("T")
        }
    } catch (_: Exception) {
        rawTime.substringBefore("T")
    }
}

fun ReviewDto.toReview(): Review {
    val reviewerStudent = autor?.copy(id = autor.id ?: autorId)?.toStudent()
        ?: com.example.thestudents.data.local.localStudentProvider.students.find { it.id == autorId }
        ?: com.example.thestudents.data.local.localStudentProvider.currentUser

    val reviewedStudentObj = resenado?.copy(id = resenado.id ?: resenadoId)?.toStudent()
        ?: com.example.thestudents.data.local.localStudentProvider.students.find { it.id == resenadoId }
        ?: com.example.thestudents.data.local.localStudentProvider.students[0]

    return Review(
        id = id ?: "0",
        reviewer = reviewerStudent,
        reviewedStudent = reviewedStudentObj,
        classReviewed = materia.orEmpty(),
        periodReviewed = periodo.orEmpty(),
        content = contenido.orEmpty(),
        time = formatReviewTime(fechaCreacion).orEmpty(),
        likes = likes ?: 0,
        disLikes = disLikes ?: 0,
        rating = if (rating.isNullOrBlank()) null else rating,
        comments = comentarios?.map { it.toComment() }
            ?: com.example.thestudents.data.local.localCommentsProvider.allComments
    )
}
