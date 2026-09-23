package com.example.thestudents.data.datasource

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageRemoteDataSource @Inject constructor(
    private val storage : FirebaseStorage
){
    //Función que me sirve para guardar cualquier tipo de imagen en firebaseStorage
    suspend fun uploadImage(path: String, uri: Uri): String{
        val imageRef =  storage.reference.child(path)
        imageRef.putFile(uri).await()
        return imageRef.downloadUrl.await().toString()
    }
}