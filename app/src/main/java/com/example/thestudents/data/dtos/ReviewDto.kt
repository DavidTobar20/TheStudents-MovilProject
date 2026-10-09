package com.example.thestudents.data.dtos

import com.example.thestudents.data.Review
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

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
    return Review(
        id = id.toString(),
        reviewer = autor?.toStudent() ?: com.example.thestudents.data.local.localStudentProvider.currentUser,
        reviewedStudent = resenado?.toStudent() ?: com.example.thestudents.data.local.localStudentProvider.students[0],
        classReviewed = materia ?: "",
        periodReviewed = periodo ?: "",
        content = contenido,
        time = formatReviewTime(fechaCreacion),
        likes = likes ?: 0,
        disLikes = disLikes ?: 0,
        rating = rating?.ifEmpty { null },
        comments = emptyList()
    )
}
