package com.example.thestudents.data.datasource.impl.firestore

import com.example.thestudents.data.datasource.ReviewRemoteDataSource
import com.example.thestudents.data.dtos.CreateReviewFirebaseDto
import com.example.thestudents.data.dtos.CreateReviewGenericDto
import com.example.thestudents.data.dtos.ReviewDto
import com.example.thestudents.data.dtos.StudentDto
import com.example.thestudents.data.dtos.StudentSummaryDto
import com.example.thestudents.data.dtos.toSummaryDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
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

        val reviews = snapshot.documents.mapNotNull { doc ->
            val reviewDto = doc.toObject(ReviewDto::class.java)
                ?: throw Exception("No se pudo deserializar la reseña de Firestore")
            reviewDto.copy(id = doc.id)
        }.filter { review ->
            val autorIdStr = review.autor?.id
            autorIdStr != studentId
        }

        // Verifica si el estudiante actual ya le dio like o dislike a cada reseña.
        // Todas las consultas se lanzan al mismo tiempo (async) en vez de una por una.
        if (studentId.isNotEmpty()) {
            coroutineScope {
                reviews.map { review ->
                    async {
                        val reviewId = review.id ?: return@async
                        val reviewRef = db.collection("reviews").document(reviewId)
                        val liked = async { reviewRef.collection("likes").document(studentId).get().await().exists() }
                        val disliked = async { reviewRef.collection("dislikes").document(studentId).get().await().exists() }
                        review.isLiked = liked.await()
                        review.isDisliked = disliked.await()
                    }
                }.awaitAll()
            }
        }

        return reviews
    }

    override suspend fun getReviewsByReviewedStudentId(reviewedStudentId: String): List<ReviewDto> {
        val snapshot = db.collection("reviews")
            .whereEqualTo("resenado_id", reviewedStudentId)
            .get()
            .await()
        return snapshot.documents.mapNotNull { doc ->
            val review = doc.toObject(ReviewDto::class.java) ?: return@mapNotNull null
            review.copy(id = doc.id)
        }
    }

    override suspend fun getReviewsByReviewerId(reviewerId: String): List<ReviewDto> {
        val snapshot = db.collection("reviews")
            .whereEqualTo("autor_id", reviewerId)
            .get()
            .await()
        return snapshot.documents.mapNotNull { doc ->
            val review = doc.toObject(ReviewDto::class.java) ?: return@mapNotNull null
            review.copy(id = doc.id)
        }
    }

    override suspend fun getReviewById(reviewId: String, studentId: String): ReviewDto {
        val reviewRef = db.collection("reviews").document(reviewId)
        val reviewSnapshot = reviewRef.get().await()
        val review = reviewSnapshot.toObject(ReviewDto::class.java) ?: throw Exception("Reseña no encontrada en Firestore")

        // Verifica si el estudiante actual ya le dio like o dislike a la reseña
        if (studentId.isNotEmpty()) {
            coroutineScope {
                val liked = async { reviewRef.collection("likes").document(studentId).get().await().exists() }
                val disliked = async { reviewRef.collection("dislikes").document(studentId).get().await().exists() }
                review.isLiked = liked.await()
                review.isDisliked = disliked.await()
            }
        }

        return review.copy(id = reviewSnapshot.id)
    }

    override suspend fun createReview(review: CreateReviewGenericDto): String {
        val autorId = review.autor_id
        val resenadoId = review.resenado_id

        var autorSummaryDto: StudentSummaryDto? = null
        if (!autorId.isNullOrBlank()) {
            val userSnapshot = db.collection("users").document(autorId).get().await()
            if (userSnapshot.exists()) {
                autorSummaryDto = userSnapshot.toObject(StudentDto::class.java)?.copy(id = userSnapshot.id)?.toSummaryDto()
            }
        }

        var resenadoSummaryDto: StudentSummaryDto? = null
        if (!resenadoId.isNullOrBlank()) {
            val userSnapshot = db.collection("users").document(resenadoId).get().await()
            if (userSnapshot.exists()) {
                resenadoSummaryDto = userSnapshot.toObject(StudentDto::class.java)?.copy(id = userSnapshot.id)?.toSummaryDto()
            }
        }

        val firebaseDto = CreateReviewFirebaseDto(
            autor_id = review.autor_id,
            resenado_id = review.resenado_id,
            materia = review.materia,
            periodo = review.periodo,
            contenido = review.contenido,
            rating = review.rating,
            autor = autorSummaryDto,
            resenado = resenadoSummaryDto
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

    override suspend fun senOrDeleteReviewLikes(
        reviewId: String,
        studentId: String
    ) {
        val reviewRef = db.collection("reviews").document(reviewId)
        val likesRef = reviewRef.collection("likes").document(studentId)
        val dislikesRef = reviewRef.collection("dislikes").document(studentId)

        db.runTransaction { transaction ->
            val likeDoc = transaction.get(likesRef)
            val dislikeDoc = transaction.get(dislikesRef)

            if (likeDoc.exists()) {
                // Quitar like
                transaction.delete(likesRef)
                transaction.update(reviewRef, "likesCount", FieldValue.increment(-1))
            } else {
                // Dar like
                transaction.set(likesRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.update(reviewRef, "likesCount", FieldValue.increment(1))
                // Si tenia dislike, se lo quitamos
                if (dislikeDoc.exists()) {
                    transaction.delete(dislikesRef)
                    transaction.update(reviewRef, "disLikes", FieldValue.increment(-1))
                }
            }
        }.await()
    }

    override suspend fun sendOrDeleteReviewDislike(
        reviewId: String,
        studentId: String
    ) {
        val reviewRef = db.collection("reviews").document(reviewId)
        val dislikesRef = reviewRef.collection("dislikes").document(studentId)
        val likesRef = reviewRef.collection("likes").document(studentId)

        db.runTransaction { transaction ->

            val dislikeDoc = transaction.get(dislikesRef)
            val likeDoc = transaction.get(likesRef)

            if (dislikeDoc.exists()) {
                // Quitar dislike
                transaction.delete(dislikesRef)
                transaction.update(reviewRef, "disLikes", FieldValue.increment(-1))
            } else {
                // Dar dislike
                transaction.set(dislikesRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.update(reviewRef, "disLikes", FieldValue.increment(1))
                // Si tenia like, se lo quitamos
                if (likeDoc.exists()) {
                    transaction.delete(likesRef)
                    transaction.update(reviewRef, "likesCount", FieldValue.increment(-1))
                }
            }
        }.await()
    }

    override suspend fun listenAllReviewsExcludingStudentId(studentId: String): Flow<List<ReviewDto>> = callbackFlow {
        val listener = db.collection("reviews").addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val reviews = snapshot.documents.map { doc ->
                    val review = doc.toObject(ReviewDto::class.java)
                    review?.copy(id = doc.id) ?: throw Exception("Reseña no encontrada")
                }.filter { review ->
                    val autorIdStr = review.autor?.id
                    autorIdStr != studentId
                }

                launch {
                    if (studentId.isNotEmpty()) {
                        reviews.map { review ->
                            async {
                                val reviewId = review.id ?: return@async
                                val reviewRef = db.collection("reviews").document(reviewId)
                                val liked = async { reviewRef.collection("likes").document(studentId).get().await().exists() }
                                val disliked = async { reviewRef.collection("dislikes").document(studentId).get().await().exists() }
                                review.isLiked = liked.await()
                                review.isDisliked = disliked.await()
                            }
                        }.awaitAll()
                    }
                    trySend(reviews).isSuccess
                }
            }
        }
        awaitClose { listener.remove() }
    }

}
