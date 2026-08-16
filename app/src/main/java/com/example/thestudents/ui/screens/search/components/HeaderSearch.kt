package com.example.thestudents.ui.screens.search.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.DiamondDivider

@Composable
fun HeaderSearch(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.buscar_estudiantes),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = colorResource(R.color.dark_green),
            letterSpacing = 1.sp
        )
        DiamondDivider(modifier = Modifier
            .padding(vertical = 16.dp)
            .width(200.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderSearchPreview() {
    TheStudentsTheme { HeaderSearch(modifier = Modifier.padding(16.dp)) }
}
