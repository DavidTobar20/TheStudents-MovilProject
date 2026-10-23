package com.example.thestudents.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.ui.theme.OnAvatar
import com.example.thestudents.ui.theme.extended
import com.example.thestudents.ui.utils.ProfileIcon

/*
 * Piezas comunes del estilo de la tarjeta de reseña, para usarlo igual en todas las pantallas.
 */

// Tarjeta: sombra suave en capas, borde fino del tema, esquinas redondeadas y fondo blanco/oscuro
@Composable
fun Modifier.luxuryCard(shape: Shape = RoundedCornerShape(20.dp)): Modifier = this
    .shadow(
        elevation = 10.dp,
        shape = shape,
        ambientColor = Color.Black.copy(alpha = 0.08f),
        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
    )
    .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), shape)
    .clip(shape)
    .background(MaterialTheme.colorScheme.surfaceContainerLowest)

// Avatar con aro dorado fino
@Composable
fun GoldRingAvatar(
    initials: String,
    profileImage: String?,
    backgroundColor: Color,
    size: Dp,
    fontSize: TextUnit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .border(if (size >= 64.dp) 2.dp else 1.5.dp, MaterialTheme.extended.rating, CircleShape)
            .padding(if (size >= 64.dp) 4.dp else 2.5.dp)
            .clip(CircleShape)
    ) {
        ProfileIcon(
            initials = initials,
            profileImage = profileImage,
            backgroundColor = backgroundColor,
            contentColor = OnAvatar,
            fontSize = fontSize,
            modifier = Modifier.fillMaxSize()
        )
    }
}

// Etiqueta de seccion en letras doradas pequeñas y espaciadas ("RESEÑA PARA", "COMENTARIOS"...)
@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        modifier = modifier,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.4.sp,
        color = MaterialTheme.colorScheme.tertiary
    )
}
