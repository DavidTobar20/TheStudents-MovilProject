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

// "Pepito comenzó a seguirte." (igual a LikeNotificationContent, pero para los follows)
@Composable
fun FollowNotificationContent(
    userName: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(userName)
            }
            append(" ")
            append(stringResource(R.string.comenzo_a_seguirte))
            append(".")
        },
        style = MaterialTheme.typography.bodyMedium
    )
}

@Preview(showBackground = true)
@Composable
fun FollowNotificationContentPreview() {
    TheStudentsTheme {
        FollowNotificationContent(
            userName = "Daniel Ruiz"
        )
    }
}
