package com.example.thestudents.ui.screens.notifications.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme

// "Pepito te invitó a su equipo de Estructuras de Datos (2026-2)."
@Composable
fun TeamInviteNotificationContent(
    userName: String,
    subject: String,
    period: String,
    onViewProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(userName)
                }
                append(" ")
                append(stringResource(R.string.te_invito_a_su_equipo))
                append(" ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(subject)
                }
                if (period.isNotBlank()) append(" ($period)")
                append(".")
            },
            style = MaterialTheme.typography.bodyMedium
        )
        TextButton(onClick = onViewProfileClick, contentPadding = PaddingValues(0.dp)) {
            Text(
                text = stringResource(R.string.ver_perfil_companero),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeamInviteNotificationContentPreview() {
    TheStudentsTheme {
        TeamInviteNotificationContent(
            userName = "María Jiménez",
            subject = "Estructuras de Datos",
            period = "2026-2",
            onViewProfileClick = {}
        )
    }
}
