package com.example.thestudents.ui.screens.search

import androidx.compose.foundation.layout.PaddingValues
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thestudents.R
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.screens.search.components.HeaderSearch
import com.example.thestudents.ui.screens.search.components.SearchBar
import com.example.thestudents.ui.screens.search.components.StudentCard
import com.example.thestudents.ui.theme.TheStudentsTheme

/**
 * Contenido de la pantalla de busqueda. Sin estado propio: recibe el texto y avisa de sus cambios.
 */
@Composable
fun BodySearch(
    query: String,
    onQueryChange: (String) -> Unit,
    students: List<Student>,
    onStudentClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    followedStudentIds: Set<String> = emptySet(),
    onFollowToggle: (String) -> Unit = {}
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Barra de titulo de 64 dp, igual que Notificaciones y Reseña
        HeaderSearch()

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SearchBar(query, onQueryChange = onQueryChange)
            Spacer(modifier = Modifier.height(20.dp))
            // Etiqueta en versalitas doradas, como "COMENTARIOS" y "RESEÑA PARA"
            Text(
                text = stringResource(R.string.sugerencias_para_ti).uppercase(),
                modifier = Modifier.padding(horizontal = 4.dp),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.4.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        // El margen va en contentPadding para que las sombras de las tarjetas no se corten
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
        ) {
            items(students, key = { it.id }) { student ->
                StudentCard(
                    student = student,
                    isFollowing = student.id in followedStudentIds,
                    onClick = { onStudentClick(student.id) },
                    onFollowToggle = { onFollowToggle(student.id) }
                )
            }
        }
    }
}

@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun BodySearchPreview() {
    TheStudentsTheme {
        Surface {
            var query by rememberSaveable { mutableStateOf("") }
            BodySearch(
                query = query,
                onQueryChange = { query = it },
                students = localStudentProvider.students,
                onStudentClick = {},

            )
        }
    }
}

/**
 * Pantalla de busqueda con ViewModel (MVVM).
 * Observa el estado del UI y delega las acciones al ViewModel.
 */
@Composable
fun SearchScreen(
    onStudentClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel
) {
    val state by searchViewModel.uiState.collectAsState()

    // Se recarga cada vez que se entra a Buscar, por si seguiste o dejaste de seguir
    // a alguien desde su perfil o desde la lista de seguidores
    LaunchedEffect(Unit) {
        searchViewModel.loadFollowedIds()
    }

    BodySearch(
        query = state.query,
        onQueryChange = { searchViewModel.updateQuery(it) },
        students = state.students,
        onStudentClick = onStudentClick,
        modifier = modifier,
        followedStudentIds = state.followedStudentIds,
        onFollowToggle = { searchViewModel.toggleFollow(it) }
    )
}

@Preview(name = "Claro", showBackground = true, showSystemUi = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun FullSearchScreenPreview() {
    TheStudentsTheme {
        Surface {
            SearchScreen(
                searchViewModel = viewModel(),
                onStudentClick = {}
            )
        }
    }
}
