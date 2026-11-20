package com.example.thestudents.ui.screens.teamMatch

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.components.SectionLabel
import com.example.thestudents.ui.screens.teamMatch.components.CourseSelectionSection
import com.example.thestudents.ui.screens.teamMatch.components.HowItWorksDialog
import com.example.thestudents.ui.screens.teamMatch.components.MatchCandidateCard
import com.example.thestudents.ui.screens.teamMatch.components.SuggestedTeamCard
import com.example.thestudents.ui.screens.teamMatch.components.TeamMatchPreviewData
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ButtonWithoutIcon
import com.example.thestudents.ui.utils.HeaderBack

@Composable
fun TeamMatchScreen(
    teamMatchViewModel: TeamMatchViewModel,
    onBackClick: () -> Unit,
    onStudentClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by teamMatchViewModel.uiState.collectAsState()

    // En las recomendaciones, "atrás" vuelve a elegir materia
    BackHandler(enabled = state.step == TeamMatchStep.RESULTS) {
        teamMatchViewModel.changeCourse()
    }

    if (state.showHowItWorks) {
        HowItWorksDialog(onDismiss = { teamMatchViewModel.showHowItWorks(false) })
    }

    TeamMatchContent(
        state = state,
        onBackClick = {
            if (state.step == TeamMatchStep.RESULTS) teamMatchViewModel.changeCourse() else onBackClick()
        },
        onStudentClick = onStudentClick,
        onRetryCoursesClick = { teamMatchViewModel.loadCourseOptions() },
        onCourseSelected = { teamMatchViewModel.onCourseSelected(it) },
        onSearchClick = { teamMatchViewModel.searchTeam() },
        onOpenSearch = { teamMatchViewModel.loadRecommendations(it) },
        onStopSearch = { teamMatchViewModel.stopSearch(it) },
        onChangeCourseClick = { teamMatchViewModel.changeCourse() },
        onRetryClick = { teamMatchViewModel.loadRecommendations() },
        onTeamSizeChange = { teamMatchViewModel.onTeamSizeChange(it) },
        onToggleCandidate = { teamMatchViewModel.toggleCandidate(it) },
        onResetSuggestion = { teamMatchViewModel.resetToSuggestion() },
        onSendInvites = { teamMatchViewModel.sendInvites() },
        onHowItWorksClick = { teamMatchViewModel.showHowItWorks(true) },
        modifier = modifier
    )
}

@Composable
fun TeamMatchContent(
    state: TeamMatchState,
    onBackClick: () -> Unit,
    onStudentClick: (String) -> Unit,
    onRetryCoursesClick: () -> Unit,
    onCourseSelected: (String) -> Unit,
    onSearchClick: () -> Unit,
    onOpenSearch: (String) -> Unit,
    onStopSearch: (String) -> Unit,
    onChangeCourseClick: () -> Unit,
    onRetryClick: () -> Unit,
    onTeamSizeChange: (Int) -> Unit,
    onToggleCandidate: (String) -> Unit,
    onResetSuggestion: () -> Unit,
    onSendInvites: () -> Unit,
    onHowItWorksClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        HeaderBack(
            title = stringResource(R.string.arma_tu_equipo_titulo),
            onBackClick = onBackClick
        )

        when (state.step) {
            TeamMatchStep.SELECT_COURSE -> SelectCourseStep(
                state = state,
                onRetryCoursesClick = onRetryCoursesClick,
                onCourseSelected = onCourseSelected,
                onSearchClick = onSearchClick,
                onOpenSearch = onOpenSearch,
                onStopSearch = onStopSearch
            )

            TeamMatchStep.RESULTS -> ResultsStep(
                state = state,
                onStudentClick = onStudentClick,
                onChangeCourseClick = onChangeCourseClick,
                onRetryClick = onRetryClick,
                onTeamSizeChange = onTeamSizeChange,
                onToggleCandidate = onToggleCandidate,
                onResetSuggestion = onResetSuggestion,
                onSendInvites = onSendInvites,
                onHowItWorksClick = onHowItWorksClick
            )
        }
    }
}

// ---------- Paso 1: elegir la materia ----------

@Composable
private fun SelectCourseStep(
    state: TeamMatchState,
    onRetryCoursesClick: () -> Unit,
    onCourseSelected: (String) -> Unit,
    onSearchClick: () -> Unit,
    onOpenSearch: (String) -> Unit,
    onStopSearch: (String) -> Unit
) {
    when {
        state.isLoadingCourses -> CenteredMessage {
            CircularProgressIndicator()
        }

        state.coursesError != null && state.availableCourses.isEmpty() -> CenteredMessage {
            Text(
                text = state.coursesError ?: "",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            ButtonWithoutIcon(
                textoBoton = stringResource(R.string.reintentar),
                onClick = onRetryCoursesClick,
                fontSize = 14.sp,
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp)
            )
        }

        else -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp)
        ) {
            item {
                CourseSelectionSection(
                    periodo = state.periodo,
                    availableCourses = state.availableCourses,
                    mySearches = state.mySearches,
                    selectedCourse = state.selectedCourse,
                    isStartingSearch = state.isStartingSearch,
                    errorMessage = state.coursesError,
                    onCourseSelected = onCourseSelected,
                    onSearchClick = onSearchClick,
                    onOpenSearch = onOpenSearch,
                    onStopSearch = onStopSearch
                )
            }
        }
    }
}

// ---------- Paso 2: recomendaciones ----------

@Composable
private fun ResultsStep(
    state: TeamMatchState,
    onStudentClick: (String) -> Unit,
    onChangeCourseClick: () -> Unit,
    onRetryClick: () -> Unit,
    onTeamSizeChange: (Int) -> Unit,
    onToggleCandidate: (String) -> Unit,
    onResetSuggestion: () -> Unit,
    onSendInvites: () -> Unit,
    onHowItWorksClick: () -> Unit
) {
    when {
        state.isLoading -> CenteredMessage {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.analizando_resenas),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        state.errorMessage != null -> CenteredMessage {
            Text(
                text = state.errorMessage ?: "",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            ButtonWithoutIcon(
                textoBoton = stringResource(R.string.reintentar),
                onClick = onRetryClick,
                fontSize = 14.sp,
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp)
            )
        }

        state.candidates.isEmpty() -> CenteredMessage {
            SectionLabel(text = "${state.materia} · ${state.periodo}")
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.sin_companeros_para_equipo),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            ButtonWithoutIcon(
                textoBoton = stringResource(R.string.cambiar_materia),
                onClick = onChangeCourseClick,
                fontSize = 14.sp,
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp)
            )
        }

        else -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            // El margen va en contentPadding para que las sombras de las tarjetas no se corten
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SectionLabel(
                    text = "${state.materia} · ${state.periodo}",
                    modifier = Modifier.padding(start = 4.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.arma_tu_equipo_intro),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onChangeCourseClick) {
                        Icon(Icons.Outlined.SwapHoriz, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = stringResource(R.string.cambiar_materia))
                    }
                    TextButton(onClick = onHowItWorksClick) {
                        Icon(Icons.Outlined.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = stringResource(R.string.como_calculamos_match))
                    }
                }
            }

            item {
                SuggestedTeamCard(
                    members = state.selectedCandidates,
                    teamSize = state.teamSize,
                    maxTeamSize = state.maxTeamSize,
                    teamScore = state.teamScore,
                    isSendingInvites = state.isSendingInvites,
                    invitesSent = state.invitesSent,
                    invitedCount = state.invitedCount,
                    inviteError = state.inviteError,
                    onTeamSizeChange = onTeamSizeChange,
                    onResetSuggestion = onResetSuggestion,
                    onSendInvites = onSendInvites,
                    minTeamSize = TeamMatchState.MIN_TEAM_SIZE
                )
            }

            item {
                SectionLabel(
                    text = "${stringResource(R.string.seccion_buscan_equipo)} (${state.candidates.size})",
                    modifier = Modifier.padding(start = 4.dp, top = 8.dp)
                )
            }

            itemsIndexed(state.candidates, key = { _, candidate -> candidate.student.id }) { index, candidate ->
                MatchCandidateCard(
                    rank = index + 1,
                    candidate = candidate,
                    isSelected = candidate.student.id in state.selectedIds,
                    canAdd = !state.isTeamFull && !state.isSendingInvites,
                    onToggleClick = { onToggleCandidate(candidate.student.id) },
                    onStudentClick = { onStudentClick(candidate.student.id) }
                )
            }
        }
    }
}

@Composable
private fun CenteredMessage(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            content()
        }
    }
}

@Preview(name = "Claro", showBackground = true, showSystemUi = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun TeamMatchContentPreview() {
    val candidates = TeamMatchPreviewData.candidates
    TheStudentsTheme {
        Surface {
            TeamMatchContent(
                state = TeamMatchState(
                    step = TeamMatchStep.RESULTS,
                    materia = "Bases de Datos",
                    periodo = "2027-1",
                    candidates = candidates,
                    selectedIds = candidates.take(2).map { it.student.id }.toSet()
                ),
                onBackClick = {},
                onStudentClick = {},
                onRetryCoursesClick = {},
                onCourseSelected = {},
                onSearchClick = {},
                onOpenSearch = {},
                onStopSearch = {},
                onChangeCourseClick = {},
                onRetryClick = {},
                onTeamSizeChange = {},
                onToggleCandidate = {},
                onResetSuggestion = {},
                onSendInvites = {},
                onHowItWorksClick = {}
            )
        }
    }
}
