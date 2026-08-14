package com.example.thestudents.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.ui.theme.TheStudentsTheme

import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.ui.components.DiamondDivider
import com.example.thestudents.ui.components.FixedBottomBar
import com.example.thestudents.ui.theme.*

// 1. PREVIEW: Encabezado
@Composable
fun HeaderSearch(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "BUSCAR ESTUDIANTES",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = DarkGreen,
            letterSpacing = 1.sp
        )
        DiamondDivider(modifier = Modifier.padding(vertical = 16.dp).width(200.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderSearchPreview() {
    TheStudentsTheme { HeaderSearch(modifier = Modifier.padding(16.dp)) }
}

// 2. PREVIEW: Barra de búsqueda
@Composable
fun SearchBarComponent(
    query: String,
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("Buscar por nombre o carrera...", color = Sage) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Sage) },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Sage.copy(alpha = 0.5f),
            unfocusedBorderColor = Sage.copy(alpha = 0.5f),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
        ),
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    TheStudentsTheme { SearchBarComponent("") {} }
}

// 3. PREVIEW: Tarjeta de Estudiante
data class Student(val initials: String, val name: String, val program: String, val reviews: Int, val rating: Int, val color: Color)

@Composable
fun StudentCard(
    student: Student,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth().padding(vertical = 8.dp),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(student.color), contentAlignment = Alignment.Center) {
                Text(text = student.initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = student.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkGreen)
                Text(text = "${student.program} · ${student.reviews} reseñas", fontSize = 13.sp, color = MediumGreen.copy(alpha = 0.6f))
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    repeat(5) { index ->
                        Icon(Icons.Default.Star, null, Modifier.size(16.dp), tint = if (index < student.rating) Gold else Sage.copy(alpha = 0.3f))
                    }
                }
            }
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text("SEGUIR", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentCardPreview() {
    TheStudentsTheme { StudentCard(Student("VT", "Valentina Torres", "Psicología", 28, 5, Color(0xFF7B5CAB))) }
}

// 5. PANTALLA COMPLETA
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController()
) {
    var query by remember { mutableStateOf("") }
    val students = listOf(
        Student("VT", "Valentina Torres", "Psicología", 28, 5, Color(0xFF7B5CAB)),
        Student("SL", "Sebastián López", "Medicina", 15, 5, Color(0xFF2C55A0)),
        Student("CH", "Camila Herrera", "Derecho", 42, 5, Color(0xFF9E4B31)),
        Student("NV", "Nicolás Vargas", "Economía", 9, 4, Color(0xFF1E3D2A)),
        Student("IC", "Isabella Castro", "Arquitectura", 33, 5, Color(0xFF4C8C64))
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Cream,
        bottomBar = { FixedBottomBar(navController, "search") }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            HeaderSearch()
            Spacer(modifier = Modifier.height(16.dp))
            SearchBarComponent(query) { query = it }
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "SUGERENCIAS PARA TI",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MediumGreen.copy(alpha = 0.7f),
                letterSpacing = 1.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(students) { student -> StudentCard(student) }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FullSearchScreenPreview() {
    TheStudentsTheme { SearchScreen() }
}
