package com.example.thestudents.ui.screens.notifications.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.RatingBadge

@Composable
fun ReviewNotificationContent(
    userName: String,
    subject: String,
    comment: String,
    rating: Int,
    onViewDetailClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(userName)
                }
                append(" ")
                append(stringResource(R.string.calificado_en))
                append(" ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(subject)
                }
            }, style = MaterialTheme.typography.bodyMedium)

            if (comment.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\"$comment\"",
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            TextButton(onClick = onViewDetailClick, contentPadding = PaddingValues(0.dp)) {
                Text(
                    text = stringResource(R.string.ver_resena_completa),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Calificación en un círculo reutilizable
        if (rating > 0) {
            RatingBadge(rating = rating)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewNotificationContentPreview() {
    TheStudentsTheme {
        ReviewNotificationContent(
            userName = "María Jiménez",
            subject = "Estructuras de Datos",
            comment = "Excelente compañera...",
            rating = 5,
            onViewDetailClick = {}
        )
    }
}
