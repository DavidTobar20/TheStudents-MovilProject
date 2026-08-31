package com.example.thestudents.ui.screens.writeReview

import androidx.lifecycle.ViewModel
import com.example.thestudents.data.local.localStudentProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class WriteReviewViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(WriteReviewState())
    val uiState: StateFlow<WriteReviewState> = _uiState

    fun updateRating(input: Int){
        _uiState.update { it.copy(rating = input) }
    }
    
    fun getStudentById(id: String) {
        _uiState.update {
            it.copy(
                student = localStudentProvider.students.find { it.id == id }
            )
        }
    }

    fun updateReview(input: String){
        _uiState.update { it.copy(review = input) }
    }

    fun updateIsAnonymous(input: Boolean){
        _uiState.update { it.copy(isAnonymous = input) }
    }
}
