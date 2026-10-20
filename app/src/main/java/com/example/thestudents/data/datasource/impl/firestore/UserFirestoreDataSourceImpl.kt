package com.example.thestudents.data.datasource.impl.firestore

import com.example.thestudents.data.datasource.UserRemoteDataSource
import com.example.thestudents.data.dtos.RegisterUserDto
import com.example.thestudents.data.dtos.ReviewDto
import com.example.thestudents.data.dtos.StudentDto
import com.example.thestudents.data.dtos.UserProfileFirestoreDto
import com.example.thestudents.data.dtos.UserProfileGenericDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.collections.mapOf

class UserFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : UserRemoteDataSource {

    override suspend fun getUserProfile(studentId: String, currentUserId: String): UserProfileGenericDto {
        val docRefUser = db.collection("users").document(studentId)
        val snapshotUser = try {
            docRefUser.get().await()
        } catch (e: Exception) {
            throw Exception("Error al conectar con Firestore para obtener el usuario: ${e.message}")
        }

        if (!snapshotUser.exists()) {
            throw Exception("Usuario no encontrado en Firestore")
        }
        val userProfileDto = snapshotUser.toObject(UserProfileFirestoreDto::class.java)
            ?: throw Exception("No se pudo deserializar el usuario de Firestore")
        
        val profileWithId = userProfileDto.copy(id = snapshotUser.id)

        // Verifica si el usuario actual ya sigue a este estudiante
        if (currentUserId.isNotEmpty()) {
            val followerDoc = db.collection("users").document(studentId).collection("followers").document(currentUserId).get().await()
            val exist = followerDoc.exists()
            profileWithId.followed = exist
        }

        val docRefReviews = db.collection("reviews").whereEqualTo("autor_id", studentId)
        val snapshotReviews = try {
            docRefReviews.get().await()
        } catch (e: Exception) {
            throw Exception("Error al obtener las reseñas creadas: ${e.message}")
        }
        val createdReviews = snapshotReviews.documents.mapNotNull { doc ->
            doc.toObject(ReviewDto::class.java)?.copy(id = doc.id)
        }

        val docRefReceived = db.collection("reviews").whereEqualTo("resenado_id", studentId)
        val snapshotReceived = try {
            docRefReceived.get().await()
        } catch (e: Exception) {
            throw Exception("Error al obtener las reseñas recibidas: ${e.message}")
        }
        val receivedReviews = snapshotReceived.documents.mapNotNull { doc ->
            doc.toObject(ReviewDto::class.java)?.copy(id = doc.id)
        }

        return profileWithId.copy(
            resenas_creadas = createdReviews,
            resenas_recibidas = receivedReviews
        )
    }

    override suspend fun getUserById(studentId: String): StudentDto {
        val docRef = db.collection("users").document(studentId)
        val snapshot = docRef.get().await()
        if (!snapshot.exists()) {
            throw Exception("Usuario no encontrado en Firestore")
        }
        val studentDto = snapshot.toObject(StudentDto::class.java)
            ?: throw Exception("No se pudo deserializar el estudiante de Firestore")
        return studentDto.copy(
            id = snapshot.id
        )
    }

    override suspend fun registerUser(
        registerUserDto: RegisterUserDto,
        userId: String
    ) {
        val docRef = db.collection("users").document(userId) // se crea un documento
        docRef.set(registerUserDto).await() // insertando el documento
    }

    override suspend fun getAllUsers(): List<StudentDto> {
        val snapshot = db.collection("users").get().await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(StudentDto::class.java)?.copy(id = doc.id)
        }
    }

    override suspend fun updateUser(
        registerUserDto: RegisterUserDto,
        userId: String
    ) {
        val docRef = db.collection("users").document(userId)
        docRef.set(registerUserDto).await()
    }

    override suspend fun followOrUnfollowStudent(
        currentUserId: String,
        targetUserId: String
    ) {
        val currentUserRef = db.collection("users").document(currentUserId)
        val targetUserRef = db.collection("users").document(targetUserId)

        val followingRef = currentUserRef.collection("following").document(targetUserId)
        val followersRef = targetUserRef.collection("followers").document(currentUserId)

        db.runTransaction { transaction ->
            val followingDoc = transaction.get(followingRef)

            if (followingDoc.exists()){
                transaction.delete(followingRef)
                transaction.delete(followersRef)
                transaction.update(currentUserRef, "followingCount", FieldValue.increment(-1))
                transaction.update(targetUserRef, "followersCount", FieldValue.increment(-1))
            } else {
                transaction.set(followingRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.set(followersRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.update(currentUserRef, "followingCount", FieldValue.increment(1))
                transaction.update(targetUserRef, "followersCount", FieldValue.increment(1))
            }
        }.await()
    }

}
