package com.example.thestudents.ui.screens.editarPerfil

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thestudents.R
import com.example.thestudents.data.Student
import com.example.thestudents.ui.screens.editarPerfil.components.EditBioSection
import com.example.thestudents.ui.screens.editarPerfil.components.EditFormSection
import com.example.thestudents.ui.screens.editarPerfil.components.EditPhotoSection
import com.example.thestudents.ui.screens.editarPerfil.components.EditPreferencesSection
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ButtonWithoutIcon
import com.example.thestudents.ui.utils.HeaderBack

/**
 * Contenido de editar perfil. Sin estado propio: cada campo llega con su valor y su callback.
 */
@Composable
fun BodyEditarPerfilScreen(
    student: Student,
    name: String,
    onNameChange: (String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    program: String,
    onProgramChange: (String) -> Unit,
    semester: String,
    onSemesterChange: (String) -> Unit,
    bio: String,
    onBioChange: (String) -> Unit,
    showReviews: Boolean,
    onShowReviewsChange: (Boolean) -> Unit,
    notificationsEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onChangePhotoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        HeaderBack(
            title = stringResource(R.string.editar_perfil_mayuscula),
            onBackClick = onBackClick
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            item {
                EditPhotoSection(
                    initials = student.initials,
                    profileImage = student.profileImage,
                    onEditClick = onChangePhotoClick
                )
            }

            item {
                EditFormSection(
                    name = name,
                    onNameChange = onNameChange,
                    username = username,
                    onUsernameChange = onUsernameChange,
                    program = program,
                    onProgramChange = onProgramChange,
                    semester = semester,
                    onSemesterChange = onSemesterChange
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                EditBioSection(
                    bio = bio,
                    onBioChange = onBioChange
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                EditPreferencesSection(
                    showReviews = showReviews,
                    onShowReviewsChange = onShowReviewsChange,
                    notificationsEnabled = notificationsEnabled,
                    onNotificationsChange = onNotificationsChange
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }

        // Boton de guardar anclado al final, fuera de la zona con scroll.
        ButtonWithoutIcon(
            textoBoton = stringResource(R.string.guardar_cambios_mayuscula),
            onClick = onSaveClick,
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp, vertical = 16.dp)
                .height(48.dp)
        )
    }
}

/**
 * Pantalla de editar perfil con manejo de estado (Carga y Error).
 */
@Composable
fun EditarPerfilScreen(
    editarPerfilViewModel: EditarPerfilViewModel,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by editarPerfilViewModel.uiState.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { editarPerfilViewModel.uploadImageToFirebase(it) }
    }

    when {
        state.isLoading && state.student == null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        state.student == null -> {
            Column(modifier = modifier.fillMaxSize()) {
                HeaderBack(
                    title = stringResource(R.string.editar_perfil_mayuscula),
                    onBackClick = onBackClick
                )
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
        }
        else -> {
            BodyEditarPerfilScreen(
                student = state.student!!,
                name = state.name,
                onNameChange = { editarPerfilViewModel.updateName(it) },
                username = state.username,
                onUsernameChange = { editarPerfilViewModel.updateUsername(it) },
                program = state.program,
                onProgramChange = { editarPerfilViewModel.updateProgram(it) },
                semester = state.semester,
                onSemesterChange = { editarPerfilViewModel.updateSemester(it) },
                bio = state.bio,
                onBioChange = { editarPerfilViewModel.updateBio(it) },
                showReviews = state.showReviews,
                onShowReviewsChange = { editarPerfilViewModel.updateShowReviews(it) },
                notificationsEnabled = state.notificationsEnabled,
                onNotificationsChange = { editarPerfilViewModel.updateNotificationsEnabled(it) },
                onBackClick = onBackClick,
                onSaveClick = {
                    editarPerfilViewModel.onSaveClick {
                        onSaveClick()
                    }
                },
                onChangePhotoClick = {
                    launcher.launch("image/*")
                },
                modifier = modifier
            )
        }
    }
}

@Preview(name = "Claro", showBackground = true, showSystemUi = true)
@Preview(name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun EditarPerfilScreenPreview() {
    TheStudentsTheme {
        Surface {
            EditarPerfilScreen(
                editarPerfilViewModel = viewModel(),
                onBackClick = {},
                onSaveClick = {}
            )
        }
    }
}
