package com.example.thestudents.ui.screens.register.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun SocialRegisterOptions(
    onSsoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Alternative Divider
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = colorResource(id = R.color.tan))
            Text(
                text = " o regístrate con ",
                modifier = Modifier.padding(horizontal = 8.dp),
                style = TextStyle(fontSize = 12.sp, color = colorResource(id = R.color.dark_green).copy(alpha = 0.5f))
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = colorResource(id = R.color.tan))
        }

        // SSO Button
        OutlinedButton(
            onClick = onSsoClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, colorResource(id = R.color.tan)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(id = R.color.dark_green))
        ) {
            Icon(
                imageVector = Icons.Default.GridView,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = colorResource(id = R.color.medium_green)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Cuenta institucional (SSO)",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SocialRegisterOptionsPreview() {
    TheStudentsTheme {
        SocialRegisterOptions(onSsoClick = {})
    }
}
