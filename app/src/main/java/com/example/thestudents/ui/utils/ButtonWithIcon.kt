package com.example.thestudents.ui.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R

@Composable
fun ButtonWithIcon(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color,
    contentColor: Color
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = contentColor
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonWithIconPreviewSSO() {
    ButtonWithIcon(
        text = "Cuenta institucional (SSO)",
        icon = Icons.Default.Home,
        onClick = {},
        borderColor = colorResource(R.color.medium_green),
        contentColor = colorResource(R.color.dark_green),
        modifier = Modifier.height(56.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun ButtonWithIconPreviewEditProfile() {
    ButtonWithIcon(
        text = "Editar perfil",
        icon = Icons.Outlined.Edit,
        onClick = {},
        borderColor = colorResource(R.color.dark_green),
        contentColor = colorResource(R.color.dark_green),
        modifier = Modifier.height(48.dp)
            .padding(horizontal = 24.dp)
    )
}
