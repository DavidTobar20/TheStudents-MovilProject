package com.example.thestudents.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/*
 * Esquema CLARO.
 *
 * Superficies del linaje crema (Cream), acentos verdes de marca (Green y Pine)
 * y dorado como terciario (Gold). Los bordes y textos secundarios salen de la
 * rampa neutral variant sage (Sage).
 */
private val LightColorScheme = lightColorScheme(
    primary = Green20,
    onPrimary = Green100,
    primaryContainer = Green90,
    onPrimaryContainer = Green10,
    inversePrimary = Green80,

    secondary = Pine40,
    onSecondary = Pine100,
    secondaryContainer = Pine90,
    onSecondaryContainer = Pine10,

    tertiary = Gold40,
    onTertiary = Gold100,
    tertiaryContainer = Gold90,
    onTertiaryContainer = Gold10,

    error = Red40,
    onError = Red100,
    errorContainer = Red90,
    onErrorContainer = Red10,

    background = Cream98,
    onBackground = Cream10,
    surface = Cream98,
    onSurface = Cream10,
    surfaceVariant = Sage90,
    onSurfaceVariant = Sage30,
    surfaceTint = Green20,

    inverseSurface = Cream20,
    inverseOnSurface = Cream95,

    outline = Sage50,
    outlineVariant = Sage80,
    scrim = Scrim,

    surfaceBright = Cream98,
    surfaceDim = Cream87,
    surfaceContainerLowest = Cream100,
    surfaceContainerLow = Cream96,
    surfaceContainer = Cream94,
    surfaceContainerHigh = Cream92,
    surfaceContainerHighest = Cream90
)

/*
 * Esquema OSCURO.
 *
 * Superficies verde profundo (Forest*) para conservar el caracter de la marca en
 * oscuro, con los acentos invertidos en la escala tonal: lo que en claro era un
 * tono 20/40 pasa a 80, y los contenedores de 90 pasan a 30.
 */
private val DarkColorScheme = darkColorScheme(
    primary = Green80,
    onPrimary = Green20,
    primaryContainer = Green30,
    onPrimaryContainer = Green90,
    inversePrimary = Green40,

    secondary = Pine80,
    onSecondary = Pine20,
    secondaryContainer = Pine30,
    onSecondaryContainer = Pine90,

    tertiary = Gold80,
    onTertiary = Gold20,
    tertiaryContainer = Gold30,
    onTertiaryContainer = Gold90,

    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,

    background = Forest6,
    onBackground = Forest90,
    surface = Forest6,
    onSurface = Forest90,
    surfaceVariant = Sage30,
    onSurfaceVariant = Sage80,
    surfaceTint = Green80,

    inverseSurface = Forest90,
    inverseOnSurface = Forest20,

    outline = Sage60,
    outlineVariant = Sage30,
    scrim = Scrim,

    surfaceBright = Forest24,
    surfaceDim = Forest6,
    surfaceContainerLowest = Forest4,
    surfaceContainerLow = Forest10,
    surfaceContainer = Forest12,
    surfaceContainerHigh = Forest17,
    surfaceContainerHighest = Forest22
)

/**
 * Colores de marca que no tienen un rol equivalente en MD3.
 *
 * El dorado vive como `tertiary` dentro del esquema, pero en modo claro `tertiary`
 * es un tono 40 (oliva oscuro) pensado para texto. Para las estrellas de valoracion
 * hace falta un dorado que siga leyendose como dorado y que aun asi alcance 3:1
 * sobre la superficie, de ahi este token aparte.
 */
@Immutable
data class ExtendedColors(
    val rating: Color,
    val ratingInactive: Color
)

private val LightExtendedColors = ExtendedColors(
    rating = Gold60,
    ratingInactive = Sage80
)

private val DarkExtendedColors = ExtendedColors(
    rating = Gold80,
    ratingInactive = Sage30
)

private val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

/** Acceso a los colores extendidos: `MaterialTheme.extended.rating`. */
val MaterialTheme.extended: ExtendedColors
    @Composable
    @ReadOnlyComposable
    get() = LocalExtendedColors.current

/**
 * Tema de la aplicacion.
 *
 * No se usa Dynamic Color (Material You) a proposito: la identidad de The Students
 * depende del verde y el crema de la marca, y los tonos dinamicos del fondo de
 * pantalla del dispositivo los reemplazarian por completo.
 */
@Composable
fun TheStudentsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
