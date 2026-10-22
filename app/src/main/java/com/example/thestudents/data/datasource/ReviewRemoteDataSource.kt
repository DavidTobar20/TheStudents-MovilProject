package com.example.thestudents.data.datasource

import com.example.thestudents.data.dtos.CommentDto
import com.example.thestudents.data.dtos.CreateReviewGenericDto
import com.example.thestudents.data.dtos.HomeFeedDto
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

    suspend fun listenHomeFeed(studentId: String): Flow<HomeFeedDto>

    suspend fun getComments(reviewId: String, studentId: String): List<CommentDto>

    suspend fun createComment(reviewId: String, autorId: String, contenido: String): CommentDto

    suspend fun updateComment(reviewId: String, commentId: String, contenido: String): Unit

    suspend fun deleteComment(reviewId: String, commentId: String): Unit

    suspend fun sendOrDeleteCommentLike(reviewId: String, commentId: String, studentId: String): Unit

    suspend fun sendOrDeleteCommentDislike(reviewId: String, commentId: String, studentId: String): Unit

}
