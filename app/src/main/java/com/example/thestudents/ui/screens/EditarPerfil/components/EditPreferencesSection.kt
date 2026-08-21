package com.example.thestudents.ui.screens.EditarPerfil.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R

@Composable
fun EditPreferencesSection(
    showReviews: Boolean,
    onShowReviewsChange: (Boolean) -> Unit,
    notificationsEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.preferencias_mayuscula),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.sage),
                letterSpacing = 0.5.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            PreferenceRow(
                title = stringResource(R.string.mostrar_resenas_perfil),
                description = stringResource(R.string.descripcion_mostrar_resenas),
                checked = showReviews,
                onCheckedChange = onShowReviewsChange
            )
            
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = colorResource(R.color.sage).copy(alpha = 0.1f)
            )
            
            PreferenceRow(
                title = stringResource(R.string.notificaciones_resenas),
                description = stringResource(R.string.descripcion_notificaciones),
                checked = notificationsEnabled,
                onCheckedChange = onNotificationsChange
            )
        }
    }
}

@Composable
private fun PreferenceRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.dark_green)
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = colorResource(R.color.sage)
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = colorResource(R.color.dark_green),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = colorResource(R.color.sage).copy(alpha = 0.5f),
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditPreferencesSectionPreview() {
    EditPreferencesSection(
        showReviews = true,
        onShowReviewsChange = {},
        notificationsEnabled = true,
        onNotificationsChange = {}
    )
}
