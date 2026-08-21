package com.example.thestudents.ui.screens.reviews.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.utils.DiamondDivider
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun HeaderReviews(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.resenar_companeros),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.dark_green),
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.personas_con_quienes_has_compartido_clase),
            fontSize = 14.sp,
            color = colorResource(R.color.medium_green).copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 4.dp),
            textAlign = TextAlign.Center
        )
        DiamondDivider(modifier = Modifier.padding(horizontal = 48.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderReviewsPreview() {
    HeaderReviews()
}
