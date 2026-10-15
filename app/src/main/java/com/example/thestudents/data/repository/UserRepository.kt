package com.example.thestudents.data.repository

import android.util.Log
import com.example.thestudents.data.UserProfile
import com.example.thestudents.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.retrofit.UserRetrofitDataSourceImpl
import com.example.thestudents.data.dtos.RegisterUserDto
import com.example.thestudents.data.dtos.toReview
import com.example.thestudents.data.dtos.toStudent
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserFirestoreDataSourceImpl
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

    suspend fun registerUser(username: String, name: String, apellido: String, userId: String) :Result<Unit>{
     return try{
         val registerUserDto = RegisterUserDto(username,name,apellido)
         userRemoteDataSource.registerUser(registerUserDto,userId)
         Result.success(Unit)
     } catch (e: Exception){
         Log.d("TAG", "getUserProfile: ${e.message}")
             Result.failure(e)
     }

    }
}
