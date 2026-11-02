package com.example.thestudents.ui.screens.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thestudents.data.CourseSection
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.data.repository.AuthRepository
import com.example.thestudents.data.repository.InscripcionRepository
import com.example.thestudents.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val inscripcionRepository: InscripcionRepository,
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(ReviewsState())
    val uiState: StateFlow<ReviewsState> = _uiState

    init {
        getAllSections()
    }

    fun getAllSections() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val userId = authRepository.currentUser?.uid ?: localStudentProvider.currentUser.id

            val sectionsResult = inscripcionRepository.getInscripcionesPorUsuario(userId)
            val catalogoResult = inscripcionRepository.getCatalogoMateriasYPeriodos()
            val misMateriasResult = inscripcionRepository.getMateriasYaInscritas(userId)

            if (sectionsResult.isSuccess) {
                val sections = sectionsResult.getOrNull() ?: emptyList()
                val catalogo = catalogoResult.getOrNull() ?: emptyMap()
                val yaInscritas = misMateriasResult.getOrNull() ?: emptySet()

                // Seleccionar por defecto la primera materia que aún no haya cursado/inscrito
                val primeraDisponible = catalogo.keys.firstOrNull { it !in yaInscritas }
                    ?: catalogo.keys.firstOrNull().orEmpty()
                val primerPeriodo = catalogo[primeraDisponible]?.firstOrNull().orEmpty()

                _uiState.update {
                    it.copy(
                        sections = sections,
                        catalogoMaterias = catalogo,
                        materiasYaInscritas = yaInscritas,
                        selectedMateria = primeraDisponible,
                        selectedPeriodo = primerPeriodo,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } else {
                val error = sectionsResult.exceptionOrNull()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error?.message ?: "Error al cargar las secciones"
                    )
                }
            }
        }
    }

    fun openEnrollDialog() {
        val state = _uiState.value
        val primeraDisponible = state.catalogoMaterias.keys.firstOrNull { it !in state.materiasYaInscritas }
            ?: state.catalogoMaterias.keys.firstOrNull().orEmpty()
        val primerPeriodo = state.catalogoMaterias[primeraDisponible]?.firstOrNull().orEmpty()

        _uiState.update {
            it.copy(
                showEnrollDialog = true,
                selectedMateria = primeraDisponible,
                selectedPeriodo = primerPeriodo,
                enrollErrorMessage = null
            )
        }
    }

    fun closeEnrollDialog() {
        _uiState.update {
            it.copy(
                showEnrollDialog = false,
                enrollErrorMessage = null
            )
        }
    }

    fun onMateriaSelected(materia: String) {
        val periodos = _uiState.value.catalogoMaterias[materia].orEmpty()
        _uiState.update {
            it.copy(
                selectedMateria = materia,
                selectedPeriodo = periodos.firstOrNull().orEmpty(),
                enrollErrorMessage = null
            )
        }
    }

    fun onPeriodoSelected(periodo: String) {
        _uiState.update {
            it.copy(
                selectedPeriodo = periodo,
                enrollErrorMessage = null
            )
        }
    }

    fun inscribirMateria() {
        val state = _uiState.value
        val materia = state.selectedMateria
        val periodo = state.selectedPeriodo
        val userId = authRepository.currentUser?.uid ?: localStudentProvider.currentUser.id

        if (materia.isBlank() || periodo.isBlank()) {
            _uiState.update { it.copy(enrollErrorMessage = "Selecciona una materia y un periodo") }
            return
        }

        if (state.materiasYaInscritas.any { it.equals(materia, ignoreCase = true) }) {
            _uiState.update { it.copy(enrollErrorMessage = "Ya cursaste o tienes inscrita esta materia") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isEnrolling = true, enrollErrorMessage = null) }
            val result = inscripcionRepository.createInscripcion(
                usuarioId = userId,
                materia = materia,
                periodo = periodo
            )
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        isEnrolling = false,
                        showEnrollDialog = false,
                        enrollErrorMessage = null
                    )
                }
                getAllSections()
            } else {
                _uiState.update {
                    it.copy(
                        isEnrolling = false,
                        enrollErrorMessage = result.exceptionOrNull()?.message
                            ?: "No se pudo inscribir la materia"
                    )
                }
            }
        }
    }

    fun openWithdrawDialog(section: CourseSection) {
        _uiState.update {
            it.copy(sectionToWithdraw = section)
        }
    }

    fun closeWithdrawDialog() {
        _uiState.update {
            it.copy(sectionToWithdraw = null)
        }
    }

    fun confirmarRetiroMateria() {
        val section = _uiState.value.sectionToWithdraw ?: return
        val userId = authRepository.currentUser?.uid ?: localStudentProvider.currentUser.id

        viewModelScope.launch {
            _uiState.update { it.copy(isWithdrawing = true) }

            // 1. Reseñas que el usuario hizo y que recibió (métodos que ya existían)
            val hechasResult = reviewRepository.getReviewsByReviewerId(userId)
            val recibidasResult = reviewRepository.getReviewsByReviewedStudentId(userId)

            if (hechasResult.isFailure || recibidasResult.isFailure) {
                val error = hechasResult.exceptionOrNull() ?: recibidasResult.exceptionOrNull()
                terminarRetiroConError(error?.message ?: "No se pudieron cargar las reseñas de la materia")
                return@launch
            }

            // 2. Quedarse solo con las de esta materia y periodo
            val resenasDeLaMateria = (hechasResult.getOrDefault(emptyList()) +
                    recibidasResult.getOrDefault(emptyList()))
                .filter { it.classReviewed == section.title && it.periodReviewed == section.period }
                .distinctBy { it.id }

            // 3. Borrar cada reseña con el deleteReview que ya existía
            for (review in resenasDeLaMateria) {
                val deleteResult = reviewRepository.deleteReview(review.id)
                if (deleteResult.isFailure) {
                    terminarRetiroConError(
                        deleteResult.exceptionOrNull()?.message ?: "No se pudo eliminar una reseña"
                    )
                    return@launch
                }
            }

            // 4. Al final se retira la inscripción. Si algo falló antes, la materia sigue
            // inscrita y el usuario puede volver a intentarlo.
            val retiroResult = inscripcionRepository.retirarMateria(
                usuarioId = userId,
                materia = section.title,
                periodo = section.period
            )
            if (retiroResult.isSuccess) {
                _uiState.update {
                    it.copy(
                        isWithdrawing = false,
                        sectionToWithdraw = null
                    )
                }
                getAllSections()
            } else {
                terminarRetiroConError(
                    retiroResult.exceptionOrNull()?.message ?: "No se pudo retirar la materia"
                )
            }
        }
    }

    private fun terminarRetiroConError(mensaje: String) {
        _uiState.update {
            it.copy(
                isWithdrawing = false,
                sectionToWithdraw = null,
                errorMessage = mensaje
            )
        }
    }
}
