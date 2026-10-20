package com.example.thestudents.data.datasource.impl.retrofit

import com.example.thestudents.data.datasource.UserRemoteDataSource
import com.example.thestudents.data.datasource.services.UserRetrofitService
import com.example.thestudents.data.dtos.RegisterUserDto
import com.example.thestudents.data.dtos.StudentDto
import com.example.thestudents.data.dtos.UserProfileGenericDto
import javax.inject.Inject

class UserRetrofitDataSourceImpl @Inject constructor(
    private val service: UserRetrofitService
) : UserRemoteDataSource {

    override suspend fun getUserProfile(studentId: String, currentUserId: String): UserProfileGenericDto {
        return service.getUserProfile(studentId)
    }

    override suspend fun getUserById(studentId: String): StudentDto {
        val studentIdEnviar = studentId.toIntOrNull()?.toString() ?: "1"
        return service.getStudentById(studentIdEnviar)
    }

    override suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllUsers(): List<StudentDto> {
        TODO("El backend de Express no tiene un endpoint para listar usuarios")
    }

    override suspend fun updateUser(
        registerUserDto: RegisterUserDto,
        userId: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun followOrUnfollowStudent(
        currenUserId: String,
        targetUserId: String
    ) {
        TODO("Not yet implemented")
    }

}
