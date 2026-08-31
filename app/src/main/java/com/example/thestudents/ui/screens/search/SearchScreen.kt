package com.example.thestudents.ui.screens.search

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
import com.example.thestudents.ui.screens.reviews.ReviewsViewModel
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        HeaderSearch()
        SearchBar(query, onQueryChange = onQueryChange)
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.sugerencias_para_ti),
            modifier = Modifier.fillMaxWidth(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(students, key = { it.id }) { student -> 
                StudentCard(
                    student = student,
                    onClick = { onStudentClick(student.id) }
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
                onStudentClick = {}
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

    BodySearch(
        query = state.query,
        onQueryChange = { searchViewModel.updateQuery(it) },
        students = state.students,
        onStudentClick = onStudentClick,
        modifier = modifier
    )
}

@Preview(name = "Claro", showBackground = true, showSystemUi = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun FullSearchScreenPreview() {
    TheStudentsTheme {
        Surface {
            SearchScreen(
                searchViewModel = SearchViewModel(),
                onStudentClick = {})
        }
    }
}
