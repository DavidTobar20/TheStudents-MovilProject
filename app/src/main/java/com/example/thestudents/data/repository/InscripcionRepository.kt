package com.example.thestudents.data.repository

import com.example.thestudents.data.CourseSection
import com.example.thestudents.data.Inscription
import com.example.thestudents.data.datasource.impl.firestore.InscripcionFirestoreDataSourceImpl
import com.example.thestudents.data.dtos.toInscription
import javax.inject.Inject

class InscripcionRepository @Inject constructor(
    private val inscripcionRemoteDataSource: InscripcionFirestoreDataSourceImpl
) {

    suspend fun getInscripcionesPorUsuario(usuarioId: String): Result<List<CourseSection>> {
        return try {
            val misInscripciones = inscripcionRemoteDataSource.getMisInscripciones(usuarioId)
            val companerosDto = inscripcionRemoteDataSource.getInscripcionesPorUsuario(usuarioId)
            val companeros = companerosDto.map { it.toInscription() }

            val companerosPorCurso = companeros.groupBy { it.className to it.period }

            // Se arma una sección por cada materia/periodo que el usuario tiene inscrita,
            // incluyendo los compañeros que comparten esa misma materia y periodo.
            val sections = misInscripciones
                .distinctBy { it.materia to it.periodo }
                .map { mia ->
                    val key = mia.materia to mia.periodo
                    CourseSection(
                        title = mia.materia,
                        period = mia.periodo,
                        inscriptions = companerosPorCurso[key] ?: emptyList()
                    )
                }

            Result.success(sections)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getInscripcionById(inscripcionId: String): Result<Inscription> {
        return try {
            val response = inscripcionRemoteDataSource.getInscripcionById(inscripcionId)
            val inscription = response.toInscription()
            Result.success(inscription)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Devuelve las materias que el usuario ya vio o tiene inscritas (en cualquier periodo)
    suspend fun getMateriasYaInscritas(usuarioId: String): Result<Set<String>> {
        return try {
            val misInscripciones = inscripcionRemoteDataSource.getMisInscripciones(usuarioId)
            val materias = misInscripciones.map { it.materia }.filter { it.isNotBlank() }.toSet()
            Result.success(materias)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtiene las materias y periodos directamente desde las inscripciones registradas en Firestore
    suspend fun getCatalogoMateriasYPeriodos(): Result<Map<String, List<String>>> {
        return try {
            val todas = inscripcionRemoteDataSource.getTodasLasInscripciones()
            val mapaMaterias = mutableMapOf<String, MutableSet<String>>()

            todas.forEach { insc ->
                if (insc.materia.isNotBlank() && insc.periodo.isNotBlank()) {
                    mapaMaterias.getOrPut(insc.materia) { linkedSetOf() }.add(insc.periodo)
                }
            }

            val resultado = mapaMaterias.mapValues { (_, periodos) ->
                periodos.sortedDescending()
            }
            Result.success(resultado)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createInscripcion(
        usuarioId: String,
        materia: String,
        periodo: String
    ): Result<Unit> {
        return try {
            inscripcionRemoteDataSource.createInscripcion(
                usuarioId = usuarioId,
                materia = materia,
                periodo = periodo
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
