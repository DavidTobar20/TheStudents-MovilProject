package com.example.thestudents.data.datasource.impl

import com.example.thestudents.data.datasource.UserRemoteDataSource
import com.example.thestudents.data.datasource.services.UserRetrofitService
import com.example.thestudents.data.dtos.UserProfileResponseDto
import javax.inject.Inject

class UserRetrofitDataSourceImpl @Inject constructor(
    private val service: UserRetrofitService
) : UserRemoteDataSource {

    override suspend fun getUserProfile(studentId: String): UserProfileResponseDto {
        return service.getUserProfile(studentId)
    }

}
