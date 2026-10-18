package com.example.thestudents.data.datasource.impl.firestore

import com.example.thestudents.data.datasource.ReviewRemoteDataSource
import com.example.thestudents.data.dtos.CreateReviewFirebaseDto
import com.example.thestudents.data.dtos.CreateReviewGenericDto
import com.example.thestudents.data.dtos.ReviewDto
import com.example.thestudents.data.dtos.StudentDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReviewFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
): ReviewRemoteDataSource {

    override suspend fun getAllReviewsExcludingStudentId(studentId: String): List<ReviewDto> {
        val snapshot = try {
            db.collection("reviews").get().await()
        } catch (e: Exception) {
            throw Exception("Error al obtener las reseñas de Firestore: ${e.message}")
        }

        return snapshot.documents.mapNotNull { doc ->
            val reviewDto = doc.toObject(ReviewDto::class.java)
                ?: throw Exception("No se pudo deserializar la reseña de Firestore")
            reviewDto.copy(id = doc.id)
        }.filter { review ->
            val autorIdStr = review.autor?.id
            autorIdStr != studentId
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
            review.copy(id = doc.id)
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
            review.copy(id = doc.id)
        }
    }

    override suspend fun getReviewById(reviewId: String): ReviewDto {
        val docRef = db.collection("reviews").document(reviewId)
        val snapshot = docRef.get().await()
        if (!snapshot.exists()) {
            throw Exception("Reseña no encontrada en Firestore")
        }
        val reviewDto = snapshot.toObject(ReviewDto::class.java)
            ?: throw Exception("No se pudo deserializar la reseña de Firestore")

        return reviewDto.copy(id = snapshot.id)
    }

    override suspend fun createReview(review: CreateReviewGenericDto): String {
        val autorId = review.autor_id
        val resenadoId = review.resenado_id

        var autorDto: StudentDto? = null
        if (!autorId.isNullOrBlank()) {
            val userSnapshot = db.collection("users").document(autorId).get().await()
            if (userSnapshot.exists()) {
                autorDto = userSnapshot.toObject(StudentDto::class.java)?.copy(id = userSnapshot.id)
            }
        }

        var resenadoDto: StudentDto? = null
        if (!resenadoId.isNullOrBlank()) {
            val userSnapshot = db.collection("users").document(resenadoId).get().await()
            if (userSnapshot.exists()) {
                resenadoDto = userSnapshot.toObject(StudentDto::class.java)?.copy(id = userSnapshot.id)
            }
        }

        val firebaseDto = CreateReviewFirebaseDto(
            autor_id = review.autor_id,
            resenado_id = review.resenado_id,
            materia = review.materia,
            periodo = review.periodo,
            contenido = review.contenido,
            rating = review.rating,
            autor = autorDto,
            resenado = resenadoDto
        )

        val docRef = db.collection("reviews").add(firebaseDto).await()
        return docRef.id
    }

    override suspend fun updateReview(
        reviewId: String,
        review: CreateReviewGenericDto
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReview(reviewId: String) {
        TODO("Not yet implemented")
    }

}
