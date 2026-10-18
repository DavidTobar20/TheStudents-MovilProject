package com.example.thestudents.data.datasource.impl.firestore

import com.example.thestudents.data.datasource.UserRemoteDataSource
import com.example.thestudents.data.dtos.RegisterUserDto
import com.example.thestudents.data.dtos.StudentDto
import com.example.thestudents.data.dtos.UserProfileDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserFirestoreDataSourceImpl @Inject constructor(private val db: FirebaseFirestore) : UserRemoteDataSource {
    override suspend fun getUserProfile(studentId: String): UserProfileDto {
        TODO("Not yet implemented")
    }

    /*override suspend fun getUserProfile(studentId: String): UserProfileResponseDto {
        TODO()

    }*/

    override suspend fun getStudentById(studentId: String): StudentDto {
        TODO("Not yet implemented")
    }

    override suspend fun registerUser(
        registerUserDto: RegisterUserDto,
        userId: String
    ) {
        val docRef = db.collection("users").document(userId) // se crea un documento
        docRef.set(registerUserDto).await() // insertando el documento
    }

}