package com.example.thestudents.ui.screens.notifications.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.theme.extended

@Composable
fun CommentNotificationContent(
    userName: String,
    subject: String,
    courseCode: String,
    snippet: String,
    onViewDetailClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = userName,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(R.string.materia_con_codigo, subject, courseCode),
            color = MaterialTheme.extended.rating,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = stringResource(R.string.nuevo_comentario_detalle, subject, snippet),
            style = MaterialTheme.typography.bodyMedium
        )
        TextButton(onClick = onViewDetailClick, contentPadding = PaddingValues(0.dp)) {
            Text(
                text = stringResource(R.string.ver_comentario),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommentNotificationContentPreview() {
    TheStudentsTheme {
        CommentNotificationContent(
            userName = "Carlos Gómez",
            subject = "Cálculo I",
            courseCode = "MATE1103",
            snippet = "Excelente disposición...",
            onViewDetailClick = {}
        )
    }
}
