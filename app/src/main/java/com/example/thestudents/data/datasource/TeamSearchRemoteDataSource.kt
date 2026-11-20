package com.example.thestudents.data.datasource

import com.example.thestudents.data.dtos.BusquedaEquipoDto
import com.example.thestudents.data.dtos.StudentSummaryDto

interface TeamSearchRemoteDataSource {
    suspend fun saveSearch(usuario: StudentSummaryDto, materia: String, periodo: String)
    suspend fun deleteSearch(usuarioId: String, materia: String, periodo: String)
    suspend fun getSearchesByCourse(materia: String, periodo: String): List<BusquedaEquipoDto>
    suspend fun getMySearches(usuarioId: String, periodo: String): List<BusquedaEquipoDto>
}
