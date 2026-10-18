package com.example.thestudents.data.repository

import android.util.Log
import com.example.thestudents.data.CourseSection
import com.example.thestudents.data.Inscription
import com.example.thestudents.data.datasource.impl.firestore.InscripcionFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.retrofit.InscripcionRetrofitDataSourceImpl
import com.example.thestudents.data.dtos.toInscription
import javax.inject.Inject

class InscripcionRepository @Inject constructor(
    private val inscripcionRemoteDataSource: InscripcionFirestoreDataSourceImpl
) {

    suspend fun getInscripcionesPorUsuario(usuarioId: String): Result<List<CourseSection>> {
        return try {
            val response = inscripcionRemoteDataSource.getInscripcionesPorUsuario(usuarioId)
            val inscriptions = response.map { it.toInscription() }

            val sections = inscriptions.groupBy { it.className to it.period }.map { (key, inscrs) ->
                val (className, period) = key
                CourseSection(
                    title = className,
                    period = period,
                    inscriptions = inscrs
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

}
