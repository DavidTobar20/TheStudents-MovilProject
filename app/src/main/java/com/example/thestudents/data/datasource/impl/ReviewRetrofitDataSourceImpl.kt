package com.example.thestudents.data.datasource.impl

import com.example.thestudents.data.datasource.ReviewRemoteDataSource
import com.example.thestudents.data.datasource.services.ReviewRetrofitService
import com.example.thestudents.data.dtos.CreateReviewDto
import com.example.thestudents.data.dtos.ReviewDto
import com.example.thestudents.data.dtos.StudentDto
import javax.inject.Inject

class ReviewRetrofitDataSourceImpl @Inject constructor(
    val service: ReviewRetrofitService
) : ReviewRemoteDataSource {

    override suspend fun getAllUsers(): List<StudentDto> {
        return service.getAllUsers()
    }

    override suspend fun getUserById(id: String): StudentDto {
        return service.getUserById(id)
    }

    override suspend fun getAllReviewsExcludingStudentId(studentId: String): List<ReviewDto> {
        return service.getAllReviewsExcludingStudentId(studentId)
    }

    override suspend fun getReviewsByReviewedStudentId(reviewedStudentId: String): List<ReviewDto> {
        return service.getReviewsByReviewedStudentId(reviewedStudentId)
    }

    override suspend fun getReviewsByReviewerId(reviewerId: String): List<ReviewDto> {
        return service.getReviewsByReviewerId(reviewerId)
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
        service.deleteReview(reviewId)
    }
}