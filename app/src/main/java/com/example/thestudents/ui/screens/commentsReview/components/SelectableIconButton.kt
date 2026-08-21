package com.example.thestudents.ui.screens.commentsReview.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun SelectableIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    count: Int,
    isSelected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = if (isSelected) selectedColor else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = if (isSelected) selectedColor else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = count.toString(),
                fontSize = 13.sp,
                color = if (isSelected) selectedColor else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview (showBackground = true)
@Composable
fun SelectableIconButtonPreviewLike() {
    TheStudentsTheme {
        Surface {
            var isSelected by remember { mutableStateOf(false) }
            var count by remember { mutableStateOf(10) }
            SelectableIconButton(
                icon = Icons.Filled.ThumbUp,
                count = 10,
                isSelected = isSelected,
                selectedColor = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    isSelected = !isSelected
                    count += if (isSelected) 1 else -1
                },
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@Preview (showBackground = true)
@Composable
fun SelectableIconButtonPreviewDisLike() {
    TheStudentsTheme {
        Surface {
            var isSelected by remember { mutableStateOf(false) }
            var count by remember { mutableStateOf(10) }
            SelectableIconButton(
                icon = Icons.Filled.ThumbDown,
                count = count,
                isSelected = isSelected,
                selectedColor = MaterialTheme.colorScheme.error,
                onClick = {
                    isSelected = !isSelected
                    count += if (isSelected) 1 else -1
                },
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}