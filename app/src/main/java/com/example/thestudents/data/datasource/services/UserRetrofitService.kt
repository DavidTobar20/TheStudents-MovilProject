package com.example.thestudents.data.datasource.services

import com.example.thestudents.data.dtos.StudentDto
import com.example.thestudents.data.dtos.UserProfileDto
import retrofit2.http.GET
import retrofit2.http.Path

interface UserRetrofitService {

    @GET("usuario/perfil/{id}")
    suspend fun getUserProfile(@Path("id") studentId: String): UserProfileDto

    @GET("usuario/{id}")
    suspend fun getStudentById(@Path("id") studentId: String): StudentDto

}
