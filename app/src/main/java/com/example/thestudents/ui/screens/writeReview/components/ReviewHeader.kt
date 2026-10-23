package com.example.thestudents.ui.screens.writeReview.components

import com.example.thestudents.ui.components.luxuryCard
import com.example.thestudents.ui.components.SectionLabel
import com.example.thestudents.ui.components.GoldRingAvatar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ProfileIcon

// Tarjeta "RESEÑA PARA" con el estudiante que se va a reseñar
@Composable
fun ReviewHeader(
    modifier: Modifier = Modifier,
    name: String,
    initials: String,
    profileImage: String? = null,
    courseInfo: String,
    onAvatarClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .luxuryCard()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GoldRingAvatar(
            initials = initials,
            profileImage = profileImage,
            backgroundColor = MaterialTheme.colorScheme.primary,
            size = 52.dp,
            fontSize = 18.sp,
            modifier = if (onAvatarClick != null) {
                Modifier
                    .clip(CircleShape)
                    .clickable(onClick = onAvatarClick)
            } else Modifier
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            SectionLabel(text = "Reseña para")
            Text(
                text = name,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = courseInfo,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewHeaderPreview() {
    TheStudentsTheme {
        ReviewHeader(
            name = "Juan Pérez",
            initials = "JP",
            profileImage = null,
            courseInfo = "Física Mecánica (FISI1027) · 2025-2",
        )
    }
}
