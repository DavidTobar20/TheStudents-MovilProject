package com.example.thestudents.ui.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.ui.utils.FixedBottomBar
import com.example.thestudents.ui.screens.search.components.HeaderSearch
import com.example.thestudents.ui.screens.search.components.SearchBar
import com.example.thestudents.ui.screens.search.components.StudentCard
import com.example.thestudents.model.Student

// 5. PANTALLA COMPLETA
@Composable
fun BodySearch(
    query: String,
    onQueryChange: (String) -> Unit,
    students: List<Student>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        HeaderSearch()
        SearchBar(query, onQueryChange = onQueryChange)
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "SUGERENCIAS PARA TI",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.medium_green).copy(alpha = 0.7f),
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(students) { student -> StudentCard(student) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BodySearchPreview() {
    val students = listOf(
        Student("1", "Valentina Torres", "valentina@u.edu.co", "Psicología", 5, "Estudiante de psicología", 5f, 28, "VT", Color(0xFF7B5CAB))
    )
    var query by remember { mutableStateOf("") }
    BodySearch(query = query, onQueryChange = {query = it}, students = students)
}

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
        containerColor = colorResource(R.color.cream),
        bottomBar = { FixedBottomBar(navController, "search") }
    ) { padding ->
        BodySearch(
            query = query,
            onQueryChange = { query = it },
            students = students,
            contentPadding = padding
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FullSearchScreenPreview() {
    SearchScreen()
}
