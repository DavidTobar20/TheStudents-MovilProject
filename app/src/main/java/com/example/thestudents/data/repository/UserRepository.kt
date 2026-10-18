package com.example.thestudents.data.repository

import android.util.Log
import com.example.thestudents.data.Student
import com.example.thestudents.data.UserProfile
import com.example.thestudents.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.thestudents.data.dtos.RegisterUserDto
import com.example.thestudents.data.dtos.toStudent
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserFirestoreDataSourceImpl
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

    suspend fun getUserById(studentId: String): Result<Student> {
        return try {
            val response = userRemoteDataSource.getUserById(studentId)

            val student = response.toStudent()

            Result.success(student)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerUser(
        name: String,
        username: String,
        program: String,
        semester: Int,
        bio: String,
        userId: String
    ): Result<Unit> {
        return try {
            val registerUserDto = RegisterUserDto(
                nombre = name,
                nombre_usuario = username,
                carrera = program,
                semestre = semester,
                biografia = bio
            )
            userRemoteDataSource.registerUser(registerUserDto, userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.d("TAG", "registerUser: ${e.message}")
            Result.failure(e)
        }
    }
}
