package com.example.thestudents.ui.screens.reviews

import androidx.lifecycle.ViewModel
import com.example.thestudents.data.local.localCourseSectionProvider
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
        _uiState.update { it.copy(sections = localCourseSectionProvider.sections) }
    }
    init {
        getAllSections()
    }
}