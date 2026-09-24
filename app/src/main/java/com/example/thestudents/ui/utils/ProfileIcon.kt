package com.example.thestudents.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme

/**
 * A simple circular profile icon composable using Coil AsyncImage.
 *
 * @param modifier The [Modifier] to be applied to this layout.
 * @param profileImageUrl The remote URL string for the profile image, or null/empty.
 * @param size The size of the profile icon.
 */
@Composable
fun ProfileIcon(
    modifier: Modifier = Modifier,
    profileImageUrl: String? = null,
    size: Dp = 48.dp,
) {
    AsyncImage(
        model = profileImageUrl,
        contentDescription = stringResource(R.string.imagen_de_perfil),
        placeholder = painterResource(id = R.drawable.logosinfondo),
        error = painterResource(id = R.drawable.logosinfondo),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .clip(CircleShape),
    )
}

/**
 * A circular profile icon composable that displays either a remote image (via URL),
 * a local drawable image, or fallback initials with customizable styling.
 *
 * Priority order for rendering content:
 * 1. [profileImageUrl] if non-null and non-empty.
 * 2. [profileImage] drawable resource if non-null.
 * 3. [initials] text fallback.
 *
 * @param initials The text initials to display when no image is available.
 * @param profileImage The local drawable resource ID for the profile image, or null.
 * @param profileImageUrl The remote URL string for the profile image, or null/empty.
 * @param backgroundColor The background color of the circular container.
 * @param contentColor The text color for the initials fallback.
 * @param fontSize The font size for the initials text.
 * @param modifier The [Modifier] to be applied to this layout.
 */
@Composable
fun ProfileIcon(
    initials: String,
    profileImage: String?,
    profileImageUrl: String?,
    backgroundColor: Color,
    contentColor: Color,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
) {
    val imageUrl = profileImageUrl ?: profileImage
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        if (!imageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = stringResource(R.string.imagen_de_perfil),
                placeholder = painterResource(id = R.drawable.logosinfondo),
                error = painterResource(id = R.drawable.logosinfondo),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            Text(
                text = initials,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}

/**
 * Preview for [ProfileIcon] configured for review screens.
 */
@Preview(showBackground = true)
@Composable
fun ProfileIconPreviewReviewScreen() {
    TheStudentsTheme {
        Surface {
            ProfileIcon(
                initials = "JP",
                profileImage = null,
                profileImageUrl = null,
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                fontSize = 18.sp,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

/**
 * Preview for [ProfileIcon] configured with larger sizing for info/detail screens.
 */
@Preview(showBackground = true)
@Composable
fun ProfileIconPreviewInfo() {
    TheStudentsTheme {
        Surface {
            ProfileIcon(
                initials = "JP",
                profileImage = null,
                profileImageUrl = null,
                backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.primary,
                fontSize = 32.sp,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}
