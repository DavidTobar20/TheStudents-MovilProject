package com.example.thestudents.viewModels

import com.example.thestudents.data.Review
import com.example.thestudents.data.dtos.isReviewWithinLast24Hours
import com.example.thestudents.data.local.localStudentProvider
import com.example.thestudents.data.repository.AuthRepository
import com.example.thestudents.data.repository.ReviewRepository
import com.example.thestudents.ui.screens.map.ReviewsMapViewModel
import com.google.common.truth.Truth
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewsMapViewModelUnitTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var reviewRepository: ReviewRepository
    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: ReviewsMapViewModel

    private fun isoFromOffsetHours(offsetHours: Long): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        format.timeZone = TimeZone.getTimeZone("UTC")
        return format.format(Date(System.currentTimeMillis() + offsetHours * 3_600_000L))
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        reviewRepository = mockk()
        authRepository = mockk()
        viewModel = ReviewsMapViewModel(
            reviewRepository = reviewRepository,
            authRepository = authRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `isReviewWithinLast24Hours retorna true para resenas de las ultimas 24 horas y false para mas antiguas`() {
        val now = System.currentTimeMillis()
        val hace2Horas = isoFromOffsetHours(-2)
        val hace23Horas = isoFromOffsetHours(-23)
        val hace25Horas = isoFromOffsetHours(-25)
        val hace5Dias = isoFromOffsetHours(-120)

        Truth.assertThat(isReviewWithinLast24Hours(hace2Horas, now)).isTrue()
        Truth.assertThat(isReviewWithinLast24Hours(hace23Horas, now)).isTrue()
        Truth.assertThat(isReviewWithinLast24Hours(hace25Horas, now)).isFalse()
        Truth.assertThat(isReviewWithinLast24Hours(hace5Dias, now)).isFalse()
        Truth.assertThat(isReviewWithinLast24Hours(null, now)).isFalse()
        Truth.assertThat(isReviewWithinLast24Hours("", now)).isFalse()
    }

    @Test
    fun `loadRecentReviews carga las resenas recientes con ubicacion y actualiza el estado`() = runTest {
        val mockUser = mockk<FirebaseUser>()
        every { mockUser.uid } returns "user_1"
        every { authRepository.currentUser } returns mockUser

        val sampleReview = Review(
            id = "rev_1",
            reviewer = localStudentProvider.students[0],
            reviewedStudent = localStudentProvider.students[1],
            classReviewed = "Desarrollo Móvil",
            periodReviewed = "2026-2",
            content = "Excelente trabajo en equipo",
            time = "Hace 1 hora",
            likesCount = 3,
            disLikes = 0,
            rating = "5",
            comments = emptyList(),
            latitude = 4.6285,
            longitude = -74.0646
        )

        coEvery { reviewRepository.getRecentMapReviewsLive("user_1") } returns flowOf(listOf(sampleReview))

        viewModel.loadRecentReviews()

        val state = viewModel.uiState.value
        Truth.assertThat(state.isLoading).isFalse()
        Truth.assertThat(state.errorMessage).isNull()
        Truth.assertThat(state.recentReviews).hasSize(1)
        Truth.assertThat(state.recentReviews.first().id).isEqualTo("rev_1")
    }

    @Test
    fun `selectReview y updateAmbientLight actualizan el estado correctamente`() = runTest {
        val sampleReview = Review(
            id = "rev_2",
            reviewer = localStudentProvider.students[0],
            reviewedStudent = localStudentProvider.students[1],
            classReviewed = "ISIS1206",
            periodReviewed = "2026-1",
            content = "Muy puntual",
            time = "Hace 3 horas",
            likesCount = 1,
            disLikes = 0,
            rating = "4.5",
            comments = emptyList(),
            latitude = 4.6272,
            longitude = -74.0632
        )

        viewModel.selectReview(sampleReview)
        Truth.assertThat(viewModel.uiState.value.selectedReview?.id).isEqualTo("rev_2")

        viewModel.selectReview(null)
        Truth.assertThat(viewModel.uiState.value.selectedReview).isNull()

        // Baja luminosidad (< 2000 lux) activa el mapa oscuro
        viewModel.updateAmbientLight(500f)
        Truth.assertThat(viewModel.uiState.value.isDarkMap).isTrue()

        // Alta luminosidad (>= 2000 lux) activa el mapa claro
        viewModel.updateAmbientLight(3500f)
        Truth.assertThat(viewModel.uiState.value.isDarkMap).isFalse()
    }
}
