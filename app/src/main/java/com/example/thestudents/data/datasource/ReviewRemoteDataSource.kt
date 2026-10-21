package com.example.thestudents.data.datasource

import com.example.thestudents.data.dtos.CreateReviewGenericDto
import com.example.thestudents.data.dtos.ReviewDto
import kotlinx.coroutines.flow.Flow

interface ReviewRemoteDataSource {

    suspend fun getAllReviewsExcludingStudentId(studentId: String): List<ReviewDto>

    suspend fun getReviewsByReviewedStudentId(reviewedStudentId: String): List<ReviewDto>

    suspend fun getReviewsByReviewerId(reviewerId: String): List<ReviewDto>

    suspend fun getReviewById(reviewId: String, studentId: String): ReviewDto

    suspend fun createReview(review: CreateReviewGenericDto): String

    suspend fun updateReview(reviewId: String, review: CreateReviewGenericDto): Unit

    suspend fun deleteReview(reviewId: String): Unit

    suspend fun senOrDeleteReviewLikes(reviewId: String, studentId: String): Unit

    suspend fun sendOrDeleteReviewDislike(reviewId: String, studentId: String): Unit

    suspend fun listenAllReviewsExcludingStudentId(studentId: String): Flow<List<ReviewDto>>

}
