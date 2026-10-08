package com.example.thestudents.data.repository

import coil.network.HttpException
import com.example.thestudents.data.Review
import com.example.thestudents.data.datasource.impl.ReviewRetrofitDataSourceImpl
import com.example.thestudents.data.dtos.CreateReviewDto
import com.example.thestudents.data.dtos.toReview
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val reviewRemoteDataSource: ReviewRetrofitDataSourceImpl
) {

    suspend fun getAllReviewsExcludingStudentId(studentId: String): Result<List<Review>> {
        return try {
            val reviewsDto = reviewRemoteDataSource.getAllReviewsExcludingStudentId(studentId)
            val reviews = reviewsDto.map { it.toReview() }
            Result.success(reviews)
        } catch (e: HttpException){
            Result.failure(e)
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun getReviewsByReviewedStudentId(reviewedStudentId: String): Result<List<Review>> {
        return try {
            val reviewsDto = reviewRemoteDataSource.getReviewsByReviewedStudentId(reviewedStudentId)
            val reviews = reviewsDto.map { it.toReview() }
            Result.success(reviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsByReviewerId(reviewerId: String): Result<List<Review>> {
        return try {
            val reviewsDto = reviewRemoteDataSource.getReviewsByReviewerId(reviewerId)
            val reviews = reviewsDto.map { it.toReview() }
            Result.success(reviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createReview(
        autorId: String,
        resenadoId: String,
        materia: String,
        periodo: String,
        rating: String?,
        contenido: String
    ): Result<Unit> {
        return try{
            val createReviewDto = CreateReviewDto(
                autorId = autorId.toInt(),
                resenadoId = resenadoId.toInt(),
                materia = materia,
                periodo = periodo,
                rating = rating,
                contenido = contenido,
                estado = null
            )
            reviewRemoteDataSource.createReview(createReviewDto)
            Result.success(Unit)

        } catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun updateReview(
        reviewId: String,
        rating: String?,
        contenido: String?,
        estado: String?
    ): Result<Unit> {
        return try {
            val createReviewDto = CreateReviewDto(
                autorId = null,
                resenadoId = null,
                materia = null,
                periodo = null,
                contenido = contenido,
                rating = rating,
                estado = estado
            )
            reviewRemoteDataSource.updateReview(reviewId, createReviewDto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteReview(reviewId: String): Result<Unit> {
        return try {
            reviewRemoteDataSource.deleteReview(reviewId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}