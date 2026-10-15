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
    private val firestoreDataSource: UserFirestoreDataSourceImpl,
    private val retrofitDataSource: UserRetrofitDataSourceImpl
) {

    companion object {
        /**
         * Bandera para alternar entre fuentes de datos en la entrega:
         * - false: Consulta el perfil desde el backend REST (Retrofit).
         * - true: Consulta el perfil desde Firebase Cloud Firestore.
         */
        var USE_FIRESTORE_FOR_PROFILE: Boolean = false
    }

    suspend fun getUserProfile(studentId: String): Result<UserProfile> {
        return try {
            val response = if (USE_FIRESTORE_FOR_PROFILE) {
                firestoreDataSource.getUserProfile(studentId)
            } else {
                retrofitDataSource.getUserProfile(studentId)
            }
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
         firestoreDataSource.registerUser(registerUserDto,userId)
         Result.success(Unit)
     } catch (e: Exception){
         Log.d("TAG", "registerUser: ${e.message}")
             Result.failure(e)
     }

    }
}
