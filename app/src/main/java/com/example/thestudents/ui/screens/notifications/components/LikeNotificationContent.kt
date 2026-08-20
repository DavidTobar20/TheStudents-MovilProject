package com.example.thestudents.ui.screens.notifications.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun LikeNotificationContent(
    userName: String,
    reviewOf: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(userName)
            }
            append(" ")
            append(stringResource(R.string.le_dio_me_gusta))
            append(" ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(reviewOf)
            }
            append(".")
        }, 
        style = MaterialTheme.typography.bodyMedium
    )
}

@Preview(showBackground = true)
@Composable
fun LikeNotificationContentPreview() {
    TheStudentsTheme {
        LikeNotificationContent(
            userName = "Daniel Ruiz",
            reviewOf = "Carlos Gómez"
        )
    }
}
