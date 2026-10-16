package com.example.thestudents.data.dtos

import com.example.thestudents.data.Student
import com.example.thestudents.data.UserProfile
import com.example.thestudents.ui.theme.avatarColorFor

abstract class UserDtoGeneric {
    abstract fun toUserProfile(): UserProfile
}

data class UserProfileFirestoreDto(
    val id: String? = null,
    val userName: String? = null,
    val name: String? = null,
    val apellido: String? = null,
    val biografia: String? = null,
    val carrera: String? = null,
    val semestre: Int? = null,
    val fotoUrl: String? = null,
    val receivedReviews: List<ReviewDto> = emptyList(),
    val createdReviews: List<ReviewDto> = emptyList()
) : UserDtoGeneric() {


    constructor() : this(null, null, null, null, null, null, null, null, emptyList(), emptyList())

    override fun toUserProfile(): UserProfile {
        val fullName = if (!apellido.isNullOrBlank()) "${name.orEmpty()} $apellido".trim() else name.orEmpty()
        val usernameSafe = userName.orEmpty()
        val initialsComputed = fullName.split(" ")
            .filter { it.isNotBlank() }
            .mapNotNull { it.firstOrNull()?.uppercase() }
            .take(2)
            .joinToString("")
            .ifEmpty { "US" }

        val student = Student(
            id = id.orEmpty(),
            name = fullName,
            username = usernameSafe,
            program = carrera.orEmpty(),
            semester = semestre ?: 1,
            bio = biografia.orEmpty(),
            rating = 0f,
            reviewsCount = 0,
            initials = initialsComputed,
            profileColor = avatarColorFor(usernameSafe.ifEmpty { "default" }),
            profileImage = if (fotoUrl.isNullOrBlank()) null else fotoUrl
        )

        return UserProfile(
            student = student,
            receivedReviews = receivedReviews.map { it.toReview() },
            createdReviews = createdReviews.map { it.toReview() }
        )
    }
}
