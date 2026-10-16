package com.example.thestudents.data.datasource.impl.firestore

import com.example.thestudents.data.datasource.ReviewRemoteDataSource
import com.example.thestudents.data.dtos.CommentDto
import com.example.thestudents.data.dtos.CreateReviewDto
import com.example.thestudents.data.dtos.ReviewDto
import com.example.thestudents.data.dtos.StudentDto
import com.example.thestudents.data.dtos.UserProfileFirestoreDto
import com.example.thestudents.data.local.localStudentProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReviewFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
): ReviewRemoteDataSource {

    private suspend fun resolveStudentDto(userId: String?, currentDto: StudentDto?): StudentDto? {
        val targetId = userId ?: currentDto?.id
        if (targetId.isNullOrBlank()) return currentDto

        val userDoc = runCatching {
            db.collection("users").document(targetId).get().await()
                .toObject(UserProfileFirestoreDto::class.java)
        }.getOrNull()

        if (userDoc != null) {
            val fullName = if (!userDoc.apellido.isNullOrBlank()) {
                "${userDoc.name.orEmpty()} ${userDoc.apellido}".trim()
            } else {
                userDoc.name.orEmpty()
            }
            return StudentDto(
                id = targetId,
                nombre = fullName.ifBlank { currentDto?.nombre },
                nombreUsuario = userDoc.userName ?: currentDto?.nombreUsuario,
                fotoUrl = userDoc.fotoUrl?.takeIf { it.isNotBlank() } ?: currentDto?.fotoUrl,
                biografia = userDoc.biografia ?: currentDto?.biografia,
                carrera = userDoc.carrera ?: currentDto?.carrera,
                semestre = userDoc.semestre ?: currentDto?.semestre
            )
        }

        val localStudent = localStudentProvider.students.find { it.id == targetId }
        if (localStudent != null && currentDto == null) {
            return StudentDto(
                id = localStudent.id,
                nombre = localStudent.name,
                nombreUsuario = localStudent.username,
                fotoUrl = localStudent.profileImage,
                biografia = localStudent.bio,
                carrera = localStudent.program,
                semestre = localStudent.semester
            )
        }

        return currentDto?.copy(id = currentDto.id ?: targetId)
    }

    private suspend fun enrichReview(review: ReviewDto, docId: String): ReviewDto {
        val resolvedAutor = resolveStudentDto(review.autorId, review.autor)
        val resolvedResenado = resolveStudentDto(review.resenadoId, review.resenado)
        return review.copy(
            id = docId,
            autor = resolvedAutor,
            resenado = resolvedResenado
        )
    }

    override suspend fun getAllReviewsExcludingStudentId(studentId: String): List<ReviewDto> {
        val snapshot = db.collection("reviews").get().await()
        return snapshot.documents.map { doc ->
            val review = doc.toObject(ReviewDto::class.java)
                ?: throw Exception("Review not found")
            enrichReview(review, doc.id)
        }
    }

    override suspend fun getReviewsByReviewedStudentId(reviewedStudentId: String): List<ReviewDto> {
        val snapshot = db.collection("reviews")
            .whereEqualTo("resenadoId", reviewedStudentId)
            .get()
            .await()
        return snapshot.documents.map { doc ->
            val review = doc.toObject(ReviewDto::class.java)
                ?: throw Exception("Review not found")
            enrichReview(review, doc.id)
        }
    }

    override suspend fun getReviewsByReviewerId(reviewerId: String): List<ReviewDto> {
        val snapshot = db.collection("reviews")
            .whereEqualTo("autorId", reviewerId)
            .get()
            .await()
        return snapshot.documents.map { doc ->
            val review = doc.toObject(ReviewDto::class.java)
                ?: throw Exception("Review not found")
            enrichReview(review, doc.id)
        }
    }

    override suspend fun getReviewById(reviewId: String): ReviewDto {
        val snapshot = db.collection("reviews").document(reviewId).get().await()
        val review = snapshot.toObject(ReviewDto::class.java)
            ?: throw Exception("Reseña no encontrada")
        return enrichReview(review, snapshot.id)
    }

    override suspend fun createReview(review: CreateReviewDto) {
        db.collection("reviews").add(review).await()
    }

    override suspend fun updateReview(
        reviewId: String,
        review: CreateReviewDto
    ) {
        db.collection("reviews").document(reviewId).set(review).await()
    }

    override suspend fun deleteReview(reviewId: String) {
        db.collection("reviews").document(reviewId).delete().await()
    }

    suspend fun addComment(reviewId: String, comment: CommentDto) {
        val docRef = db.collection("reviews").document(reviewId)
        val snapshot = docRef.get().await()
        val review = snapshot.toObject(ReviewDto::class.java)
            ?: throw Exception("Reseña no encontrada")
        val baseComments = review.comentarios
            ?: com.example.thestudents.data.local.localCommentsProvider.allComments.map { c ->
                CommentDto(
                    autor = StudentDto(
                        id = c.commentator.id,
                        nombre = c.commentator.name,
                        nombreUsuario = c.commentator.username,
                        fotoUrl = c.commentator.profileImage,
                        biografia = c.commentator.bio,
                        carrera = c.commentator.program,
                        semestre = c.commentator.semester
                    ),
                    contenido = c.content,
                    likes = c.likes,
                    disLikes = c.disLikes,
                    fechaCreacion = c.createdAt
                )
            }
        val updatedComments = baseComments + comment
        docRef.update("comentarios", updatedComments).await()
    }

}