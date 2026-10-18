package com.example.thestudents.data.dtos

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class CreateReviewGenericDto(
    val autor_id: String? = null,
    val resenado_id: String? = null,
    val materia: String? = null,
    val periodo: String? = null,
    val contenido: String? = null,
    val rating: String? = null
)

data class CreateReviewRetrofitDto(
    val autor_id: String? = null,
    val resenado_id: String? = null,
    val materia: String? = null,
    val periodo: String? = null,
    val contenido: String? = null,
    val rating: String? = null
)

fun currentIsoDate(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.format(Date())
}

data class CreateReviewFirebaseDto(
    val autor_id: String? = null,
    val resenado_id: String? = null,
    val materia: String? = null,
    val periodo: String? = null,
    val contenido: String? = null,
    val rating: String? = null,
    val likes: Int = 0,
    val disLikes: Int = 0,
    val fechaCreacion: String = currentIsoDate(),
    val autor: StudentSummaryDto? = null,
    val resenado: StudentSummaryDto? = null
)
