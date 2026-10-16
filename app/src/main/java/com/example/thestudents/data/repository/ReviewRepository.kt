package com.example.thestudents.data.repository

import retrofit2.HttpException
import com.example.thestudents.data.Review
import com.example.thestudents.data.Student
import com.example.thestudents.data.datasource.AuthRemoteDataSource
import com.example.thestudents.data.datasource.UserRemoteDataSource
import com.example.thestudents.data.datasource.impl.firestore.ReviewFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.retrofit.ReviewRetrofitDataSourceImpl
import com.example.thestudents.data.dtos.CommentDto
import com.example.thestudents.data.dtos.CreateReviewDto
import com.example.thestudents.data.dtos.CreateReviewUserDto
import com.example.thestudents.data.dtos.StudentDto
import com.example.thestudents.data.dtos.toReview
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val reviewRemoteDataSource: ReviewFirestoreDataSourceImpl,
    private val userRemoteDataSource: UserFirestoreDataSourceImpl,
    private val authRemoteDataSource: AuthRemoteDataSource
) {

    suspend fun getAllReviewsExcludingStudentId(studentId: String): Result<List<Review>> {
        return try {
            val reviewsDto = reviewRemoteDataSource.getAllReviewsExcludingStudentId(studentId)
            val reviews = reviewsDto.map { it.toReview() }
            Result.success(reviews)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsByReviewedStudentId(reviewedStudentId: String): Result<List<Review>> {
        return try {
            val reviewsDto = reviewRemoteDataSource.getReviewsByReviewedStudentId(reviewedStudentId)
            val reviews = reviewsDto.map { it.toReview() }
            Result.success(reviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsByReviewerId(reviewerId: String): Result<List<Review>> {
        return try {
            val reviewsDto = reviewRemoteDataSource.getReviewsByReviewerId(reviewerId)
            val reviews = reviewsDto.map { it.toReview() }
            Result.success(reviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewById(reviewId: String): Result<Review> {
        return try {
            val reviewDto = reviewRemoteDataSource.getReviewById(reviewId)
            val review = reviewDto.toReview()
            Result.success(review)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createReview(
        autorId: String,
        resenadoId: String,
        materia: String,
        periodo: String,
        rating: String?,
        contenido: String,
        resenadoStudent: Student? = null
    ): Result<Unit> {
        return try {
            val autor = userRemoteDataSource.getUserProfile(autorId)
            val autorFullName = if (!autor.apellido.isNullOrBlank()) {
                "${autor.name.orEmpty()} ${autor.apellido}".trim()
            } else {
                autor.name.orEmpty()
            }
            val photoUrl = authRemoteDataSource.currentUser?.photoUrl?.toString()
                ?.takeIf { it.isNotBlank() } ?: autor.fotoUrl

            val createReviewUserDto = CreateReviewUserDto(
                id = autorId,
                nombre = autorFullName,
                nombreUsuario = autor.userName,
                fotoUrl = photoUrl
            )

            val resenadoProfile = runCatching { userRemoteDataSource.getUserProfile(resenadoId) }.getOrNull()
            val resenadoDto = if (resenadoProfile != null) {
                val resenadoFullName = if (!resenadoProfile.apellido.isNullOrBlank()) {
                    "${resenadoProfile.name.orEmpty()} ${resenadoProfile.apellido}".trim()
                } else {
                    resenadoProfile.name.orEmpty()
                }
                CreateReviewUserDto(
                    id = resenadoId,
                    nombre = resenadoFullName.ifBlank { resenadoStudent?.name },
                    nombreUsuario = resenadoProfile.userName ?: resenadoStudent?.username,
                    fotoUrl = resenadoProfile.fotoUrl?.takeIf { it.isNotBlank() } ?: resenadoStudent?.profileImage
                )
            } else if (resenadoStudent != null) {
                CreateReviewUserDto(
                    id = resenadoId,
                    nombre = resenadoStudent.name,
                    nombreUsuario = resenadoStudent.username,
                    fotoUrl = resenadoStudent.profileImage
                )
            } else {
                null
            }

            val createReviewDto = CreateReviewDto(
                autorId = autorId,
                resenadoId = resenadoId,
                materia = materia,
                periodo = periodo,
                rating = rating,
                contenido = contenido,
                estado = "activo",
                autor = createReviewUserDto,
                resenado = resenadoDto
            )
            reviewRemoteDataSource.createReview(createReviewDto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateReview(
        reviewId: String,
        rating: String?,
        contenido: String?,
        estado: String?
    ): Result<Unit> {
        return try {
            val existing = runCatching { reviewRemoteDataSource.getReviewById(reviewId) }.getOrNull()

            val createReviewDto = CreateReviewDto(
                autorId = existing?.autorId,
                resenadoId = existing?.resenadoId,
                materia = existing?.materia,
                periodo = existing?.periodo,
                contenido = contenido,
                rating = rating,
                estado = estado,
                autor = existing?.autor?.let {
                    CreateReviewUserDto(
                        id = it.id ?: existing.autorId,
                        nombre = it.nombre,
                        nombreUsuario = it.nombreUsuario,
                        fotoUrl = it.fotoUrl
                    )
                },
                resenado = existing?.resenado?.let {
                    CreateReviewUserDto(
                        id = it.id ?: existing.resenadoId,
                        nombre = it.nombre,
                        nombreUsuario = it.nombreUsuario,
                        fotoUrl = it.fotoUrl
                    )
                },
                comentarios = existing?.comentarios
            )
            reviewRemoteDataSource.updateReview(reviewId, createReviewDto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteReview(reviewId: String): Result<Unit> {
        return try {
            reviewRemoteDataSource.deleteReview(reviewId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addComment(reviewId: String, contenido: String): Result<Unit> {
        return try {
            val userId = authRemoteDataSource.currentUser?.uid
                ?: com.example.thestudents.data.local.localStudentProvider.currentUser.id
            val autor = runCatching { userRemoteDataSource.getUserProfile(userId) }.getOrNull()
            val fullName = if (autor != null) {
                if (!autor.apellido.isNullOrBlank()) {
                    "${autor.name.orEmpty()} ${autor.apellido}".trim()
                } else {
                    autor.name.orEmpty()
                }
            } else {
                com.example.thestudents.data.local.localStudentProvider.currentUser.name
            }
            val photoUrl = authRemoteDataSource.currentUser?.photoUrl?.toString()
                ?.takeIf { it.isNotBlank() } ?: autor?.fotoUrl

            val commentDto = CommentDto(
                autor = StudentDto(
                    id = userId,
                    nombre = fullName,
                    nombreUsuario = autor?.userName ?: com.example.thestudents.data.local.localStudentProvider.currentUser.username,
                    fotoUrl = photoUrl
                ),
                contenido = contenido,
                likes = 0,
                disLikes = 0,
                fechaCreacion = "Hace un momento"
            )
            reviewRemoteDataSource.addComment(reviewId, commentDto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
