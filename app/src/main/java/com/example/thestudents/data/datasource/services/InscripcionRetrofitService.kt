package com.example.thestudents.data.datasource.services

import com.example.thestudents.data.dtos.InscripcionDto
import retrofit2.http.GET
import retrofit2.http.Path

interface InscripcionRetrofitService {

    @GET("inscripcion/companeros/{usuario_id}")
    suspend fun getInscripcionesPorUsuario(@Path("usuario_id") usuarioId: String): List<InscripcionDto>

    @GET("inscripcion/{id}")
    suspend fun getInscripcionById(@Path("id") inscripcionId: String): InscripcionDto
}
