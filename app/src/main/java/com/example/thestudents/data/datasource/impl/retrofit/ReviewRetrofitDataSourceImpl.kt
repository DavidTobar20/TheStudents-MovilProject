package com.example.thestudents.data.datasource.impl.retrofit

import com.example.thestudents.data.datasource.ReviewRemoteDataSource
import com.example.thestudents.data.datasource.services.ReviewRetrofitService
import com.example.thestudents.data.dtos.CreateReviewGenericDto
import com.example.thestudents.data.dtos.CreateReviewRetrofitDto
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

    override suspend fun createReview(review: CreateReviewGenericDto): String {
        val retrofitDto = CreateReviewRetrofitDto(
            autor_id = review.autor_id,
            resenado_id = review.resenado_id,
            materia = review.materia,
            periodo = review.periodo,
            contenido = review.contenido,
            rating = review.rating
        )
        service.createReview(retrofitDto)
        return ""
    }

    override suspend fun updateReview(
        reviewId: String,
        review: CreateReviewGenericDto
    ) {
        val retrofitDto = CreateReviewRetrofitDto(
            autor_id = review.autor_id,
            resenado_id = review.resenado_id,
            materia = review.materia,
            periodo = review.periodo,
            contenido = review.contenido,
            rating = review.rating
        )
        service.updateReview(reviewId, retrofitDto)
    }

    override suspend fun deleteReview(reviewId: String) {
        val response = service.deleteReview(reviewId)
        if (!response.isSuccessful) throw HttpException(response)
    }
}
