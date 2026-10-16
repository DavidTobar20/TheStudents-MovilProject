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
    // Para probar con Retrofit, comentar la línea de arriba y descomentar la siguiente:
    // private val userRemoteDataSource: UserRetrofitDataSourceImpl
) {

    suspend fun getUserProfile(studentId: String): Result<UserProfile> {
        return try {
            val response = userRemoteDataSource.getUserProfile(studentId)
            val profile = response.toUserProfile()
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(userId: String): Result<UserProfile> {
        return getUserProfile(userId)
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
