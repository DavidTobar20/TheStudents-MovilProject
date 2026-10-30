package com.example.thestudents.ui.screens.reviews.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.thestudents.R

@Composable
fun EnrollSubjectDialog(
    catalogoMaterias: Map<String, List<String>>,
    materiasYaInscritas: Set<String>,
    selectedMateria: String,
    selectedPeriodo: String,
    isEnrolling: Boolean,
    errorMessage: String?,
    onMateriaSelected: (String) -> Unit,
    onPeriodoSelected: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    var expandedMateria by remember { mutableStateOf(false) }
    var expandedPeriodo by remember { mutableStateOf(false) }
    val periodosDisponibles = catalogoMaterias[selectedMateria].orEmpty()

    AlertDialog(
        onDismissRequest = { if (!isEnrolling) onDismiss() },
        title = {
            Text(
                text = stringResource(R.string.inscribir_materia),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.selecciona_materia_y_periodo),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Lista 1: Materia
                Text(
                    text = stringResource(R.string.materia),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !isEnrolling) { expandedMateria = true }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedMateria.ifEmpty { stringResource(R.string.seleccionar_materia) },
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = expandedMateria,
                        onDismissRequest = { expandedMateria = false }
                    ) {
                        catalogoMaterias.keys.forEach { materia ->
                            val yaCursada = materiasYaInscritas.any { it.equals(materia, ignoreCase = true) }
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = if (yaCursada) "$materia (Ya cursada)" else materia,
                                        color = if (yaCursada) {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        } else {
                                            MaterialTheme.colorScheme.onSurface
                                        }
                                    )
                                },
                                onClick = {
                                    onMateriaSelected(materia)
                                    expandedMateria = false
                                }
                            )
                        }
                    }
                }

                // Lista 2: Periodo de esa materia
                Text(
                    text = stringResource(R.string.periodo_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !isEnrolling && periodosDisponibles.isNotEmpty()) {
                                expandedPeriodo = true
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedPeriodo.ifEmpty { stringResource(R.string.seleccionar_periodo) },
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = expandedPeriodo,
                        onDismissRequest = { expandedPeriodo = false }
                    ) {
                        periodosDisponibles.forEach { periodo ->
                            DropdownMenuItem(
                                text = { Text(text = periodo) },
                                onClick = {
                                    onPeriodoSelected(periodo)
                                    expandedPeriodo = false
                                }
                            )
                        }
                    }
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isEnrolling && selectedMateria.isNotBlank() && selectedPeriodo.isNotBlank()
            ) {
                if (isEnrolling) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(18.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = stringResource(R.string.inscribir))
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isEnrolling
            ) {
                Text(text = stringResource(R.string.cancelar))
            }
        }
    )
}
