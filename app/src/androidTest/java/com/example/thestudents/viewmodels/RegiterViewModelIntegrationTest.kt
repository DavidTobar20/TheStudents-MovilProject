package com.example.thestudents.viewmodels

import com.example.thestudents.data.datasource.AuthRemoteDataSource
import com.example.thestudents.data.datasource.impl.firestore.ReviewFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.thestudents.data.repository.AuthRepository
import com.example.thestudents.data.repository.UserRepository
import com.example.thestudents.ui.screens.register.RegisterViewModel
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Before
import org.junit.Test

class RegiterViewModelIntegrationTest {

    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setup() {
        try {
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
        } catch (e: Exception) {
        }

        val authRepository = AuthRepository(AuthRemoteDataSource(Firebase.auth))
        val userRepository = UserRepository(
            UserFirestoreDataSourceImpl(Firebase.firestore),
            ReviewFirestoreDataSourceImpl(Firebase.firestore),
            AuthRemoteDataSource(Firebase.auth)
        )

        val scheduler = TestCoroutineScheduler()
        val dispatcher = StandardTestDispatcher(scheduler)

        Dispatchers.setMain(dispatcher)

        viewModel = RegisterViewModel(authRepository, userRepository,dispatcher)
    }


    @Test
    fun register_emptyFields_showsError() = runTest {
        // Arrange: Dejamos campos vacíos
        viewModel.onNamesChange("")
        viewModel.onEmailChange("test@example.com")

        // Act
        viewModel.onRegisterClick()

        // Assert
        Truth.assertThat(viewModel.mostrarMensajeError.value).isTrue()
        Truth.assertThat(viewModel.errorMessage.value).isEqualTo("Todos los campos son obligatorios excepto bio")
        Truth.assertThat(viewModel.navigateToHome.value).isFalse()
    }

    @Test
    fun register_passwordsDoNotMatch_showsError() = runTest {
        // Arrange
        viewModel.onNamesChange("Juan Perez")
        viewModel.onUsernameChange("juanperez")
        viewModel.onEmailChange("juan@example.com")
        viewModel.onPasswordChange("12345678")
        viewModel.onConfirmPasswordChange("87654321")
        viewModel.onProgramChange("Ingeniería de Sistemas")
        viewModel.onSemesterChange("5")

        // Act
        viewModel.onRegisterClick()

        // Assert
        Truth.assertThat(viewModel.mostrarMensajeError.value).isTrue()
        Truth.assertThat(viewModel.errorMessage.value).isEqualTo("Las contraseñas no coinciden")
        Truth.assertThat(viewModel.navigateToHome.value).isFalse()
    }

    @After
    fun tearDown() = runTest {
        val user = Firebase.auth.currentUser
        if (user != null) {
            val userId = user.uid
            try {
                Firebase.firestore.collection("users").document(userId).delete().await()
            } catch (e: Exception) {
            }
            try {
                user.delete().await()
            } catch (e: Exception) {
            }
            Firebase.auth.signOut()
        }
    }
}
