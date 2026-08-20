package com.example.thestudents.ui.screens.EditarPerfil.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

@Composable
fun HeaderEditarPerfil(
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(top = 4.dp, bottom = 8.dp),
        contentAlignment = Alignment.Center
    ) {

        IconButton(
            onClick = onCancelClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(R.string.cancelar),
                tint = colorResource(R.color.dark_green),
                modifier = Modifier.size(28.dp)
            )
        }


        Text(
            text = stringResource(R.string.editar_perfil),
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.dark_green),
            fontFamily = FontFamily.Serif
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderEditarPerfilPreview() {
    HeaderEditarPerfil(onCancelClick = {})
}
