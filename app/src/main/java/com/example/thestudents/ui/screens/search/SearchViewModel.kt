package com.example.thestudents.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.Student
import com.example.thestudents.data.repository.AuthRepository
import com.example.thestudents.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(SearchState())
    val uiState: StateFlow<SearchState> = _uiState

    // Lista completa traida de Firestore; la busqueda filtra sobre ella
    private var allStudents: List<Student> = emptyList()

    init {
        getAllStudents()
    }

    fun updateQuery(input: String) {
        _uiState.update { it.copy(query = input, students = filterStudents(allStudents, input)) }
    }

    fun getAllStudents() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val currentUserId = authRepository.currentUser?.uid
            val result = userRepository.getAllStudents(excludeId = currentUserId)
            if (result.isSuccess) {
                allStudents = result.getOrNull() ?: emptyList()
                _uiState.update {
                    it.copy(
                        students = filterStudents(allStudents, it.query),
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Error al cargar los estudiantes"
                    )
                }
            }
        }
    }

    // Firestore no tiene busqueda por "contiene", asi que se filtra en el cliente
    private fun filterStudents(students: List<Student>, query: String): List<Student> {
        val text = query.trim()
        if (text.isEmpty()) return students
        return students.filter {
            it.name.contains(text, ignoreCase = true) ||
                it.username.contains(text, ignoreCase = true) ||
                it.program.contains(text, ignoreCase = true)
        }
    }
}
