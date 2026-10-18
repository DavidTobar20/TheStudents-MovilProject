package com.example.thestudents.data.datasource.impl.retrofit

import com.example.thestudents.data.datasource.UserRemoteDataSource
import com.example.thestudents.data.datasource.services.UserRetrofitService
import com.example.thestudents.data.dtos.RegisterUserDto
import com.example.thestudents.data.dtos.StudentDto
import com.example.thestudents.data.dtos.UserProfileDto
import javax.inject.Inject

class UserRetrofitDataSourceImpl @Inject constructor(
    private val service: UserRetrofitService
) : UserRemoteDataSource {

    override suspend fun getUserProfile(studentId: String): UserProfileDto {
        return service.getUserProfile(studentId)
    }

    override suspend fun getStudentById(studentId: String): StudentDto {
        return service.getStudentById(studentId)
    }

    override suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String) {
        TODO("Not yet implemented")
    }

}
