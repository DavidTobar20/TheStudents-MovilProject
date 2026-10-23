package com.example.thestudents.data.dtos

import com.example.thestudents.data.Comment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

// Comentario guardado en la subcoleccion reviews/{reviewId}/comments.
// "autor" va desnormalizado (nombre, usuario, foto) igual que en las reseñas.
data class CommentDto(
    @get:Exclude val id: String? = null, // el id es el del documento, no se guarda como campo
    val autor_id: String? = null,
    val autor: StudentSummaryDto? = null,
    val contenido: String? = null,
    val fecha_creacion: Timestamp? = null,
    val likesCount: Int = 0,
    val disLikes: Int = 0,
    // Se calculan al leer (revisando las subcolecciones likes/dislikes), no se guardan en Firestore
    @get:Exclude var isLiked: Boolean = false,
    @get:Exclude var isDisliked: Boolean = false
)

fun CommentDto.toComment(): Comment {
    return Comment(
        id = id.orEmpty(),
        commentator = (autor ?: StudentSummaryDto(id = autor_id)).toStudent(),
        content = contenido.orEmpty(),
        likes = likesCount,
        disLikes = disLikes,
        createdAt = "Hace " + timeAgo(fecha_creacion), // timeAgo esta en NotificationDto.kt
        isLiked = isLiked,
        isDisliked = isDisliked
    )
}
