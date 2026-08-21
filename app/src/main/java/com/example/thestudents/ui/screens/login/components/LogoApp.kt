package com.example.thestudents.ui.screens.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme


@Composable
fun LogoApp(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.logosinfondo),
        contentDescription = stringResource(R.string.logo_the_students),
        modifier = modifier.size(120.dp),
        contentScale = ContentScale.Fit
    )
}

@Preview(showBackground = true)
@Composable
fun LogoAppPreview() {
    TheStudentsTheme {
        Surface {
            LogoApp()
        }
    }
}
