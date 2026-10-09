package com.example.thestudents.data.repository

import com.example.thestudents.data.UserProfile
import com.example.thestudents.data.datasource.impl.UserRetrofitDataSourceImpl
import com.example.thestudents.data.dtos.toReview
import com.example.thestudents.data.dtos.toStudent
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRetrofitDataSourceImpl
) {

    suspend fun getUserProfile(studentId: String): Result<UserProfile> {
        return try {
            val response = userRemoteDataSource.getUserProfile(studentId)
            val student = response.usuario.toStudent()
            val receivedReviews = response.resenasRecibidas.map { it.toReview(response.usuario) }
            val createdReviews = response.resenasCreadas.map { it.toReview(response.usuario) }

            val profile = UserProfile(
                student = student,
                receivedReviews = receivedReviews,
                createdReviews = createdReviews
            )

            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
