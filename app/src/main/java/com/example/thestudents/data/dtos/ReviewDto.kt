package com.example.thestudents.data.dtos

import com.example.thestudents.data.Review
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

data class ReviewDto(
    val id: String? = null,
    val contenido: String? = null,
    val likesCount: Int? = 0,
    val disLikes: Int? = 0,
    val commentsCount: Int? = 0,
    val rating: String? = null,
    // En Firestore el campo se llama "fechaCreacion"; en el backend de Retrofit, "fecha_creacion"
    @SerializedName("fecha_creacion")
    val fechaCreacion: String? = null,
    val autor: StudentSummaryDto? = null,
    val resenado: StudentSummaryDto? = null,
    val materia: String? = null,
    val periodo: String? = null,
    var isLiked: Boolean = false,
    var isDisliked: Boolean = false
)

// Convierte la fecha ISO guardada ("2026-10-23T04:31:23.257Z") en texto relativo:
// "Hace un momento", "Hace 5 minutos", "Hace 2 horas", "Hace 3 días", "Hace 1 mes", "Hace 2 años"
fun formatReviewTime(rawTime: String?): String {
    if (rawTime.isNullOrBlank()) return ""

    // Se lee como fecha UTC (la "Z" del final); los milisegundos y la "Z" se ignoran al leer
    val date = try {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        isoFormat.timeZone = TimeZone.getTimeZone("UTC")
        isoFormat.parse(rawTime)
    } catch (_: Exception) {
        null
    } ?: return rawTime.substringBefore("T")

    val minutos = ((System.currentTimeMillis() - date.time) / 60_000).coerceAtLeast(0)
    val horas = minutos / 60
    val dias = horas / 24
    val meses = dias / 30
    val anios = dias / 365

    return when {
        minutos < 1 -> "Hace un momento"
        minutos < 60 -> if (minutos == 1L) "Hace 1 minuto" else "Hace $minutos minutos"
        horas < 24 -> if (horas == 1L) "Hace 1 hora" else "Hace $horas horas"
        dias < 30 -> if (dias == 1L) "Hace 1 día" else "Hace $dias días"
        meses < 12 -> if (meses == 1L) "Hace 1 mes" else "Hace $meses meses"
        else -> if (anios == 1L) "Hace 1 año" else "Hace $anios años"
    }
}

fun ReviewDto.toReview(): Review {
    return Review(
        id = id.orEmpty(),
        reviewer = autor?.toStudent() ?: com.example.thestudents.data.local.localStudentProvider.currentUser,
        reviewedStudent = resenado?.toStudent() ?: com.example.thestudents.data.local.localStudentProvider.students[0],
        classReviewed = materia.orEmpty(),
        periodReviewed = periodo.orEmpty(),
        content = contenido.orEmpty(),
        time = formatReviewTime(fechaCreacion),
        likesCount = likesCount ?: 0,
        disLikes = disLikes ?: 0,
        rating = if (rating.isNullOrBlank()) null else rating,
        comments = emptyList(),
        commentsCount = commentsCount ?: 0,
        isLiked = isLiked,
        isDisliked = isDisliked
    )
}
