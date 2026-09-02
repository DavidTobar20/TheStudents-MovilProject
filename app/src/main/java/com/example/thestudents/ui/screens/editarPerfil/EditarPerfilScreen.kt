package com.example.thestudents.ui.screens.editarPerfil

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.example.thestudents.ui.utils.HeaderBack
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ButtonWithoutIcon

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
                    profileImageRes = student.profileImage,
                    onEditClick = onChangePhotoClick
                )
            }

            item {
                EditFormSection(
                    name = name,
                    onNameChange = onNameChange,
                    username = username,
                    onUsernameChange = onUsernameChange,
                    program = student.program,
                    semester = stringResource(R.string.semestre_numero, student.semester)
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
 * Pantalla de editar perfil.
 *
 * Es la duena del formulario. Se usa rememberSaveable para que lo escrito sobreviva a un giro de
 * pantalla, que con remember se perdia.
 */
@Composable
fun EditarPerfilScreen(
    editarPerfilViewModel: EditarPerfilViewModel,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by editarPerfilViewModel.uiState.collectAsState()

    BodyEditarPerfilScreen(
        student = state.student,
        name = state.name,
        onNameChange = { editarPerfilViewModel.updateName(it) },
        username = state.username,
        onUsernameChange = { editarPerfilViewModel.updateUsername(it) },
        bio = state.bio,
        onBioChange = { editarPerfilViewModel.updateBio(it) },
        showReviews = state.showReviews,
        onShowReviewsChange = { editarPerfilViewModel.updateShowReviews(it) },
        notificationsEnabled = state.notificationsEnabled,
        onNotificationsChange = { editarPerfilViewModel.updateNotificationsEnabled(it) },
        onBackClick = onBackClick,
        onSaveClick = onSaveClick,
        onChangePhotoClick = { },
        modifier = modifier
    )
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
