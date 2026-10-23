package com.example.thestudents.ui.screens.search.components

import com.example.thestudents.ui.theme.extended
import androidx.compose.foundation.layout.height
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun SearchBar(
    query: String,
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            // Sombra suave, como las tarjetas
            .shadow(
                elevation = 8.dp,
                shape = CircleShape,
                ambientColor = Color.Black.copy(alpha = 0.06f),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
            ),
        placeholder = {
            Text(
                text = stringResource(R.string.buscar_por_nombre_o_carrera),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        shape = CircleShape,
        colors = OutlinedTextFieldDefaults.colors(
            // Borde dorado al escribir; borde fino del tema en reposo
            focusedBorderColor = MaterialTheme.extended.rating,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        ),
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    TheStudentsTheme {
        Surface {
            SearchBar(query = "", onQueryChange = {})
        }
    }
}
