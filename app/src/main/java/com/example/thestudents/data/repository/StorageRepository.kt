package com.example.thestudents.data.repository

import android.net.Uri
import com.example.thestudents.data.datasource.AuthRemoteDataSource
import com.example.thestudents.data.datasource.StorageRemoteDataSource
import com.example.thestudents.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import javax.inject.Inject

class StorageRepository @Inject constructor(
    private val storage: StorageRemoteDataSource,
    private val auth: AuthRemoteDataSource,
    private val userRemoteDataSource: UserFirestoreDataSourceImpl
) {

    suspend fun uploadProfileImage(uri: Uri): Result<String> {
        return try {
            val userId =
                auth.currentUser?.uid ?: return Result.failure(Exception("No user logged in"))
            val path = "profileImages/$userId.jpg"
            val url = storage.uploadImage(path, uri)

            // Actualizar url del usuario en Firebase Auth y en Firestore
            auth.updateProfileImage(url)
            userRemoteDataSource.updateUserPhoto(userId, url)
            Result.success(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}