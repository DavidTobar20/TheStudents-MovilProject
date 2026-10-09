package com.example.thestudents.data.datasource.impl

import com.example.thestudents.data.datasource.services.InscripcionRetrofitService
import com.example.thestudents.data.dtos.InscripcionDto
import javax.inject.Inject

class InscripcionRetrofitDataSourceImpl @Inject constructor(
    private val service: InscripcionRetrofitService
) {
    suspend fun getInscripcionesPorUsuario(usuarioId: String): InscripcionDto {
        return service.getInscripcionesPorUsuario(usuarioId)
    }
}
