package com.example.thestudents.data.datasource

import com.example.thestudents.data.dtos.RegisterUserDto
import com.example.thestudents.data.dtos.StudentDto
import com.example.thestudents.data.dtos.UserProfileGenericDto

interface UserRemoteDataSource {

    suspend fun getUserProfile(studentId: String, currentUserId: String): UserProfileGenericDto

    suspend fun getUserById(studentId: String): StudentDto

    suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String)

    suspend fun getAllUsers(): List<StudentDto>

    suspend fun updateUser(registerUserDto: RegisterUserDto, userId: String)

    suspend fun updateFcmToken(userId: String, token: String)

    suspend fun followOrUnfollowStudent(currentUserId: String, targetUserId: String): Unit

    suspend fun getFollowers(userId: String): List<StudentDto>

    suspend fun getFollowing(userId: String): List<StudentDto>

}
