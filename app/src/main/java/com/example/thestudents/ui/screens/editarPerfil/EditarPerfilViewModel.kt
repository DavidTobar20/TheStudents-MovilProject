package com.example.thestudents.ui.screens.editarPerfil

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.data.repository.AuthRepository
import com.example.thestudents.data.repository.StorageRepository
import com.example.thestudents.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditarPerfilViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(EditarPerfilState())
    val uiState: StateFlow<EditarPerfilState> = _uiState.asStateFlow()

    init {
        loadStudent()
    }

    fun loadStudent() {
        val currentUser = authRepository.currentUser?.uid ?: ""
        val authPhoto = authRepository.currentUser?.photoUrl?.toString()
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val result = userRepository.getUserById(currentUser)

            if (result.isSuccess) {
                var student = result.getOrNull() ?: localStudentProvider.currentUser
                if (authPhoto != null) {
                    student = student.copy(profileImage = authPhoto)
                }
                _uiState.update {
                    it.copy(
                        student = student,
                        name = student.name,
                        username = student.username,
                        program = student.program,
                        semester = student.semester.toString(),
                        bio = student.bio,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } else {
                val error = result.exceptionOrNull()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error?.message ?: "Error al cargar el perfil"
                    )
                }
            }
        }
    }

    fun onSaveClick(onSuccess: () -> Unit) {
        val currentUser = authRepository.currentUser?.uid ?: ""
        val state = _uiState.value

        if (state.name.isBlank() || state.username.isBlank() || state.program.isBlank() || state.semester.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Todos los campos son obligatorios") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val colorHex = state.student?.profileColor?.let { color ->
                String.format("#%06X", (0xFFFFFF and color.hashCode()))
            } ?: ""

            val result = userRepository.updateUser(
                name = state.name,
                username = state.username,
                program = state.program,
                semester = state.semester.toIntOrNull() ?: 1,
                bio = state.bio,
                fotoUrl = authRepository.currentUser?.photoUrl?.toString(),
                colorPerfil = colorHex,
                userId = currentUser
            )

            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            } else {
                val error = result.exceptionOrNull()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error?.message ?: "Error al actualizar el perfil"
                    )
                }
            }
        }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun updateUsername(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun updateProgram(program: String) {
        _uiState.update { it.copy(program = program) }
    }

    fun updateSemester(semester: String) {
        _uiState.update { state ->
            state.copy(semester = semester.filter { it.isDigit() })
        }
    }

    fun updateBio(bio: String) {
        _uiState.update { it.copy(bio = bio) }
    }

    fun updateShowReviews(showReviews: Boolean) {
        _uiState.update { it.copy(showReviews = showReviews) }
    }

    fun updateNotificationsEnabled(notificationsEnabled: Boolean) {
        _uiState.update { it.copy(notificationsEnabled = notificationsEnabled) }
    }

    fun uploadImageToFirebase(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = storageRepository.uploadProfileImage(uri)
            if (result.isSuccess) {
                val url = result.getOrNull()
                _uiState.update { state ->
                    state.copy(
                        student = state.student?.copy(profileImage = url),
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = result.exceptionOrNull()?.message) }
            }
        }
    }
}
