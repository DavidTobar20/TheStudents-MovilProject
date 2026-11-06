package com.example.thestudents.data.local

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date

object SeedHelper {

    // Crea en Firestore (emulador) los datos que necesitan las pruebas E2E:
    // un usuario autor, una reseña suya y un comentario en esa reseña.
    // El id de la reseña empieza por "00000" para que sea la PRIMERA publicación en Home
    // (Firestore ordena los documentos por id).
    suspend fun seedUsersAndReviews(db: FirebaseFirestore) {
        val autor = mapOf(
            "id" to "e2e_autor",
            "nombre_usuario" to "autor_e2e",
            "nombre" to "Autor E2E"
        )
        val resenado = mapOf(
            "id" to "e2e_resenado",
            "nombre_usuario" to "resenado_e2e",
            "nombre" to "Resenado E2E"
        )

        // Usuario autor (necesario para ver su perfil y poder seguirlo)
        val user = mapOf(
            "nombre" to "Autor E2E",
            "nombre_usuario" to "autor_e2e",
            "carrera" to "Ingeniería de Sistemas",
            "semestre" to 5,
            "biografia" to "Autor de pruebas E2E",
            "followersCount" to 0,
            "followingCount" to 0
        )
        db.collection("users").document("e2e_autor").set(user).await()

        // Reseña escrita por el autor
        val review = mapOf(
            "autor_id" to "e2e_autor",
            "resenado_id" to "e2e_resenado",
            "autor" to autor,
            "resenado" to resenado,
            "materia" to "Desarrollo Móvil",
            "periodo" to "2026-2",
            "rating" to "5",
            "contenido" to "Excelente compañero, muy responsable",
            "fechaCreacion" to "2026-01-01T00:00:00.000Z",
            "likesCount" to 0,
            "disLikes" to 0,
            "commentsCount" to 1
        )
        val reviewRef = db.collection("reviews").document("00000_e2e_review")
        reviewRef.set(review).await()

        // Comentario en la reseña
        val comment = mapOf(
            "autor_id" to "e2e_resenado",
            "autor" to resenado,
            "contenido" to "Totalmente de acuerdo",
            "fecha_creacion" to Timestamp(Date(1767225600000L)),
            "likesCount" to 0,
            "disLikes" to 0
        )
        reviewRef.collection("comments").document("00000_e2e_comment").set(comment).await()
    }
}
