package com.example.thestudents.ui


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme

// Colors
val DarkGreen = Color(0xFF1B3935)
val MediumGreen = Color(0xFF376052)
val Sage = Color(0xFFB2B7AC)
val Cream = Color(0xFFFBF7F2)
val Tan = Color(0xFFD3C3A7)

@Composable
fun MensajeBienvenida(modifier: Modifier = Modifier) {
    Text(
        text = "THE STUDENTS",
        modifier = modifier,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Serif,
        color = DarkGreen,
        letterSpacing = 4.sp,
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
fun MensajeBienvenidaPreview() {
    TheStudentsTheme {
        MensajeBienvenida()
    }
}

@Composable
fun LogoApp(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.logosinfondo),
        contentDescription = "Logo The Students",
        modifier = modifier.size(120.dp),
        contentScale = ContentScale.Fit
    )
}

@Preview(showBackground = true)
@Composable
fun LogoAppPreview() {
    LogoApp()
}

@Composable
fun DiamondDivider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = Tan, thickness = 1.dp)
        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(10.dp)
                .rotate(45f)
                .background(Tan)
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = Tan, thickness = 1.dp)
    }
}

@Composable
fun AppButton(textoBoton: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = textoBoton,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
@Preview(showBackground = true)
fun AppButtonPreview() {
    AppButton("INGRESAR") {}
}

@Composable
fun BodyHomeScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .padding(horizontal = 32.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LogoApp()
        
        Spacer(modifier = Modifier.height(8.dp))
        
        MensajeBienvenida()
        
        DiamondDivider(modifier = Modifier.padding(horizontal = 48.dp))
        
        Text(
            text = "Inicia sesión para continuar",
            color = MediumGreen.copy(alpha = 0.7f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Input Fields
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Correo institucional",
                color = DarkGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("nombre@universidad.edu.co", color = Sage) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = DarkGreen) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Sage,
                    unfocusedBorderColor = Sage.copy(alpha = 0.5f),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Contraseña",
                color = DarkGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Ingresa tu contraseña", color = Sage) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = DarkGreen) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Sage,
                    unfocusedBorderColor = Sage.copy(alpha = 0.5f),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
        }

        Text(
            text = "¿Olvidaste tu contraseña?",
            color = DarkGreen,
            fontSize = 12.sp,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        AppButton("INGRESAR") { /* TODO */ }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Sage.copy(alpha = 0.5f))
            Text(
                text = "o continúa con",
                modifier = Modifier.padding(horizontal = 8.dp),
                color = MediumGreen,
                fontSize = 12.sp
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = Sage.copy(alpha = 0.5f))
        }

        OutlinedButton(
            onClick = { /* TODO */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MediumGreen),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkGreen)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cuenta institucional (SSO)", fontWeight = FontWeight.Normal)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row {
            Text(text = "¿No tienes una cuenta? ", fontSize = 14.sp, color = MediumGreen)
            Text(
                text = "Crear cuenta",
                color = DarkGreen,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun BodyHomeScreenPreview() {
    TheStudentsTheme {
        BodyHomeScreen()
    }
}

@Composable
fun HomeScreen() {
    BodyHomeScreen()
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    TheStudentsTheme {
        HomeScreen()
    }
}
