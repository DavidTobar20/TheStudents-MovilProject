package com.example.thestudents.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.Student
import com.example.thestudents.data.local.localCourseSectionProvider
import com.example.thestudents.data.local.localStudentProvider
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
    private val _uiState= MutableStateFlow(SearchState())
    val uiState: StateFlow<SearchState> = _uiState

    // Lista completa traida de Firestore; la busqueda filtra sobre ella
    private var allStudents: List<Student> = emptyList()

    fun updateQuery(input: String){
        _uiState.update { it.copy(query = input, students = filterStudents(allStudents, input)) }
    }
    fun getAllStudents(){
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val currentUserId = authRepository.currentUser?.uid
            val result = userRepository.getAllUsers(excludeId = currentUserId)
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

    // Igual que en FollowListViewModel: ids de los estudiantes que sigue el usuario actual
    fun loadFollowedIds() {
        val currentUserId = authRepository.currentUser?.uid ?: return

        viewModelScope.launch {
            val result = userRepository.getFollowing(currentUserId)
            if (result.isSuccess) {
                val followedIds = result.getOrNull()?.map { it.id }?.toSet() ?: emptySet()
                _uiState.update { it.copy(followedStudentIds = followedIds) }
            }
        }
    }

    // Igual que en FollowListViewModel: seguir o dejar de seguir desde la tarjeta
    fun toggleFollow(targetUserId: String) {
        val currentUserId = authRepository.currentUser?.uid ?: return

        viewModelScope.launch {
            val result = userRepository.followOrUnfollowStudent(currentUserId, targetUserId)
            if (result.isSuccess) {
                _uiState.update { state ->
                    val currentlyFollowing = state.followedStudentIds.contains(targetUserId)
                    val newFollowedIds = if (currentlyFollowing) {
                        state.followedStudentIds - targetUserId
                    } else {
                        state.followedStudentIds + targetUserId
                    }
                    state.copy(followedStudentIds = newFollowedIds)
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


    init {
        getAllStudents()
    }
}