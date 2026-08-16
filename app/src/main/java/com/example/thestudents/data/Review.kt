package com.example.thestudents.data

import androidx.annotation.DrawableRes


data class Review(
    @DrawableRes val authorImageId: Int? = null,
    val authorName: String,
    val authorInitials: String, // Para el icono si no hay imagen
    val courseAndPeriod: String, // Actúa como el 'username' o subtítulo
    val content: String,
    val time: String,
    val likes: Int = 0,
    val comments: Int = 0
)
