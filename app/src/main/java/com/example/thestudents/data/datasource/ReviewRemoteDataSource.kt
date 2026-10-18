package com.example.thestudents.data.datasource

import com.example.thestudents.data.dtos.CreateReviewGenericDto
import com.example.thestudents.data.dtos.ReviewDto

interface ReviewRemoteDataSource {

    suspend fun getAllReviewsExcludingStudentId(studentId: String): List<ReviewDto>

    suspend fun getReviewsByReviewedStudentId(reviewedStudentId: String): List<ReviewDto>

    suspend fun getReviewsByReviewerId(reviewerId: String): List<ReviewDto>

    suspend fun getReviewById(reviewId: String): ReviewDto

    suspend fun createReview(review: CreateReviewGenericDto): String

    suspend fun updateReview(reviewId: String, review: CreateReviewGenericDto): Unit

    suspend fun deleteReview(reviewId: String): Unit

}
