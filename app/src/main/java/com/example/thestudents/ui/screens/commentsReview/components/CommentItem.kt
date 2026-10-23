package com.example.thestudents.ui.screens.commentsReview.components

import com.example.thestudents.ui.components.ReactionCapsule
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.data.Comment
import com.example.thestudents.data.local.localCommentsProvider
import com.example.thestudents.ui.components.SelectableIconButton
import com.example.thestudents.ui.theme.OnAvatar
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.theme.extended
import com.example.thestudents.ui.utils.ProfileIcon

@Composable
fun CommentItem(
    comment: Comment,
    isLiked: Boolean,
    isDisliked: Boolean,
    onLikeClick: () -> Unit,
    onDislikeClick: () -> Unit,
    modifier: Modifier = Modifier,
    onCommentatorClick: (() -> Unit)? = null,
    showActions: Boolean = false,          // solo el autor del comentario ve "Editar" y "Eliminar"
    onEditClick: (String) -> Unit = {},    // recibe el texto nuevo
    onDeleteClick: () -> Unit = {}
) {
    val commentatorModifier =
        if (onCommentatorClick != null) Modifier.clickable(onClick = onCommentatorClick) else Modifier
    val shape = RoundedCornerShape(20.dp)

    // Tarjeta del comentario con el mismo estilo que la de la reseña
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.08f),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.30f)
            )
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), shape)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(start = 16.dp, end = 8.dp, top = 14.dp, bottom = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Avatar con aro dorado fino
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(1.5.dp, MaterialTheme.extended.rating, CircleShape)
                    .padding(2.5.dp)
                    .clip(CircleShape)
                    .then(commentatorModifier)
            ) {
                ProfileIcon(
                    initials = comment.commentator.initials,
                    profileImage = comment.commentator.profileImage,
                    backgroundColor = comment.commentator.profileColor,
                    contentColor = OnAvatar,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .then(commentatorModifier),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = comment.commentator.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1
                )
                Text(
                    text = comment.createdAt,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
            // Menu "..." igual al de las reseñas (ReviewItem)
            if (showActions) {
                CommentActionsMenu(
                    content = comment.content,
                    onEditClick = onEditClick,
                    onDeleteClick = onDeleteClick
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = comment.content,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(end = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))
        ReactionCapsule(
            likes = comment.likes,
            dislikes = comment.disLikes,
            isLiked = isLiked,
            isDisliked = isDisliked,
            onLikeClick = onLikeClick,
            onDislikeClick = onDislikeClick,
            height = 40.dp,
            iconSize = 16.dp,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun CommentActionsMenu(
    content: String,
    onEditClick: (String) -> Unit,
    onDeleteClick: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar comentario") },
            text = { Text("¿Estás seguro de que deseas eliminar este comentario? Esta acción no se puede deshacer.") },
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

    if (showEditDialog) {
        var editedText by remember { mutableStateOf(content) }
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Editar comentario") },
            text = {
                OutlinedTextField(
                    value = editedText,
                    onValueChange = { editedText = it },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    enabled = editedText.isNotBlank(),
                    onClick = {
                        showEditDialog = false
                        onEditClick(editedText)
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Box {
        IconButton(
            onClick = { menuExpanded = true },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = "Opciones",
                modifier = Modifier.size(22.dp),
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
                    showEditDialog = true
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

@Preview(showBackground = true)
@Composable
fun CommentItemPreview() {
    TheStudentsTheme {
        Surface {
            var isLiked by remember { mutableStateOf(false) }
            var isDisliked by remember { mutableStateOf(false) }
            CommentItem(
                comment = localCommentsProvider.allComments[1],
                isLiked = isLiked,
                isDisliked = isDisliked,
                onLikeClick = {isLiked=!isLiked},
                onDislikeClick = {isDisliked=!isDisliked}
            )
        }
    }
}