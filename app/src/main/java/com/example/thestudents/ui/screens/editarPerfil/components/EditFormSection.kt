package com.example.thestudents.ui.screens.EditarPerfil.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.theme.TheStudentsTheme

@Composable
fun EditFormSection(
    name: String,
    onNameChange: (String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    program: String,
    semester: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            EditFieldItem(
                label = stringResource(R.string.nombre_completo_mayuscula),
                value = name,
                onValueChange = onNameChange,
                icon = Icons.Default.Person
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh
            )
            EditFieldItem(
                label = stringResource(R.string.nombre_de_usuario_mayuscula),
                value = username,
                onValueChange = onUsernameChange,
                icon = Icons.Default.AlternateEmail
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh
            )
            EditFieldItem(
                label = stringResource(R.string.carrera_programa_mayuscula),
                value = program,
                onValueChange = {},
                icon = Icons.Default.School,
                trailingIcon = Icons.Default.KeyboardArrowDown,
                readOnly = true
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh
            )
            EditFieldItem(
                label = stringResource(R.string.semestre_mayuscula),
                value = semester,
                onValueChange = {},
                icon = Icons.Default.CalendarMonth,
                trailingIcon = Icons.Default.KeyboardArrowDown,
                readOnly = true
            )
        }
    }
}

@Composable
private fun EditFieldItem(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    trailingIcon: ImageVector? = null,
    readOnly: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically // Centrado verticalmente de forma perpendicular
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.3.sp
            )
            
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                readOnly = readOnly,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                singleLine = true
            )
        }
        
        if (trailingIcon != null) {
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EditFormSectionPreview() {
    TheStudentsTheme {
        Surface {
            val student = localStudentProvider.currentUser
            Column(modifier = Modifier.padding(16.dp)) {
                EditFormSection(
                    name = student.name,
                    onNameChange = {},
                    username = student.username,
                    onUsernameChange = {},
                    program = student.program,
                    semester = "Semestre ${student.semester}"
                )
            }
        }
    }
}
