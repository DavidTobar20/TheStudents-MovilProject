package com.example.thestudents.ui.screens.search

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.example.thestudents.ui.components.Student
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
            Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(student.profileColor), contentAlignment = Alignment.Center) {
                if (student.profileImageRes != null) {
                    Image(
                        painter = painterResource(id = student.profileImageRes),
                        contentDescription = "Imagen de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(text = student.initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = student.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkGreen)
                Text(text = "${student.program} · ${student.reviewsCount} reseñas", fontSize = 13.sp, color = MediumGreen.copy(alpha = 0.6f))
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    repeat(5) { index ->
                        Icon(Icons.Default.Star, null, Modifier.size(16.dp), tint = if (index < student.rating.toInt()) Gold else Sage.copy(alpha = 0.3f))
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
    TheStudentsTheme { 
        StudentCard(
            Student(
                id = "1",
                initials = "VT",
                name = "Valentina Torres",
                program = "Psicología",
                reviewsCount = 28,
                rating = 5f,
                profileColor = Color(0xFF7B5CAB),
                email = "valentina@u.edu.co",
                semester = 5,
                bio = "Estudiante de psicología"
            )
        ) 
    }
}

// 5. PANTALLA COMPLETA
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController()
) {
    var query by remember { mutableStateOf("") }
    val students = listOf(
        Student("1", "Valentina Torres", "valentina@u.edu.co", "Psicología", 5, "Estudiante de psicología", 5f, 28, "VT", Color(0xFF7B5CAB)),
        Student("2", "Sebastián López", "sebastian@u.edu.co", "Medicina", 4, "Estudiante de medicina", 5f, 15, "SL", Color(0xFF2C55A0)),
        Student("3", "Camila Herrera", "camila@u.edu.co", "Derecho", 6, "Estudiante de derecho", 5f, 42, "CH", Color(0xFF9E4B31)),
        Student("4", "Nicolás Vargas", "nicolas@u.edu.co", "Economía", 3, "Estudiante de economía", 4f, 9, "NV", Color(0xFF1E3D2A)),
        Student("5", "Isabella Castro", "isabella@u.edu.co", "Arquitectura", 7, "Estudiante de arquitectura", 5f, 33, "IC", Color(0xFF4C8C64))
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
