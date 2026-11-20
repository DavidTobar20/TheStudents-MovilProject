package com.example.thestudents.ui.screens.teamMatch.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.components.SectionLabel
import com.example.thestudents.ui.components.luxuryCard
import com.example.thestudents.ui.theme.TheStudentsTheme

/**
 * Paso 1 de "Arma tu equipo": elegir la materia que va a ver el próximo semestre.
 * También muestra las materias en las que ya está buscando equipo.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CourseSelectionSection(
    periodo: String,
    availableCourses: List<String>,
    mySearches: List<String>,
    selectedCourse: String,
    isStartingSearch: Boolean,
    errorMessage: String?,
    onCourseSelected: (String) -> Unit,
    onSearchClick: () -> Unit,
    onOpenSearch: (String) -> Unit,
    onStopSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .luxuryCard()
                .padding(20.dp)
        ) {
            SectionLabel(text = stringResource(R.string.semestre_x, periodo))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.elige_materia_proxima),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.elige_materia_proxima_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (availableCourses.isEmpty()) {
                Text(
                    text = stringResource(R.string.sin_materias_disponibles),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                // Mismo selector que el diálogo de "Inscribir materia"
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !isStartingSearch) { expanded = true }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedCourse.ifEmpty { stringResource(R.string.seleccionar_materia) },
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
                        }
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.heightIn(max = 320.dp)
                    ) {
                        availableCourses.forEach { materia ->
                            DropdownMenuItem(
                                text = { Text(text = materia) },
                                onClick = {
                                    onCourseSelected(materia)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onSearchClick,
                    enabled = selectedCourse.isNotBlank() && !isStartingSearch,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    if (isStartingSearch) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Icon(Icons.Filled.Groups, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.buscar_equipo),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.buscar_equipo_aviso),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = errorMessage, fontSize = 13.sp, color = MaterialTheme.colorScheme.error)
            }
        }

        // Materias en las que ya está buscando equipo: tocar = ver recomendaciones, X = dejar de buscar
        if (mySearches.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            SectionLabel(
                text = stringResource(R.string.mis_busquedas),
                modifier = Modifier.padding(start = 4.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                mySearches.forEach { materia ->
                    InputChip(
                        selected = false,
                        onClick = { onOpenSearch(materia) },
                        label = { Text(text = materia) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(R.string.dejar_de_buscar),
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { onStopSearch(materia) }
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CourseSelectionSectionPreview() {
    TheStudentsTheme {
        Surface {
            CourseSelectionSection(
                periodo = "2027-1",
                availableCourses = listOf("Bases de Datos", "Ingeniería de Software"),
                mySearches = listOf("Bases de Datos"),
                selectedCourse = "Ingeniería de Software",
                isStartingSearch = false,
                errorMessage = null,
                onCourseSelected = {},
                onSearchClick = {},
                onOpenSearch = {},
                onStopSearch = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
