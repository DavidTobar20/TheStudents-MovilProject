package com.example.thestudents.ui.screens.notifications.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun FollowRequestContent(
    userName: String,
    onAcceptClick: () -> Unit,
    onRejectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(userName)
            }
            append(" ")
            append(stringResource(R.string.solicitud_seguimiento))
            append(".")
        }, style = MaterialTheme.typography.bodyMedium)
        
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Button(
                onClick = onAcceptClick,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.dark_green)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(stringResource(R.string.aceptar))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onRejectClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEFEBE4)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(stringResource(R.string.rechazar), color = Color.Black)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FollowRequestContentPreview() {
    TheStudentsTheme {
        FollowRequestContent(
            userName = "Laura Martínez",
            onAcceptClick = {},
            onRejectClick = {}
        )
    }
}
