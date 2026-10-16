package com.example.thestudents.data.datasource.impl.firestore

import com.example.thestudents.data.datasource.UserRemoteDataSource
import com.example.thestudents.data.dtos.RegisterUserDto
import com.example.thestudents.data.dtos.ReviewDto
import com.example.thestudents.data.dtos.StudentDto
import com.example.thestudents.data.dtos.UserDtoGeneric
import com.example.thestudents.data.dtos.UserProfileFirestoreDto
import com.example.thestudents.data.local.localStudentProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserFirestoreDataSourceImpl @Inject constructor(private val db: FirebaseFirestore) : UserRemoteDataSource {

    private suspend fun resolveStudentDto(userId: String?, currentDto: StudentDto?): StudentDto? {
        val targetId = userId ?: currentDto?.id
        if (targetId.isNullOrBlank()) return currentDto

        val userDoc = runCatching {
            db.collection("users").document(targetId).get().await()
                .toObject(UserProfileFirestoreDto::class.java)
        }.getOrNull()

        if (userDoc != null) {
            val fullName = if (!userDoc.apellido.isNullOrBlank()) {
                "${userDoc.name.orEmpty()} ${userDoc.apellido}".trim()
            } else {
                userDoc.name.orEmpty()
            }
            return StudentDto(
                id = targetId,
                nombre = fullName.ifBlank { currentDto?.nombre },
                nombreUsuario = userDoc.userName ?: currentDto?.nombreUsuario,
                fotoUrl = userDoc.fotoUrl?.takeIf { it.isNotBlank() } ?: currentDto?.fotoUrl,
                biografia = userDoc.biografia ?: currentDto?.biografia,
                carrera = userDoc.carrera ?: currentDto?.carrera,
                semestre = userDoc.semestre ?: currentDto?.semestre
            )
        }

        val localStudent = localStudentProvider.students.find { it.id == targetId }
        if (localStudent != null && currentDto == null) {
            return StudentDto(
                id = localStudent.id,
                nombre = localStudent.name,
                nombreUsuario = localStudent.username,
                fotoUrl = localStudent.profileImage,
                biografia = localStudent.bio,
                carrera = localStudent.program,
                semestre = localStudent.semester
            )
        }

        return currentDto?.copy(id = currentDto.id ?: targetId)
    }

    override suspend fun getUserProfile(studentId: String): UserProfileFirestoreDto {
        val docRef = db.collection("users").document(studentId)
        val snapshot = docRef.get().await()

        val userDto = if (snapshot.exists()) {
            snapshot.toObject(UserProfileFirestoreDto::class.java)
                ?: throw Exception("No se pudo deserializar el usuario de Firestore")
        } else {
            val fallback = localStudentProvider.students.find { it.id == studentId }
                ?: throw Exception("Usuario no encontrado en Firestore")
            UserProfileFirestoreDto(
                id = fallback.id,
                userName = fallback.username,
                name = fallback.name,
                apellido = "",
                biografia = fallback.bio,
                carrera = fallback.program,
                semestre = fallback.semester,
                fotoUrl = fallback.profileImage
            )
        }

        val createdSnapshot = db.collection("reviews")
            .whereEqualTo("autorId", studentId)
            .get()
            .await()
        val createdReviews = createdSnapshot.documents.mapNotNull { doc ->
            val review = doc.toObject(ReviewDto::class.java) ?: return@mapNotNull null
            review.copy(
                id = doc.id,
                autor = resolveStudentDto(review.autorId ?: studentId, review.autor),
                resenado = resolveStudentDto(review.resenadoId, review.resenado)
            )
        }

        val receivedSnapshot = db.collection("reviews")
            .whereEqualTo("resenadoId", studentId)
            .get()
            .await()
        val receivedReviews = receivedSnapshot.documents.mapNotNull { doc ->
            val review = doc.toObject(ReviewDto::class.java) ?: return@mapNotNull null
            review.copy(
                id = doc.id,
                autor = resolveStudentDto(review.autorId, review.autor),
                resenado = resolveStudentDto(review.resenadoId ?: studentId, review.resenado)
            )
        }

        return userDto.copy(
            id = if (snapshot.exists()) snapshot.id else studentId,
            receivedReviews = receivedReviews,
            createdReviews = createdReviews
        )
    }

    suspend fun getUserById(userId: String): UserDtoGeneric {
        return getUserProfile(userId)
    }

    override suspend fun registerUser(
        registerUserDto: RegisterUserDto,
        userId: String
    ) {
        val docRef = db.collection("users").document(userId) // se crea un documento
        docRef.set(registerUserDto).await() // insertando el documento
    }

    suspend fun updateUserPhoto(userId: String, photoUrl: String) {
        runCatching {
            db.collection("users").document(userId).update("fotoUrl", photoUrl).await()
        }
        runCatching {
            val authored = db.collection("reviews").whereEqualTo("autorId", userId).get().await()
            for (doc in authored.documents) {
                doc.reference.update("autor.fotoUrl", photoUrl).await()
            }
        }
        runCatching {
            val received = db.collection("reviews").whereEqualTo("resenadoId", userId).get().await()
            for (doc in received.documents) {
                doc.reference.update("resenado.fotoUrl", photoUrl).await()
            }
        }
    }
}
