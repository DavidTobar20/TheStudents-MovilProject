package com.example.thestudents.data.datasource

import com.example.thestudents.data.dtos.RegisterUserDto
import com.example.thestudents.data.dtos.UserDtoGeneric

interface UserRemoteDataSource {

    suspend fun getUserProfile(studentId: String): UserDtoGeneric
    suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String): Unit

}
