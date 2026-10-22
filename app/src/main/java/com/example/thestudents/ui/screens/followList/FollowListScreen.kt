package com.example.thestudents.ui.screens.followList

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thestudents.R
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.screens.search.components.StudentCard
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.HeaderBack

@Composable
fun BodyFollowListScreen(
    students: List<Student>,
    followedStudentIds: Set<String>,
    currentUserId: String,
    emptyMessage: String,
    onStudentClick: (String) -> Unit,
    onFollowToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (students.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emptyMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(24.dp)
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(students, key = { it.id }) { student ->
                StudentCard(
                    student = student,
                    isFollowing = student.id in followedStudentIds,
                    showFollowButton = student.id != currentUserId,
                    onClick = { onStudentClick(student.id) },
                    onFollowToggle = { onFollowToggle(student.id) }
                )
            }
        }
    }
}

@Composable
fun FollowListScreen(
    followListViewModel: FollowListViewModel,
    userId: String,
    isFollowingList: Boolean,
    onBackClick: () -> Unit,
    onStudentClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by followListViewModel.uiState.collectAsState()

    LaunchedEffect(userId, isFollowingList) {
        followListViewModel.loadFollowList(userId, isFollowingList)
    }

    val displayTitle = state.title.ifEmpty { if (isFollowingList) "SIGUIENDO" else "SEGUIDORES" }

    Column(modifier = modifier.fillMaxSize()) {
        HeaderBack(
            title = displayTitle,
            onBackClick = onBackClick
        )

        when {
            state.isLoading && state.students.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.errorMessage != null && state.students.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.errorMessage ?: stringResource(R.string.estudiante_no_encontrado),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> {
                BodyFollowListScreen(
                    students = state.students,
                    followedStudentIds = state.followedStudentIds,
                    currentUserId = state.currentUserId,
                    emptyMessage = state.emptyMessage,
                    onStudentClick = onStudentClick,
                    onFollowToggle = { targetId ->
                        followListViewModel.toggleFollow(targetId)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun BodyFollowListScreenPreview() {
    TheStudentsTheme {
        Surface {
            BodyFollowListScreen(
                students = localStudentProvider.students,
                followedStudentIds = setOf("1", "3"),
                currentUserId = "0",
                emptyMessage = "No se encontraron estudiantes.",
                onStudentClick = {},
                onFollowToggle = {}
            )
        }
    }
}
