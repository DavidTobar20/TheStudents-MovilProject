package com.example.thestudents.data.datasource.services

import com.example.thestudents.data.dtos.CreateReviewDto
import com.example.thestudents.data.dtos.ReviewDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReviewRetrofitService {

    @GET("resena/excluir/{student_id}")
    suspend fun getAllReviewsExcludingStudentId(@Path("student_id") studentId: String): List<ReviewDto>

    @GET("resena/resenado/{resenado_id}")
    suspend fun getReviewsByReviewedStudentId(@Path("resenado_id") resenado_id: String): List<ReviewDto>

    @GET("resena/autor/{autor_id}")
    suspend fun getReviewsByReviewerId(@Path("autor_id") autor_id: String): List<ReviewDto>

    @GET("resena/{resena_id}")
    suspend fun getReviewById(@Path("resena_id") resena_id: String): ReviewDto

    @POST("resena")
    suspend fun createReview(@Body review: CreateReviewDto): Unit

    @PUT("resena/{review_id}")
    suspend fun updateReview(@Path("review_id") reviewId: String, @Body review: CreateReviewDto): Unit

    @DELETE("resena/{review_id}")
    suspend fun deleteReview(@Path("review_id") reviewId: String): Unit



}