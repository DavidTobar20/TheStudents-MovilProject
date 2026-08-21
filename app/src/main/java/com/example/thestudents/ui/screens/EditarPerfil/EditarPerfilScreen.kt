package com.example.thestudents.ui.screens.EditarPerfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thestudents.R
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.screens.EditarPerfil.components.EditBioSection
import com.example.thestudents.ui.screens.EditarPerfil.components.EditFormSection
import com.example.thestudents.ui.screens.EditarPerfil.components.EditPhotoSection
import com.example.thestudents.ui.screens.EditarPerfil.components.EditPreferencesSection
import com.example.thestudents.ui.screens.EditarPerfil.components.HeaderEditarPerfil
import com.example.thestudents.ui.theme.TheStudentsTheme
import com.example.thestudents.ui.utils.ButtonWithoutIcon
import com.example.thestudents.ui.utils.FixedBottomBar

/**
 * BODY EDITE PERFIL (Stateless)
 */
@Composable
fun BodyEditarPerfil(
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
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .background(colorResource(R.color.cream))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Contenido scrollable
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    HeaderEditarPerfil(
                        onCancelClick = onCancelClick
                    )
                }

                item {
                    EditPhotoSection(
                        initials = student.initials,
                        profileImageRes = student.profileImage,
                        onEditClick = { /* Acción para cambiar foto */ }
                    )
                }

                item {
                    EditFormSection(
                        name = name,
                        onNameChange = onNameChange,
                        username = username,
                        onUsernameChange = onUsernameChange,
                        program = student.program,
                        semester = "Semestre ${student.semester}"
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

            // Botón GUARDAR CAMBIOS abajo del todo (fijo) -
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                ButtonWithoutIcon(
                    textoBoton = stringResource(R.string.guardar_cambios_mayuscula),
                    onClick = onSaveClick,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                )
            }
        }
    }
}

/**
 * EDITAR PERFIL SCREEN (Stateful)
 */
@Composable
fun EditarPerfilScreen(
    navController: NavController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val student = localStudentProvider.currentUser
    
    // Estado elevado (State Hoisting)
    var name by remember { mutableStateOf(student.name) }
    var username by remember { mutableStateOf(student.username) }
    var bio by remember { mutableStateOf(student.bio) }
    var showReviews by remember { mutableStateOf(true) }
    var notificationsEnabled by remember { mutableStateOf(true) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colorResource(R.color.cream),
        bottomBar = { FixedBottomBar(navController, "profile") }
    ) { padding ->
        BodyEditarPerfil(
            student = student,
            name = name,
            onNameChange = { name = it },
            username = username,
            onUsernameChange = { username = it },
            bio = bio,
            onBioChange = { bio = it },
            showReviews = showReviews,
            onShowReviewsChange = { showReviews = it },
            notificationsEnabled = notificationsEnabled,
            onNotificationsChange = { notificationsEnabled = it },
            onCancelClick = { navController.popBackStack() },
            onSaveClick = { /* Lógica para guardar */ },
            contentPadding = padding
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditarPerfilScreenPreview() {
    TheStudentsTheme {
        EditarPerfilScreen()
    }
}
