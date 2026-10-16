package com.example.thestudents.data.datasource.impl.retrofit

import com.example.thestudents.data.datasource.UserRemoteDataSource
import com.example.thestudents.data.datasource.services.UserRetrofitService
import com.example.thestudents.data.dtos.RegisterUserDto
import com.example.thestudents.data.dtos.UserDtoGeneric
import com.example.thestudents.data.dtos.UserProfileResponseDto
import javax.inject.Inject

class UserRetrofitDataSourceImpl @Inject constructor(
    private val service: UserRetrofitService
) : UserRemoteDataSource {

    override suspend fun getUserProfile(studentId: String): UserProfileResponseDto {
        // En Retrofit (Express/Postgres), si viene un UID alfanumérico de Firebase,
        // se usa "1" para evitar que el backend SQL falle con IDs no numéricos (como en el min 27:01)
        val targetId = studentId.toIntOrNull()?.toString() ?: "1"
        return service.getUserProfile(targetId)
    }

    override suspend fun registerUser(registerUserDto: RegisterUserDto, userId : String){
        //to-do
    }
}
