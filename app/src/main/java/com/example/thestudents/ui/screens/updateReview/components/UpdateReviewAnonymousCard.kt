package com.example.thestudents.ui.screens.updateReview.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.SettingSwitchRow

@Composable
fun UpdateReviewAnonymousCard(
    isAnonymous: Boolean,
    onAnonymousChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        SettingSwitchRow(
            modifier = Modifier.padding(16.dp),
            title = stringResource(R.string.publicar_de_forma_anonima),
            description = stringResource(R.string.tu_nombre_no_aparecer_en_la_resena),
            checked = isAnonymous,
            onCheckedChange = onAnonymousChange
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateReviewAnonymousCardPreview() {
    TheStudentsTheme {
        UpdateReviewAnonymousCard(
            isAnonymous = false,
            onAnonymousChange = {}
        )
    }
}
