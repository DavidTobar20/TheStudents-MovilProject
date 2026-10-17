package com.example.thestudents.data.repository

import android.util.Log
import com.example.thestudents.data.Student
import com.example.thestudents.data.UserProfile
enimport com.example.thestudents.data.datasource.impl.firestore.ReviewFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.retrofit.UserRetrofitDataSourceImpl
import com.example.thestudents.data.dtos.RegisterUserDto
import com.example.thestudents.data.dtos.toReview
import com.example.thestudents.data.dtos.toStudent
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserFirestoreDataSourceImpl,
    // Para probar con Retrofit, comentar la línea de arriba y descomentar la siguiente:
    // private val userRemoteDataSource: UserRetrofitDataSourceImpl,
    private val reviewRemoteDataSource: ReviewFirestoreDataSourceImpl
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

    // Todos los usuarios registrados (sin incluir al usuario actual),
    // con el numero de reseñas recibidas y el promedio de su calificacion
    suspend fun getAllStudents(excludeId: String?): Result<List<Student>> {
        return try {
            val users = userRemoteDataSource.getAllUsers()
            val students = coroutineScope {
                users
                    .map { it.toUserProfile().student }
                    .filter { it.id != excludeId }
                    .map { student ->
                        // Las consultas de reseñas de cada estudiante se hacen en paralelo
                        async {
                            val receivedReviews = reviewRemoteDataSource.getReviewsByReviewedStudentId(student.id)
                            val ratings = receivedReviews.mapNotNull { it.rating?.toFloatOrNull() }
                            student.copy(
                                reviewsCount = receivedReviews.size,
                                rating = if (ratings.isEmpty()) 0f else ratings.average().toFloat()
                            )
                        }
                    }
                    .awaitAll()
            }
            Result.success(students)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
