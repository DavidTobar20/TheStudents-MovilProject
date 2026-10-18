package com.example.thestudents.data.dtos

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

interface CreateReviewGenericDto {
    val autor_id: Int?
    val resenado_id: Int?
    val materia: String?
    val periodo: String?
    val contenido: String?
    val rating: String?
}

data class CreateReviewRetrofitDto(
    override val autor_id: Int? = null,
    override val resenado_id: Int? = null,
    override val materia: String? = null,
    override val periodo: String? = null,
    override val contenido: String? = null,
    override val rating: String? = null
) : CreateReviewGenericDto

private fun currentIsoDate(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.format(Date())
}

data class CreateReviewFirebaseDto(
    override val autor_id: Int? = null,
    override val resenado_id: Int? = null,
    override val materia: String? = null,
    override val periodo: String? = null,
    override val contenido: String? = null,
    override val rating: String? = null,
    val likes: Int = 0,
    val disLikes: Int = 0,
    val fechaCreacion: String = currentIsoDate()
) : CreateReviewGenericDto
