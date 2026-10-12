package com.example.thestudents.data.datasource

import com.example.thestudents.data.dtos.InscripcionDto

interface InscripcionRemoteDataSource {

    suspend fun getInscripcionesPorUsuario(usuarioId: String): List<InscripcionDto>

    suspend fun getInscripcionById(inscripcionId: String): InscripcionDto

}