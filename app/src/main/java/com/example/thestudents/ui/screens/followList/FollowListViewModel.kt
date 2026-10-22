package com.example.thestudents.ui.screens.followList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.repository.AuthRepository
import com.example.thestudents.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FollowListState())
    val uiState: StateFlow<FollowListState> = _uiState.asStateFlow()

    fun loadFollowList(userId: String, isFollowingList: Boolean) {
        val currentUserId = authRepository.currentUser?.uid ?: ""
        val isOwnProfile = userId == currentUserId

        val title = if (isFollowingList) "SIGUIENDO" else "SEGUIDORES"
        val emptyMessage = when {
            isFollowingList && isOwnProfile -> "No sigues a nadie"
            isFollowingList -> "Este usuario no sigue a nadie"
            isOwnProfile -> "No eres seguido por nadie"
            else -> "Este usuario no tiene seguidores"
        }

        _uiState.update {
            it.copy(
                title = title,
                currentUserId = currentUserId,
                emptyMessage = emptyMessage,
                isLoading = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            // Cargar los IDs que el usuario actual sigue para saber si mostrar SEGUIR o SIGUIENDO
            val myFollowingResult = userRepository.getFollowing(currentUserId)
            val myFollowingIds = if (myFollowingResult.isSuccess) {
                myFollowingResult.getOrNull()?.map { it.id }?.toSet() ?: emptySet()
            } else {
                emptySet()
            }

            val result = if (isFollowingList) {
                myFollowingResult
            } else {
                userRepository.getFollowers(userId)
            }

            if (result.isSuccess) {
                val listUsers = result.getOrNull() ?: emptyList()
                _uiState.update {
                    it.copy(
                        students = listUsers,
                        followedStudentIds = myFollowingIds,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } else {
                val error = result.exceptionOrNull()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error?.message ?: "Error al cargar la lista de usuarios"
                    )
                }
            }
        }
    }

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
}
