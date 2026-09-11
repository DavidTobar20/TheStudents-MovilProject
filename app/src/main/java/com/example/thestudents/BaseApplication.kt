package com.example.thestudents

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Punto de entrada de Hilt: genera el contenedor de dependencias de toda la app.
 * Declarada en el manifest con android:name; sin ella Hilt falla al arrancar.
 */
@HiltAndroidApp
class BaseApplication : Application()
