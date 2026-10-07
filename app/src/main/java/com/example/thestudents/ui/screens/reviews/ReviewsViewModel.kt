package com.example.thestudents.ui.screens.reviews

import androidx.lifecycle.ViewModel
import com.example.thestudents.data.local.localInscriptionProvider
import com.example.thestudents.data.local.localStudentProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(ReviewsState())
    val uiState: StateFlow<ReviewsState> = _uiState

    fun getAllSections(){
        val sections = localInscriptionProvider.getCourseSectionsForUser(localStudentProvider.currentUser.id)
        _uiState.update { it.copy(sections = sections) }
    }
    init {
        getAllSections()
    }
}
