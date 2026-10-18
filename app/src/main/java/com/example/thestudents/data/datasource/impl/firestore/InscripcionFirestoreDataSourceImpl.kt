package com.example.thestudents.data.datasource.impl.firestore

import android.util.Log
import com.example.thestudents.data.datasource.InscripcionRemoteDataSource
import com.example.thestudents.data.dtos.InscripcionDto
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class InscripcionFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
): InscripcionRemoteDataSource {

    // Equivalente a GET inscripcion/companeros/{usuario_id}:
    // devuelve las inscripciones de los compañeros que comparten materia y periodo con el usuario.
    override suspend fun getInscripcionesPorUsuario(usuarioId: String): List<InscripcionDto> {
        // 1. Mis inscripciones (en qué materias y periodos estoy)
        val misInscripciones = db.collection("inscripciones")
            .whereEqualTo("usuario.id", usuarioId)
            .get()
            .await()
            .documents
            .mapNotNull { it.toInscripcionDto() }

        // 2. Por cada materia/periodo, traer las inscripciones de los demás estudiantes
        val companeros = mutableListOf<InscripcionDto>()
        misInscripciones
            .distinctBy { it.materia to it.periodo }
            .forEach { mia ->
                companeros += db.collection("inscripciones")
                    .whereEqualTo("materia", mia.materia)
                    .whereEqualTo("periodo", mia.periodo)
                    .get()
                    .await()
                    .documents
                    .mapNotNull { it.toInscripcionDto() }
                    .filter { it.usuario.id != usuarioId } // no me incluyo a mí mismo
            }
        return companeros
    }

    override suspend fun getInscripcionById(inscripcionId: String): InscripcionDto {
        val snapshot = db.collection("inscripciones").document(inscripcionId).get().await()
        if (!snapshot.exists()) {
            throw Exception("Inscripción no encontrada")
        }
        val inscripcion = snapshot.toInscripcionDto()
            ?: throw Exception("No se pudo deserializar la inscripción de Firestore")

        return inscripcion
    }

    // El id del documento no viene dentro del documento, hay que copiarlo.
    private fun DocumentSnapshot.toInscripcionDto(): InscripcionDto? {
        val documentId = id
        return toObject(InscripcionDto::class.java)?.copy(id = documentId)
    }


}