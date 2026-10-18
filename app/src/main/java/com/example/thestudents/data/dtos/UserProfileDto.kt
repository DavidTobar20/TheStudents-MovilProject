package com.example.thestudents.data.dtos

import com.example.thestudents.data.UserProfile
import com.google.gson.annotations.SerializedName

data class UserProfileDto(
    val usuario: StudentDto,
    val resenas_recibidas: List<ReviewDto>,
    val resenas_creadas: List<ReviewDto>
)

fun UserProfileDto.toUserProfile(): UserProfile{
    return UserProfile(
        student = usuario.toStudent(),
        receivedReviews = resenas_recibidas.map { it.toReview() },
        createdReviews = resenas_creadas.map { it.toReview() }
    )
}