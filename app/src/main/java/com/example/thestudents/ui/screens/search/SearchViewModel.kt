package com.example.thestudents.ui.screens.search

import androidx.lifecycle.ViewModel
import com.example.thestudents.data.local.localCourseSectionProvider
import com.example.thestudents.data.local.localStudentProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(): ViewModel() {
    private val _uiState= MutableStateFlow(SearchState())
    val uiState: StateFlow<SearchState> = _uiState

    fun updateQuery(input: String){
        _uiState.update { it.copy(query = input) }
    }
    fun getAllStudents(){
        _uiState.update { it.copy(students = localStudentProvider.students) }
    }
    init {
        getAllStudents()
    }
}