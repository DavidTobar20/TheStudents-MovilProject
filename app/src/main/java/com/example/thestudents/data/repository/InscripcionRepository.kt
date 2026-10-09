package com.example.thestudents.data.repository

import com.example.thestudents.data.Inscription
import com.example.thestudents.data.datasource.impl.InscripcionRetrofitDataSourceImpl
import com.example.thestudents.data.dtos.toInscriptions
import javax.inject.Inject

class InscripcionRepository @Inject constructor(
    private val inscripcionRemoteDataSource: InscripcionRetrofitDataSourceImpl
) {

    suspend fun getInscripcionesPorUsuario(usuarioId: String): Result<List<Inscription>> {
        return try {
            val response = inscripcionRemoteDataSource.getInscripcionesPorUsuario(usuarioId)
            Result.success(response.toInscriptions())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
