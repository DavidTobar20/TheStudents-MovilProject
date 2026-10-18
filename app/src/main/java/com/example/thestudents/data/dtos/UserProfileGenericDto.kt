package com.example.thestudents.data.dtos

import com.example.thestudents.data.UserProfile


abstract class UserProfileGenericDto {
    abstract fun toUserProfile(): UserProfile
}

data class UserProfileRetrofitDto(
    val usuario: StudentDto,
    val resenas_recibidas: List<ReviewDto>,
    val resenas_creadas: List<ReviewDto>
): UserProfileGenericDto() {
    override fun toUserProfile(): UserProfile {
        return UserProfile(
            student = usuario.toStudent(),
            receivedReviews = resenas_recibidas.map { it.toReview() },
            createdReviews = resenas_creadas.map { it.toReview() }
        )
    }
}

data class UserProfileFirestoreDto(
    val id: String? = null,     // id del usuario
    val nombre_usuario: String? = null,
    val nombre: String? = null,
    val biografia: String? = null,
    val foto_url: String? = null,
    val color_perfil: String? = null,
    val carrera: String? = null,
    val semestre: Int? = null,
    val resenas_recibidas: List<ReviewDto> = emptyList(),
    val resenas_creadas: List<ReviewDto> = emptyList()
) : UserProfileGenericDto() {
    override fun toUserProfile(): UserProfile {
        val studentDto = StudentDto(
            id = id,
            nombre_usuario = nombre_usuario,
            nombre = nombre,
            biografia = biografia,
            foto_url = foto_url,
            color_perfil = color_perfil,
            carrera = carrera,
            semestre = semestre
        )
        return UserProfile(
            student = studentDto.toStudent(),
            receivedReviews = resenas_recibidas.map { it.toReview() },
            createdReviews = resenas_creadas.map { it.toReview() }
        )
    }
}
