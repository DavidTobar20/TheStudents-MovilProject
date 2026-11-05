package com.example.thestudents.viewModels

import android.util.Log
import com.example.thestudents.data.repository.AuthRepository
import com.example.thestudents.data.repository.UserRepository
import com.example.thestudents.ui.screens.register.RegisterViewModel
import com.google.common.truth.Truth
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelUnitTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository
    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.w(any(), any<String>()) } returns 0
        every { Log.v(any(), any()) } returns 0

        Dispatchers.setMain(testDispatcher)
        authRepository = mockk()
        userRepository = mockk()
        viewModel = RegisterViewModel(
            authRepository = authRepository,
            userRepository = userRepository,
            ioDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
    }

    @Test
    fun `al actualizar campos del formulario, uiState se actualiza correctamente`() = runTest {
        viewModel.onNamesChange("Juan Perez")
        viewModel.onUsernameChange("juanperez")
        viewModel.onEmailChange("juan@example.com")
        viewModel.onPasswordChange("123456")
        viewModel.onConfirmPasswordChange("123456")
        viewModel.onProgramChange("Ingeniería de Sistemas")
        viewModel.onSemesterChange("5")
        viewModel.onBioChange("Hola")

        val state = viewModel.uiState.value
        Truth.assertThat(state.names).isEqualTo("Juan Perez")
        Truth.assertThat(state.username).isEqualTo("juanperez")
        Truth.assertThat(state.email).isEqualTo("juan@example.com")
        Truth.assertThat(state.password).isEqualTo("123456")
        Truth.assertThat(state.confirmPassword).isEqualTo("123456")
        Truth.assertThat(state.program).isEqualTo("Ingeniería de Sistemas")
        Truth.assertThat(state.semester).isEqualTo("5")
        Truth.assertThat(state.bio).isEqualTo("Hola")
    }

    @Test
    fun `al registrar con campos obligatorios vacios, muestra mensaje de error`() = runTest {
        viewModel.onNamesChange("")
        viewModel.onEmailChange("juan@example.com")

        viewModel.onRegisterClick()

        Truth.assertThat(viewModel.mostrarMensajeError.value).isTrue()
        Truth.assertThat(viewModel.errorMessage.value).isEqualTo("Todos los campos son obligatorios excepto bio")
        coVerify(exactly = 0) { authRepository.signUp(any(), any()) }
    }

    @Test
    fun `al registrar con contrasenas no coincidentes, muestra mensaje de error`() = runTest {
        viewModel.onNamesChange("Juan Perez")
        viewModel.onUsernameChange("juanperez")
        viewModel.onEmailChange("juan@example.com")
        viewModel.onPasswordChange("123456")
        viewModel.onConfirmPasswordChange("654321")
        viewModel.onProgramChange("Ingeniería de Sistemas")
        viewModel.onSemesterChange("5")

        viewModel.onRegisterClick()

        Truth.assertThat(viewModel.mostrarMensajeError.value).isTrue()
        Truth.assertThat(viewModel.errorMessage.value).isEqualTo("Las contraseñas no coinciden")
        coVerify(exactly = 0) { authRepository.signUp(any(), any()) }
    }

    @Test
    fun `al registrar exitosamente, actualiza estado y navega a home`() = runTest {
        val mockUser = mockk<FirebaseUser>()
        every { mockUser.uid } returns "user_123"
        coEvery { authRepository.signUp("juan@example.com", "123456") } returns Result.success(Unit)
        every { authRepository.currentUser } returns mockUser
        coEvery {
            userRepository.registerUser(
                name = "Juan Perez",
                username = "juanperez",
                program = "Ingeniería de Sistemas",
                semester = 5,
                bio = "Hola",
                userId = "user_123"
            )
        } returns Result.success(Unit)

        viewModel.onNamesChange("Juan Perez")
        viewModel.onUsernameChange("juanperez")
        viewModel.onEmailChange("juan@example.com")
        viewModel.onPasswordChange("123456")
        viewModel.onConfirmPasswordChange("123456")
        viewModel.onProgramChange("Ingeniería de Sistemas")
        viewModel.onSemesterChange("5")
        viewModel.onBioChange("Hola")

        viewModel.onRegisterClick()

        Truth.assertThat(viewModel.navigateToHome.value).isTrue()
        Truth.assertThat(viewModel.mostrarMensajeError.value).isFalse()
    }

    @Test
    fun `al fallar la autenticacion, muestra mensaje de error`() = runTest {
        coEvery { authRepository.signUp("juan@example.com", "123456") } returns Result.failure(Exception("Email ya registrado"))

        viewModel.onNamesChange("Juan Perez")
        viewModel.onUsernameChange("juanperez")
        viewModel.onEmailChange("juan@example.com")
        viewModel.onPasswordChange("123456")
        viewModel.onConfirmPasswordChange("123456")
        viewModel.onProgramChange("Ingeniería de Sistemas")
        viewModel.onSemesterChange("5")

        viewModel.onRegisterClick()

        Truth.assertThat(viewModel.mostrarMensajeError.value).isTrue()
        Truth.assertThat(viewModel.errorMessage.value).isEqualTo("Email ya registrado")
        Truth.assertThat(viewModel.navigateToHome.value).isFalse()
    }

    @Test
    fun `al fallar el registro en base de datos, muestra mensaje de error`() = runTest {
        val mockUser = mockk<FirebaseUser>()
        every { mockUser.uid } returns "user_123"
        coEvery { authRepository.signUp("juan@example.com", "123456") } returns Result.success(Unit)
        every { authRepository.currentUser } returns mockUser
        coEvery {
            userRepository.registerUser(any(), any(), any(), any(), any(), any())
        } returns Result.failure(Exception("Error de conexion en Firestore"))

        viewModel.onNamesChange("Juan Perez")
        viewModel.onUsernameChange("juanperez")
        viewModel.onEmailChange("juan@example.com")
        viewModel.onPasswordChange("123456")
        viewModel.onConfirmPasswordChange("123456")
        viewModel.onProgramChange("Ingeniería de Sistemas")
        viewModel.onSemesterChange("5")

        viewModel.onRegisterClick()

        Truth.assertThat(viewModel.mostrarMensajeError.value).isTrue()
        Truth.assertThat(viewModel.errorMessage.value).isEqualTo("Error de conexion en Firestore")
        Truth.assertThat(viewModel.navigateToHome.value).isFalse()
    }

    @Test
    fun `onErrorDismiss oculta el mensaje de error`() = runTest {
        viewModel.onNamesChange("")
        viewModel.onRegisterClick() // Muestra error por vacíos
        Truth.assertThat(viewModel.mostrarMensajeError.value).isTrue()

        viewModel.onErrorDismiss()

        Truth.assertThat(viewModel.mostrarMensajeError.value).isFalse()
    }

    @Test
    fun `mostrarEsconderPassword alterna la visibilidad de la contrasena`() = runTest {
        Truth.assertThat(viewModel.mostrarPassword.value).isFalse()

        viewModel.mostrarEsconderPassword()

        Truth.assertThat(viewModel.mostrarPassword.value).isTrue()
    }
}
