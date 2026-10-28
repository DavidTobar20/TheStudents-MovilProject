package com.example.thestudents

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.HiltAndroidApp

/**
 * Punto de entrada de Hilt: genera el contenedor de dependencias de toda la app.
 * Declarada en el manifest con android:name; sin ella Hilt falla al arrancar.
 */
@HiltAndroidApp
class BaseApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        if (BuildConfig.DEBUG) {
            try {
                // 10.0.2.2 es la IP del host desde el emulador de Android.
                Firebase.firestore.useEmulator("10.0.2.2", 8080)
                Firebase.auth.useEmulator("10.0.2.2", 9099)
            } catch (_: Exception) {
                // Evita cierres si los emuladores ya fueron configurados
            }
        }
    }
}
