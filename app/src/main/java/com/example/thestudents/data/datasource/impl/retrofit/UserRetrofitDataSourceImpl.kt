package com.example.thestudents.data.datasource.impl.retrofit

import com.example.thestudents.data.datasource.UserRemoteDataSource
import com.example.thestudents.data.datasource.services.UserRetrofitService
import com.example.thestudents.data.dtos.RegisterUserDto
import com.example.thestudents.data.dtos.UserProfileResponseDto
import javax.inject.Inject

class UserRetrofitDataSourceImpl @Inject constructor(
    private val service: UserRetrofitService
) : UserRemoteDataSource {



    override suspend fun getUserProfile(studentId: String): UserProfileResponseDto {
        val targetId = if (studentId.toIntOrNull() != null) studentId else "1"
        return service.getUserProfile(targetId)
    }

    override suspend fun registerUser(registerUserDto: RegisterUserDto, userId : String){
        //to-do
    }
}
