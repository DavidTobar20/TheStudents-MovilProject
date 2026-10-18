package com.example.thestudents.data.datasource.impl.retrofit

import com.example.thestudents.data.datasource.InscripcionRemoteDataSource
import com.example.thestudents.data.datasource.services.InscripcionRetrofitService
import com.example.thestudents.data.dtos.InscripcionDto
import javax.inject.Inject

class InscripcionRetrofitDataSourceImpl @Inject constructor(
    private val service: InscripcionRetrofitService
) : InscripcionRemoteDataSource {
    override suspend fun getInscripcionesPorUsuario(usuarioId: String): List<InscripcionDto> {
        return service.getInscripcionesPorUsuario(usuarioId)
    }

    override suspend fun getInscripcionById(inscripcionId: String): InscripcionDto {
        return service.getInscripcionById(inscripcionId)
    }

}
