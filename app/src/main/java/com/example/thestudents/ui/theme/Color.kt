package com.example.thestudents.ui.theme

import androidx.compose.ui.graphics.Color

/*
 * Paletas tonales Material Design 3.
 *
 * Cada rampa se genera a partir de un color semilla (seed) de la marca manteniendo
 * fijos el tono (hue) y el croma, y variando unicamente la luminosidad. El numero
 * que acompana a cada nombre ES el tono MD3 (0 = negro, 100 = blanco), que equivale
 * al L* de CIELAB. Todos los pares texto/fondo derivados de estas rampas fueron
 * verificados contra WCAG (4.5:1 para texto, 3:1 para iconos y bordes).
 *
 * Estas constantes NO se usan directamente en la UI: solo alimentan los esquemas
 * de Theme.kt. En las pantallas siempre se usa MaterialTheme.colorScheme.*
 */

// PRIMARY - verde de marca (seed: dark_green #1B3935 | h=184.6 C=12.5)
val Green10 = Color(0xFF01201D)
val Green20 = Color(0xFF173531) // ~ #1B3935 original de la marca
val Green30 = Color(0xFF2E4C48)
val Green40 = Color(0xFF45645F)
val Green80 = Color(0xFFABCDC8)
val Green90 = Color(0xFFC7EAE4)
val Green100 = Color(0xFFFFFFFF)

// SECONDARY - verde medio (seed: medium_green #376052 | h=169.0 C=16.0)
val Pine10 = Color(0xFF002117)
val Pine20 = Color(0xFF14362B)
val Pine30 = Color(0xFF2B4D42)
val Pine40 = Color(0xFF426559) // ~ #376052 original de la marca
val Pine80 = Color(0xFFA8CFC0)
val Pine90 = Color(0xFFC4EBDC)
val Pine100 = Color(0xFFFFFFFF)

// TERTIARY - dorado de marca (seed: gold #D4AF37 | h=88.7 C=48.0)
val Gold10 = Color(0xFF231B00)
val Gold20 = Color(0xFF3C2F00)
val Gold30 = Color(0xFF574500)
val Gold40 = Color(0xFF735C00)
val Gold60 = Color(0xFFAA8E38) // tono minimo que alcanza 3:1 sobre superficie clara
val Gold80 = Color(0xFFE4C36B)
val Gold90 = Color(0xFFFFDF91)
val Gold100 = Color(0xFFFFFFFF)

// NEUTRAL (modo claro) - linaje crema (seed: cream #FBF7F2 | h=81.0 C=4.0)
val Cream10 = Color(0xFF1E1B16)
val Cream20 = Color(0xFF34302B)
val Cream87 = Color(0xFFDED9D2)
val Cream90 = Color(0xFFE7E2DB)
val Cream92 = Color(0xFFEDE7E1) // ~ #F0EAE1 (light_tan) original de la marca
val Cream94 = Color(0xFFF2EDE6)
val Cream95 = Color(0xFFF5F0E9)
val Cream96 = Color(0xFFF8F3EC)
val Cream98 = Color(0xFFFEF9F2) // ~ #FBF7F2 (cream) original de la marca
val Cream100 = Color(0xFFFFFFFF)

// NEUTRAL (modo oscuro) - verde profundo tenido (h=184.6 C=5.0)
val Forest4 = Color(0xFF01110F)
val Forest6 = Color(0xFF081614)
val Forest10 = Color(0xFF131D1C)
val Forest12 = Color(0xFF172220)
val Forest17 = Color(0xFF212C2A)
val Forest20 = Color(0xFF283231)
val Forest22 = Color(0xFF2C3735)
val Forest24 = Color(0xFF303B3A)
val Forest90 = Color(0xFFD8E5E3)
val Forest95 = Color(0xFFE6F4F1)

// NEUTRAL VARIANT - sage, comun a ambos temas (seed: sage #B2B7AC | h=127.2 C=9.0)
val Sage30 = Color(0xFF43493B)
val Sage50 = Color(0xFF73796B)
val Sage60 = Color(0xFF8C9384)
val Sage80 = Color(0xFFC2C9B9)
val Sage90 = Color(0xFFDEE5D5)

// ERROR - rojo Material 3 (h=36.4 C=68.2)
val Red10 = Color(0xFF390C00)
val Red20 = Color(0xFF690000)
val Red30 = Color(0xFF93000A)
val Red40 = Color(0xFFB4271F)
val Red80 = Color(0xFFFFB4A5)
val Red90 = Color(0xFFFFDAD2)
val Red100 = Color(0xFFFFFFFF)

// Negro puro reservado para scrim y sombras (roles que MD3 define como opacos).
val Scrim = Color(0xFF000000)

/*
 * Colores decorativos de avatar.
 *
 * Identifican visualmente a cada persona (como en Gmail o Slack), por lo que son
 * datos de la entidad y no cambian entre tema claro y oscuro. Todos comparten
 * tono 45 y croma 42, asi que se leen como una familia y garantizan 5.4:1 con
 * texto blanco encima y 3.4:1 sobre la superficie oscura.
 */
val Avatar1 = Color(0xFFAA4E4F)
val Avatar2 = Color(0xFF975C2C)
val Avatar3 = Color(0xFF677024)
val Avatar4 = Color(0xFF297946)
val Avatar5 = Color(0xFF007774)
val Avatar6 = Color(0xFF007493)
val Avatar7 = Color(0xFF5367AD)
val Avatar8 = Color(0xFF9C5085)

/** Paleta de avatares en orden estable, para elegir un color a partir de un id. */
val AvatarPalette = listOf(Avatar1, Avatar2, Avatar3, Avatar4, Avatar5, Avatar6, Avatar7, Avatar8)

/** Color de contenido (iniciales) sobre cualquier color de [AvatarPalette]. */
val OnAvatar = Color(0xFFFFFFFF)

/** Devuelve siempre el mismo color de [AvatarPalette] para una misma clave. */
fun avatarColorFor(key: String): Color =
    AvatarPalette[(key.hashCode().mod(AvatarPalette.size))]
