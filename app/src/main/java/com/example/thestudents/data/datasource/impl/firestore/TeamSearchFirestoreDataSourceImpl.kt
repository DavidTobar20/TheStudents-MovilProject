package com.example.thestudents.data.datasource.impl.firestore

import com.example.thestudents.data.datasource.TeamSearchRemoteDataSource
import com.example.thestudents.data.dtos.BusquedaEquipoDto
import com.example.thestudents.data.dtos.StudentSummaryDto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TeamSearchFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : TeamSearchRemoteDataSource {

    private val collection get() = db.collection("busquedasEquipo")

    // Id fijo por usuario + materia + periodo: si vuelve a buscar, se actualiza en vez de duplicarse.
    // Firestore no permite "/" en los ids.
    private fun documentId(usuarioId: String, materia: String, periodo: String): String =
        "${usuarioId}_${materia}_$periodo".replace("/", "-")

    override suspend fun saveSearch(usuario: StudentSummaryDto, materia: String, periodo: String) {
        val usuarioId = usuario.id.orEmpty()
        val busqueda = BusquedaEquipoDto(
            materia = materia,
            periodo = periodo,
            usuario = usuario,
            fechaCreacion = Timestamp.now()
        )
        collection.document(documentId(usuarioId, materia, periodo)).set(busqueda).await()
    }

    override suspend fun deleteSearch(usuarioId: String, materia: String, periodo: String) {
        collection.document(documentId(usuarioId, materia, periodo)).delete().await()
    }

    override suspend fun getSearchesByCourse(materia: String, periodo: String): List<BusquedaEquipoDto> {
        return collection
            .whereEqualTo("materia", materia)
            .whereEqualTo("periodo", periodo)
            .get()
            .await()
            .documents
            .mapNotNull { doc -> doc.toObject(BusquedaEquipoDto::class.java)?.copy(id = doc.id) }
    }

    override suspend fun getMySearches(usuarioId: String, periodo: String): List<BusquedaEquipoDto> {
        // Solo un whereEqualTo para no necesitar índice compuesto; el periodo se filtra aquí
        return collection
            .whereEqualTo("usuario.id", usuarioId)
            .get()
            .await()
            .documents
            .mapNotNull { doc -> doc.toObject(BusquedaEquipoDto::class.java)?.copy(id = doc.id) }
            .filter { it.periodo == periodo }
    }
}
