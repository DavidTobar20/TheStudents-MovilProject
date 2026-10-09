package com.example.thestudents.ui.screens.studentDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.local.localReviewsProvider
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentDetailViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(StudentDetailState())
    val uiState: StateFlow<StudentDetailState> = _uiState

    fun getStudentById(id: String) {
        val localStudent = localStudentProvider.students.find { it.id == id }
        val localReviews = localReviewsProvider.allReviews.filter { it.reviewedStudent.id == id }
        _uiState.update { 
            it.copy(
                student = localStudent,
                reviews = localReviews
            )
        }

        viewModelScope.launch {
            val userResult = reviewRepository.getUserById(id)
            userResult.onSuccess { fetchedStudent ->
                _uiState.update { state ->
                    val curStudent = state.student
                    val updated = if (curStudent != null) {
                        fetchedStudent.copy(
                            rating = curStudent.rating,
                            reviewsCount = curStudent.reviewsCount
                        )
                    } else fetchedStudent
                    state.copy(student = updated)
                }
            }

            val reviewsResult = reviewRepository.getReviewsByReviewedStudentId(id)
            reviewsResult.onSuccess { fetchedReviews ->
                val averageRating = if (fetchedReviews.isNotEmpty()) {
                    val ratings = fetchedReviews.mapNotNull { it.rating?.toFloatOrNull() }
                    if (ratings.isNotEmpty()) ratings.average().toFloat() else 0f
                } else 0f

                _uiState.update { state ->
                    val updatedStudent = state.student?.copy(
                        reviewsCount = fetchedReviews.size,
                        rating = averageRating
                    ) ?: state.student
                    state.copy(
                        student = updatedStudent,
                        reviews = fetchedReviews
                    )
                }
            }
        }
    }
}
