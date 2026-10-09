package com.example.thestudents.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
): ViewModel() {
    private val _uiState= MutableStateFlow(SearchState())
    val uiState: StateFlow<SearchState> = _uiState

    fun updateQuery(input: String){
        _uiState.update { it.copy(query = input) }
    }

    fun getAllStudents(){
        _uiState.update { it.copy(students = localStudentProvider.students) }
        viewModelScope.launch {
            val result = reviewRepository.getAllUsers()
            result.onSuccess { users ->
                if (users.isNotEmpty()) {
                    _uiState.update { it.copy(students = users) }
                }
            }
        }
    }

    init {
        getAllStudents()
    }
}