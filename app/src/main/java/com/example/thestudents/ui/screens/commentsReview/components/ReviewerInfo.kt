package com.example.thestudents.ui.screens.commentsReview.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ProfileIcon

@Composable
fun ReviewerInfo(
    initialsReviewer: String,
    nameReviewer: String,
    usernameReviewer: String,
    timeAgo: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        ProfileIcon(
            initials = initialsReviewer,
            profileImage = null,
            backgroundColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(top = 5.dp, end = 10.dp)
                .size(32.dp)
        )
        Column {
            Text(
                text = nameReviewer,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(R.string.usuario_y_tiempo, usernameReviewer, timeAgo),
                fontSize = 13.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewerInfoPreview() {
    TheStudentsTheme {
        Surface {
            ReviewerInfo(
                initialsReviewer = "JP",
                nameReviewer = "Juan Perez",
                usernameReviewer = "@juanperez",
                timeAgo = "hace 1 hora"
            )
        }
    }
}