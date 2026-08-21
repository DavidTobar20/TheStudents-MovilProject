package com.example.thestudents.ui.utils

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun ProfileIcon(
    initials: String,
    profileImageRes: Int?,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    contentColor: Color,
    fontSize: TextUnit
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (profileImageRes != null) {
            Image(
                painter = painterResource(id = profileImageRes),
                contentDescription = "Imagen de perfil",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
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

@Preview(showBackground = true)
@Composable
fun ProfileIconPreviewReviewScreen() {
    TheStudentsTheme {
        ProfileIcon(
            initials = "JP",
            profileImageRes = null,
            backgroundColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            fontSize = 18.sp,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileIconPreviewInfo() {
    TheStudentsTheme {
        ProfileIcon(
            initials = "JP",
            profileImageRes = null,
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.primary,
            fontSize = 32.sp,
            modifier = Modifier.size(80.dp)
        )
    }
}
