package com.example.thestudents.data.datasource.services

import com.example.thestudents.data.dtos.StudentDto
import com.example.thestudents.data.dtos.UserProfileRetrofitDto
import retrofit2.http.GET
import retrofit2.http.Path

interface UserRetrofitService {

    @GET("usuario/perfil/{id}")
    suspend fun getUserProfile(@Path("id") studentId: String): UserProfileRetrofitDto

    @GET("usuario/{id}")
    suspend fun getStudentById(@Path("id") studentId: String): StudentDto

}
