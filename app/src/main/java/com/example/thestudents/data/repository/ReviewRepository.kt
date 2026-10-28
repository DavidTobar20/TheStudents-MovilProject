package com.example.thestudents.data.repository

import android.util.Log
import retrofit2.HttpException
import com.example.thestudents.data.Comment
import com.example.thestudents.data.HomeFeed
import com.example.thestudents.data.NotificationType
import com.example.thestudents.data.Review
import com.example.thestudents.data.datasource.AuthRemoteDataSource
import com.example.thestudents.data.datasource.impl.firestore.NotificationFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.firestore.ReviewFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.thestudents.data.dtos.CreateReviewGenericDto
import com.example.thestudents.data.dtos.NotificationDto
import com.example.thestudents.data.dtos.StudentSummaryDto
import com.example.thestudents.data.dtos.toSummaryDto
import com.google.firebase.Timestamp
import com.example.thestudents.data.dtos.toComment
import com.example.thestudents.data.dtos.toReview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val reviewRemoteDataSource: ReviewFirestoreDataSourceImpl,
    private val notificationRemoteDataSource: NotificationFirestoreDataSourceImpl,
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val userRemoteDataSource: UserFirestoreDataSourceImpl
) {

    suspend fun getAllReviewsExcludingStudentId(studentId: String): Result<List<Review>> {
        return try {
            val reviewsDto = reviewRemoteDataSource.getAllReviewsExcludingStudentId(studentId)
            val reviews = reviewsDto.map { it.toReview() }
            Result.success(reviews)
        } catch (e: HttpException){
            Result.failure(e)
        } catch (e: Exception){
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

    suspend fun getReviewById(reviewId: String) : Result<Review> {
        val currentUserId = authRemoteDataSource.currentUser?.uid ?: ""

        return try {
            val reviewDto = reviewRemoteDataSource.getReviewById(reviewId, currentUserId)
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
        return try{
            val createReviewDto = CreateReviewGenericDto(
                autor_id = autorId,
                resenado_id = resenadoId,
                materia = materia,
                periodo = periodo,
                rating = rating,
                contenido = contenido
            )
            val reviewId = reviewRemoteDataSource.createReview(createReviewDto)
            try {
                // Datos del autor (nombre, usuario y foto) para mostrarlos en la notificacion.
                // Si no se puede leer su perfil, al menos se usa la foto del usuario logueado.
                val emisor = try {
                    userRemoteDataSource.getUserById(autorId)?.toSummaryDto()
                } catch (e: Exception) {
                    null
                } ?: StudentSummaryDto(
                    id = autorId,
                    foto_url = authRemoteDataSource.currentUser?.photoUrl?.toString()
                )

                val notificationDto = NotificationDto(
                    destinatarioId = resenadoId,
                    emisorId = autorId,
                    tipo = NotificationType.REVIEW.name,
                    materia = materia,
                    rating = rating?.toDoubleOrNull()?.toInt(),
                    comentario = contenido,
                    resenaId = reviewId.ifBlank { null },
                    emisor = emisor,
                    fechaCreacion = Timestamp.now()
                )
                notificationRemoteDataSource.createNotification(notificationDto)
            } catch (e: Exception) {
                Log.d("ReviewRepository", "No se pudo crear la notificación: ${e.message}")
            }
            Result.success(Unit)

        } catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun updateReview(
        reviewId: String,
        rating: String?,
        contenido: String?
    ): Result<Unit> {
        return try {
            val createReviewDto = CreateReviewGenericDto(
                contenido = contenido,
                rating = rating
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

    suspend fun sendOrDeleteReviewLike(
        reviewId: String,
        studentId: String,
    ): Result<Unit> {
        return try {
            reviewRemoteDataSource.senOrDeleteReviewLikes(
                reviewId = reviewId,
                studentId = studentId,
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendOrDeleteReviewDislike(
        reviewId: String,
        studentId: String,
    ): Result<Unit> {
        return try {
            reviewRemoteDataSource.sendOrDeleteReviewDislike(
                reviewId = reviewId,
                studentId = studentId,
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsLive(studentId: String): Flow<List<Review>> {
        return reviewRemoteDataSource.listenAllReviewsExcludingStudentId(studentId).map { reviews ->
            reviews.map { it.toReview() }
        }
    }

    suspend fun getHomeFeedLive(studentId: String): Flow<HomeFeed> {
        return reviewRemoteDataSource.listenHomeFeed(studentId).map { it.toHomeFeed() }
    }

    suspend fun getComments(reviewId: String): Result<List<Comment>> {
        // Para saber a cuales comentarios ya les dio like/dislike el usuario actual
        val currentUserId = authRemoteDataSource.currentUser?.uid ?: ""

        return try {
            val commentsDto = reviewRemoteDataSource.getComments(reviewId, currentUserId)
            val comments = commentsDto.map { it.toComment() }
            Result.success(comments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createComment(reviewId: String, contenido: String): Result<Comment> {
        // El autor es el usuario que tiene la sesion iniciada
        val autorId = authRemoteDataSource.currentUser?.uid ?: ""

        return try {
            val commentDto = reviewRemoteDataSource.createComment(reviewId, autorId, contenido)
            val comment = commentDto.toComment()
            Result.success(comment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateComment(reviewId: String, commentId: String, contenido: String): Result<Unit> {
        return try {
            reviewRemoteDataSource.updateComment(reviewId, commentId, contenido)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteComment(reviewId: String, commentId: String): Result<Unit> {
        return try {
            reviewRemoteDataSource.deleteComment(reviewId, commentId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendOrDeleteCommentLike(reviewId: String, commentId: String, studentId: String): Result<Unit> {
        return try {
            reviewRemoteDataSource.sendOrDeleteCommentLike(reviewId, commentId, studentId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendOrDeleteCommentDislike(reviewId: String, commentId: String, studentId: String): Result<Unit> {
        return try {
            reviewRemoteDataSource.sendOrDeleteCommentDislike(reviewId, commentId, studentId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
