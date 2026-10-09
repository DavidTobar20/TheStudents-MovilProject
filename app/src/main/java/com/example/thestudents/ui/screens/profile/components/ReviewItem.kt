package com.example.thestudents.ui.screens.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.data.Review
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ProfileIcon

@Composable
fun ReviewItem(
    modifier: Modifier = Modifier,
    review: Review,
    isWrittenTab: Boolean = false,
    showActions: Boolean = isWrittenTab,
    onClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    val displayStudent = if (isWrittenTab) review.reviewedStudent else review.reviewer
    val displayName = if (isWrittenTab) "Para: ${review.reviewedStudent.name}" else review.reviewer.name

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            ProfileIcon(
                initials = displayStudent.initials,
                profileImage = displayStudent.profileImage,
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                fontSize = 14.sp,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = displayName,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(
                            R.string.resena_curso_y_fecha,
                            review.classReviewed,
                            review.periodReviewed,
                            review.time
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.contenido_citado, review.content),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.9f),
                    fontStyle = FontStyle.Italic
                )
            }
            if (showActions) {
                var menuExpanded by remember { mutableStateOf(false) }
                var showDeleteDialog by remember { mutableStateOf(false) }

                if (showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        title = { Text("Eliminar reseña") },
                        text = { Text("¿Estás seguro de que deseas eliminar esta reseña? Esta acción no se puede deshacer.") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showDeleteDialog = false
                                    onDeleteClick()
                                }
                            ) {
                                Text("Eliminar", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDeleteDialog = false }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }

                Box {
                    IconButton(
                        onClick = { menuExpanded = true },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "Opciones",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Editar") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = "Editar"
                                )
                            },
                            onClick = {
                                menuExpanded = false
                                onEditClick()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar", color = MaterialTheme.colorScheme.error) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Eliminar",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            },
                            onClick = {
                                menuExpanded = false
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewItemPreview() {
    TheStudentsTheme {
        Surface {
            ReviewItem(review = localReviewsProvider.allReviews[0])
        }
    }
}
