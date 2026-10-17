package com.example.thestudents.data.repository

import android.util.Log
import retrofit2.HttpException
import com.example.thestudents.data.NotificationType
import com.example.thestudents.data.Review
import com.example.thestudents.data.datasource.AuthRemoteDataSource
import com.example.thestudents.data.datasource.UserRemoteDataSource
import com.example.thestudents.data.datasource.impl.firestore.NotificationFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.firestore.ReviewFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.retrofit.ReviewRetrofitDataSourceImpl
import com.example.thestudents.data.dtos.CreateReviewDto
import com.example.thestudents.data.dtos.CreateReviewUserDto
import com.example.thestudents.data.dtos.NotificationDto
import com.example.thestudents.data.dtos.StudentDto
import com.example.thestudents.data.dtos.toReview
import com.google.firebase.Timestamp
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val reviewRemoteDataSource: ReviewFirestoreDataSourceImpl,
    private val userRemoteDataSource: UserFirestoreDataSourceImpl,
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val notificationRemoteDataSource: NotificationFirestoreDataSourceImpl
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
        contenido: String
    ): Result<Unit> {
        return try {

            val autor = userRemoteDataSource.getUserProfile(autorId)
            val photoUrl = authRemoteDataSource.currentUser?.photoUrl?.toString()

            val createReviewUserDto = CreateReviewUserDto(
                nombre = autor.name,
                nombreUsuario = autor.userName,
                fotoUrl = photoUrl
            )

            val createReviewDto = CreateReviewDto(
                autorId = autorId,
                resenadoId = resenadoId,
                materia = materia,
                periodo = periodo,
                rating = rating,
                contenido = contenido,
                estado = null,
                autor = createReviewUserDto
            )
            reviewRemoteDataSource.createReview(createReviewDto)

            // Notificar al estudiante reseñado. Si falla, la reseña igual queda creada.
            try {
                val nombreAutor = listOfNotNull(autor.name, autor.apellido)
                    .filter { it.isNotBlank() }
                    .joinToString(" ")
                val notificationDto = NotificationDto(
                    destinatarioId = resenadoId,
                    emisorId = autorId,
                    tipo = NotificationType.REVIEW.name,
                    materia = materia,
                    rating = rating?.toDoubleOrNull()?.toInt(),
                    comentario = contenido,
                    emisor = StudentDto(
                        id = autorId,
                        nombre = nombreAutor,
                        nombreUsuario = autor.userName,
                        fotoUrl = photoUrl
                    ),
                    fechaCreacion = Timestamp.now()
                )
                notificationRemoteDataSource.createNotification(notificationDto)
            } catch (e: Exception) {
                Log.d("ReviewRepository", "No se pudo crear la notificación: ${e.message}")
            }

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
            val createReviewDto = CreateReviewDto(
                autorId = null,
                resenadoId = null,
                materia = null,
                periodo = null,
                contenido = contenido,
                rating = rating,
                estado = estado
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
}
