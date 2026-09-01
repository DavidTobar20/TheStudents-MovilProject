package com.example.thestudents.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.ui.screens.profile.components.ProfileTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        _uiState.update { it.copy(isLoading = true) }
        val student = localStudentProvider.currentUser
        val reviews = localReviewsProvider.getReviewsForStudent(student.id)
        _uiState.update {
            it.copy(
                student = student,
                reviews = reviews,
                isLoading = false
            )
        }
    }

    fun onTabSelected(tab: ProfileTab) {
        _uiState.update { it.copy(selectedTab = tab) }
        // Si quisiéramos filtrar reviews por pestaña, lo haríamos aquí
        // Por ahora mantenemos la lógica simple según el original
    }
}
