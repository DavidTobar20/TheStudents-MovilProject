package com.example.thestudents.data.datasource.impl.firestore

import com.example.thestudents.data.datasource.UserRemoteDataSource
import com.example.thestudents.data.dtos.RegisterUserDto
import com.example.thestudents.data.dtos.UserDtoGeneric
import com.example.thestudents.data.dtos.UserProfileFirestoreDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserFirestoreDataSourceImpl @Inject constructor(private val db: FirebaseFirestore) : UserRemoteDataSource {

    override suspend fun getUserProfile(studentId: String): UserProfileFirestoreDto {
        val docRef = db.collection("users").document(studentId)
        val snapshot = docRef.get().await()
        if (!snapshot.exists()) {
            throw Exception("Usuario no encontrado en Firestore")
        }
        val userDto = snapshot.toObject(UserProfileFirestoreDto::class.java)
            ?: throw Exception("No se pudo deserializar el usuario de Firestore")
        return userDto.copy(id = snapshot.id)
    }

    override suspend fun registerUser(
        registerUserDto: RegisterUserDto,
        userId: String
    ) {
        val docRef = db.collection("users").document(userId) // se crea un documento
        docRef.set(registerUserDto).await() // insertando el documento
    }
}
