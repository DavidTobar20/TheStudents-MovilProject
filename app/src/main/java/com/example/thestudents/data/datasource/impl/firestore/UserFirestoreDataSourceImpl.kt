package com.example.thestudents.data.datasource.impl.firestore

import com.example.thestudents.data.datasource.UserRemoteDataSource
import com.example.thestudents.data.dtos.RegisterUserDto
import com.example.thestudents.data.dtos.UserProfileResponseDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserFirestoreDataSourceImpl @Inject constructor(private val db: FirebaseFirestore) : UserRemoteDataSource {



    override suspend fun getUserProfile(studentId: String): UserProfileResponseDto {
        val snapshot = db.collection("users").document(studentId).get().await()
        if (!snapshot.exists()) {
            throw Exception("Usuario no encontrado en Firestore")
        }

        val userName = snapshot.getString("userName")
            ?: snapshot.getString("username")
            ?: snapshot.getString("nombre_usuario").orEmpty()
        val name = snapshot.getString("name").orEmpty()
        val apellido = snapshot.getString("apellido").orEmpty()
        val fullName = if (apellido.isNotBlank()) "$name $apellido".trim() else name
        val bio = snapshot.getString("bio") ?: snapshot.getString("biografia").orEmpty()
        val carrera = snapshot.getString("carrera") ?: snapshot.getString("program").orEmpty()
        val semestre = snapshot.getLong("semestre")?.toInt() ?: 1
        val fotoUrl = snapshot.getString("fotoUrl") ?: snapshot.getString("foto_url")
        val correo = snapshot.getString("correo") ?: snapshot.getString("email").orEmpty()

        val studentDto = com.example.thestudents.data.dtos.StudentDto(
            id = studentId.toIntOrNull(),
            uid = studentId,
            correo = correo,
            nombreUsuario = userName,
            nombre = fullName,
            biografia = bio,
            fotoUrl = fotoUrl,
            carrera = carrera,
            semestre = semestre
        )

        return UserProfileResponseDto(
            usuario = studentDto,
            resenasRecibidas = emptyList(),
            resenasCreadas = emptyList()
        )
    }

    override suspend fun registerUser(
        registerUserDto: RegisterUserDto,
        userId: String
    ) {
        val docRef = db.collection("users").document(userId) // se crea un documento
        docRef.set(registerUserDto).await() // insertando el documento
    }
}
