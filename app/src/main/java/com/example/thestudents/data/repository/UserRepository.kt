package com.example.thestudents.data.repository

import com.example.thestudents.data.Student
import com.example.thestudents.data.UserProfile
import com.example.thestudents.data.datasource.impl.UserRetrofitDataSourceImpl
import com.example.thestudents.data.dtos.toReview
import com.example.thestudents.data.dtos.toStudent
import com.example.thestudents.data.dtos.toUserProfile
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRetrofitDataSourceImpl
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
            val response = userRemoteDataSource.getStudentById(studentId)

            val student = response.toStudent()

            Result.success(student)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}