package com.example.thestudents.data.dtos

import com.example.thestudents.data.UserProfile
import com.google.gson.annotations.SerializedName

data class UserProfileDto(
    val usuario: StudentDto,
    @SerializedName("resenas_recibidas")
    val resenasRecibidas: List<ReviewDto>,
    @SerializedName("resenas_creadas")
    val resenasCreadas: List<ReviewDto>
)

fun UserProfileDto.toUserProfile(): UserProfile{
    return UserProfile(
        student = usuario.toStudent(),
        receivedReviews = resenasRecibidas.map { it.toReview() },
        createdReviews = resenasCreadas.map { it.toReview() }
    )
}