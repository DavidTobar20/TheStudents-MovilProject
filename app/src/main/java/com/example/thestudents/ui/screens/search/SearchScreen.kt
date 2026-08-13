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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.ui.theme.TheStudentsTheme

// Colores
val DarkGreenS = Color(0xFF1B3935)
val MediumGreenS = Color(0xFF376052)
val SageS = Color(0xFFB2B7AC)
val CreamS = Color(0xFFFBF7F2)
val TanS = Color(0xFFD3C3A7)
val GoldS = Color(0xFFD4AF37)

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
            color = DarkGreenS,
            letterSpacing = 1.sp
        )
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            HorizontalDivider(modifier = Modifier.width(60.dp), color = TanS, thickness = 1.dp)
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(10.dp)
                    .rotate(45f)
                    .background(TanS)
            )
            HorizontalDivider(modifier = Modifier.width(60.dp), color = TanS, thickness = 1.dp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderSearchPreview() {
    TheStudentsTheme { HeaderSearch(modifier = Modifier.padding(16.dp)) }
}

// 2. PREVIEW: Barra de búsqueda
@Composable
fun SearchBarComponent(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Buscar por nombre o carrera...", color = SageS) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SageS) },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SageS.copy(alpha = 0.5f),
            unfocusedBorderColor = SageS.copy(alpha = 0.5f),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    TheStudentsTheme { SearchBarComponent("", {}) }
}

// 3. PREVIEW: Tarjeta de Estudiante
data class Student(val initials: String, val name: String, val program: String, val reviews: Int, val rating: Int, val color: Color)

@Composable
fun StudentCard(student: Student) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
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
                Text(text = student.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkGreenS)
                Text(text = "${student.program} · ${student.reviews} reseñas", fontSize = 13.sp, color = MediumGreenS.copy(alpha = 0.6f))
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    repeat(5) { index ->
                        Icon(Icons.Default.Star, null, Modifier.size(16.dp), tint = if (index < student.rating) GoldS else SageS.copy(alpha = 0.3f))
                    }
                }
            }
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreenS),
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

// 4. PREVIEW: Barra de Navegación corregida
@Composable
fun FixedBottomBar() {
    Surface(
        modifier = Modifier.fillMaxWidth().height(80.dp),
        color = Color.White,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            NavItem(Icons.Default.Home, "Inicio", false)
            NavItem(Icons.Default.Search, "Explorar", true)
            
            Surface(shape = CircleShape, color = DarkGreenS, modifier = Modifier.size(52.dp)) {
                Icon(Icons.Default.School, null, Modifier.padding(12.dp), tint = Color.White)
            }

            NavItem(Icons.Default.Edit, "Publicar", false)
            NavItem(Icons.Default.Person, "Perfil", false)
        }
    }
}

@Composable
fun NavItem(icon: ImageVector, label: String, selected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = if (selected) DarkGreenS else MediumGreenS, modifier = Modifier.size(24.dp))
        Text(text = label, fontSize = 10.sp, color = if (selected) DarkGreenS else MediumGreenS)
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    TheStudentsTheme { FixedBottomBar() }
}

// 5. PANTALLA COMPLETA
@Composable
fun SearchScreen() {
    var query by remember { mutableStateOf("") }
    val students = listOf(
        Student("VT", "Valentina Torres", "Psicología", 28, 5, Color(0xFF7B5CAB)),
        Student("SL", "Sebastián López", "Medicina", 15, 5, Color(0xFF2C55A0)),
        Student("CH", "Camila Herrera", "Derecho", 42, 5, Color(0xFF9E4B31)),
        Student("NV", "Nicolás Vargas", "Economía", 9, 4, Color(0xFF1E3D2A)),
        Student("IC", "Isabella Castro", "Arquitectura", 33, 5, Color(0xFF4C8C64))
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = CreamS,
        bottomBar = { FixedBottomBar() }
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
                color = MediumGreenS.copy(alpha = 0.7f),
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
