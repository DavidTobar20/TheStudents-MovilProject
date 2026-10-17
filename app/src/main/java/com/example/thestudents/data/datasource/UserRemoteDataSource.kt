package com.example.thestudents.data.datasource

import com.example.thestudents.data.dtos.StudentDto
import com.example.thestudents.data.dtos.UserProfileDto

interface UserRemoteDataSource {

    suspend fun getUserProfile(studentId: String): UserProfileDto

    suspend fun getStudentById(studentId: String): StudentDto

}
