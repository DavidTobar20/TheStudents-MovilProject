package com.example.thestudents.data.datasource.impl.firestore

import com.example.thestudents.data.datasource.ReviewRemoteDataSource
import com.example.thestudents.data.dtos.CreateReviewDto
import com.example.thestudents.data.dtos.ReviewDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReviewFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
): ReviewRemoteDataSource {
    override suspend fun getAllReviewsExcludingStudentId(studentId: String): List<ReviewDto> {
        return db.collection("reviews").get().await().toObjects(ReviewDto::class.java)
    }

    override suspend fun getReviewsByReviewedStudentId(reviewedStudentId: String): List<ReviewDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getReviewsByReviewerId(reviewerId: String): List<ReviewDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getReviewById(reviewId: String): ReviewDto {
        TODO("Not yet implemented")
    }

    override suspend fun createReview(review: CreateReviewDto) {
        db.collection("reviews").add(review).await()
    }

    override suspend fun updateReview(
        reviewId: String,
        review: CreateReviewDto
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReview(reviewId: String) {
        TODO("Not yet implemented")
    }

}