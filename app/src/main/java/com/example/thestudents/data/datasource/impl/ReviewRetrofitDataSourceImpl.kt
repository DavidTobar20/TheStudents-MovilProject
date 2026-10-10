package com.example.thestudents.data.datasource.impl

import com.example.thestudents.data.datasource.ReviewRemoteDataSource
import com.example.thestudents.data.datasource.services.ReviewRetrofitService
import com.example.thestudents.data.dtos.CreateReviewDto
import com.example.thestudents.data.dtos.ReviewDto
import retrofit2.HttpException
import javax.inject.Inject

class ReviewRetrofitDataSourceImpl @Inject constructor(
    val service: ReviewRetrofitService
) : ReviewRemoteDataSource {
    override suspend fun getAllReviewsExcludingStudentId(studentId: String): List<ReviewDto> {
        return service.getAllReviewsExcludingStudentId(studentId)
    }

    override suspend fun getReviewsByReviewedStudentId(reviewedStudentId: String): List<ReviewDto> {
        return service.getReviewsByReviewedStudentId(reviewedStudentId)
    }

    override suspend fun getReviewsByReviewerId(reviewerId: String): List<ReviewDto> {
        return service.getReviewsByReviewerId(reviewerId)
    }

    override suspend fun getReviewById(reviewId: String): ReviewDto {
        return service.getReviewById(reviewId)
    }

    override suspend fun createReview(review: CreateReviewDto) {
        service.createReview(review)
    }

    override suspend fun updateReview(
        reviewId: String,
        review: CreateReviewDto
    ) {
        service.updateReview(reviewId, review)
    }

    override suspend fun deleteReview(reviewId: String) {
        val response = service.deleteReview(reviewId)
        if (!response.isSuccessful) throw HttpException(response)
    }
}