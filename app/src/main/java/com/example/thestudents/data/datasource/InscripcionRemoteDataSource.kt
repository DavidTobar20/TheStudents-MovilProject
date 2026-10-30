package com.example.thestudents.data.datasource

import com.example.thestudents.data.dtos.InscripcionDto
import com.example.thestudents.data.dtos.MateriaDto

interface InscripcionRemoteDataSource {
    suspend fun getInscripcionesPorUsuario(usuarioId: String): List<InscripcionDto>
    suspend fun getInscripcionById(inscripcionId: String): InscripcionDto
    suspend fun getMisInscripciones(usuarioId: String): List<InscripcionDto>
    suspend fun getMaterias(): List<MateriaDto>
    suspend fun createInscripcion(usuarioId: String, materia: String, periodo: String): String
}
