package com.example.thestudents.data.datasource.impl.firestore

import com.example.thestudents.data.datasource.InscripcionRemoteDataSource
import com.example.thestudents.data.dtos.InscripcionDto
import com.example.thestudents.data.dtos.MateriaDto
import com.example.thestudents.data.dtos.StudentDto
import com.example.thestudents.data.dtos.StudentSummaryDto
import com.example.thestudents.data.dtos.toSummaryDto
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
        val misInscripciones = getMisInscripciones(usuarioId)

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
                    .filter { it.usuario?.id != usuarioId } // no me incluyo a mí mismo
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

    override suspend fun getMisInscripciones(usuarioId: String): List<InscripcionDto> {
        return db.collection("inscripciones")
            .whereEqualTo("usuario.id", usuarioId)
            .get()
            .await()
            .documents
            .mapNotNull { it.toInscripcionDto() }
    }

    override suspend fun getMaterias(): List<MateriaDto> {
        return db.collection("materias")
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                doc.toObject(MateriaDto::class.java)?.copy(id = doc.id)
            }
    }

    override suspend fun createInscripcion(
        usuarioId: String,
        materia: String,
        periodo: String
    ): String {
        // 1. Verificar que el estudiante no haya visto/inscrito ya esta materia (en ningún periodo)
        val misInscripciones = getMisInscripciones(usuarioId)
        val yaLaVio = misInscripciones.any { it.materia.equals(materia, ignoreCase = true) }
        if (yaLaVio) {
            throw Exception("Ya cursaste o tienes inscrita la materia $materia")
        }

        // 2. Obtener los datos resumidos del estudiante desde la colección users
        val userSnapshot = db.collection("users").document(usuarioId).get().await()
        val usuarioSummary: StudentSummaryDto = if (userSnapshot.exists()) {
            userSnapshot.toObject(StudentDto::class.java)?.copy(id = userSnapshot.id)?.toSummaryDto()
                ?: StudentSummaryDto(id = usuarioId)
        } else {
            StudentSummaryDto(id = usuarioId)
        }

        // 3. Guardar la nueva inscripción en Firestore
        val nuevaInscripcion = mapOf(
            "materia" to materia,
            "periodo" to periodo,
            "usuario" to usuarioSummary
        )
        val docRef = db.collection("inscripciones").add(nuevaInscripcion).await()
        return docRef.id
    }

    // El id del documento no viene dentro del documento, hay que copiarlo.
    private fun DocumentSnapshot.toInscripcionDto(): InscripcionDto? {
        val documentId = id
        return toObject(InscripcionDto::class.java)?.copy(id = documentId)
    }

}
