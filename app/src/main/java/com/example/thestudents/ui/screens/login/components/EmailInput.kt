package com.example.thestudents.ui.screens.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R

@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = "Correo institucional",
            color = colorResource(R.color.dark_green),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = { Text("nombre@universidad.edu.co", color = colorResource(R.color.sage)) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = colorResource(R.color.dark_green)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.sage),
                unfocusedBorderColor = colorResource(R.color.sage).copy(alpha = 0.5f),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmailInputPreview() {
    var email by remember { mutableStateOf("") }
    EmailInput(
        email = email,
        onEmailChange = { email = it }
    )
}
