package com.example.thestudents.data.injection

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent:: class) //los objetos los cree como singleton

class FirebaseHiltModule {
    @Provides
    fun auth(): FirebaseAuth = Firebase.auth  // crea una unica instancia
}