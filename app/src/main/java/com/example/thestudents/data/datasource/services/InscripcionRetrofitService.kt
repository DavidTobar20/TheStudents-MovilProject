package com.example.thestudents.data.datasource.services

import com.example.thestudents.data.dtos.InscripcionDto
import retrofit2.http.GET
import retrofit2.http.Path

interface InscripcionRetrofitService {

    @GET("inscripcion/usuario/{usuario_id}")
    suspend fun getInscripcionesPorUsuario(
        @Path("usuario_id") usuarioId: String
    ): InscripcionDto
}
