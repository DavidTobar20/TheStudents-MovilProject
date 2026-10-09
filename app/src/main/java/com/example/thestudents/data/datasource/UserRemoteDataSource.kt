package com.example.thestudents.data.datasource

import com.example.thestudents.data.dtos.UserProfileResponseDto

interface UserRemoteDataSource {

    suspend fun getUserProfile(studentId: String): UserProfileResponseDto

}
