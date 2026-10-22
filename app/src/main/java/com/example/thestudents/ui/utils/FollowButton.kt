package com.example.thestudents.ui.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun FollowButton(
    isFollowing: Boolean,
    onFollowToggle: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 11.sp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
) {
    if (isFollowing) {
        OutlinedButton(
            onClick = onFollowToggle,
            modifier = modifier,
            shape = RoundedCornerShape(12.dp),
            contentPadding = contentPadding,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = stringResource(R.string.siguiendo_mayuscula),
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        Button(
            onClick = onFollowToggle,
            modifier = modifier,
            shape = RoundedCornerShape(12.dp),
            contentPadding = contentPadding,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = stringResource(R.string.seguir_mayuscula),
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FollowButtonPreview() {
    TheStudentsTheme {
        FollowButton(
            isFollowing = false,
            onFollowToggle = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FollowButtonFollowingPreview() {
    TheStudentsTheme {
        FollowButton(
            isFollowing = true,
            onFollowToggle = {}
        )
    }
}
