package com.example.thestudents.ui.screens.studentDetail

import androidx.lifecycle.ViewModel
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.data.local.localStudentProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class StudentDetailViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(StudentDetailState())
    val uiState: StateFlow<StudentDetailState> = _uiState

    fun getStudentById(id: String) {
        _uiState.update { 
            it.copy(
                student = localStudentProvider.students.find { it.id == id },
                reviews = localReviewsProvider.allReviews.filter { it.reviewedStudent.id == id }
            )
        }
    }
}
