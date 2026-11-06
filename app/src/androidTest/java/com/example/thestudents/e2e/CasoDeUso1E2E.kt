package com.example.thestudents.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.example.thestudents.MainActivity
import com.example.thestudents.data.datasource.AuthRemoteDataSource
import com.example.thestudents.data.datasource.impl.firestore.ReviewFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.thestudents.data.repository.AuthRepository
import com.example.thestudents.data.repository.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CasoDeUso1E2E {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        hiltRule.inject()
        try {
            Firebase.auth.useEmulator("10.0.2.2", 9099) // auth emulator
            Firebase.firestore.useEmulator("10.0.2.2", 8080) // firestore emulator
        } catch (e: Exception) {
        }

        val authRemoteDataSource = AuthRemoteDataSource(Firebase.auth)
        val userDatasource = UserFirestoreDataSourceImpl(Firebase.firestore)
        val reviewRemoteDataSource = ReviewFirestoreDataSourceImpl(Firebase.firestore)

        userRepository = UserRepository(userDatasource, reviewRemoteDataSource, authRemoteDataSource)
        authRepository = AuthRepository(authRemoteDataSource)
    }

    @After
    fun cleanDatabase() = runTest {
        val user = Firebase.auth.currentUser
        user?.delete()?.await()
        Firebase.auth.signOut()
    }

    @Test
    fun navigate_fromStart_toLogin() {
        // 1. Verifica que la pantalla de Login aparece por defecto al iniciar
        composeRule.onNodeWithTag("loginScreen").assertIsDisplayed()
        composeRule.onNodeWithTag("btnLogin").assertIsDisplayed()
    }

    @Test
    fun navigate_fromStart_toRegister() {
        // 2. Click en el botón/enlace de registro
        composeRule.onNodeWithTag("btnRegister").performClick()

        // Verifica que la pantalla de Register aparece
        composeRule.onNodeWithTag("registerScreen").assertIsDisplayed()
    }

    @Test
    fun registerUser_shortPassword_showMessage() {
        // 3. Intento de registro con contraseña corta (12345)
        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("nameField").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("nameField").performTextInput("Juan Sebastian")
        composeRule.onNodeWithTag("userNameField").performTextInput("Juan Sebastian")
        composeRule.onNodeWithTag("paisField").performTextInput("Colombia")
        composeRule.onNodeWithTag("semesterField").performTextInput("5")
        composeRule.onNodeWithTag("bioField").performTextInput("Estudiante")
        composeRule.onNodeWithTag("emailField").performTextInput("admin@admin.com")
        composeRule.onNodeWithTag("passwordField").performTextInput("12345")
        composeRule.onNodeWithTag("confirmPasswordField").performTextInput("12345")

        // Aceptar términos y condiciones para habilitar el botón de registro
        composeRule.onNodeWithTag("termsCheckbox").performClick()

        composeRule.onNodeWithTag("btnRegister").performClick()

        // Verificar mensaje de error en el AlertDialog emergente (useUnmergedTree = true)
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("errorMessage", useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("errorMessage", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun registerUser_allValidsInputs_navigateHome() {
        // 4. Registro con datos válidos e ingreso a Home
        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("nameField").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("nameField").performTextInput("Juan Sebastian")
        composeRule.onNodeWithTag("userNameField").performTextInput("Juan Sebastian")
        composeRule.onNodeWithTag("paisField").performTextInput("Colombia")
        composeRule.onNodeWithTag("semesterField").performTextInput("5")
        composeRule.onNodeWithTag("bioField").performTextInput("Estudiante")
        composeRule.onNodeWithTag("emailField").performTextInput("juan@admin.com")
        composeRule.onNodeWithTag("passwordField").performTextInput("123456")
        composeRule.onNodeWithTag("confirmPasswordField").performTextInput("123456")

        // Aceptar términos y condiciones
        composeRule.onNodeWithTag("termsCheckbox").performClick()

        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("homeScreen").assertIsDisplayed()
    }

    @Test
    fun registerUser_usedEmail_showMessage() {
        // 5. Registro con correo duplicado
        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("nameField").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("nameField").performTextInput("Juan Sebastian")
        composeRule.onNodeWithTag("userNameField").performTextInput("Juan Sebastian")
        composeRule.onNodeWithTag("paisField").performTextInput("Colombia")
        composeRule.onNodeWithTag("semesterField").performTextInput("5")
        composeRule.onNodeWithTag("bioField").performTextInput("Estudiante")
        composeRule.onNodeWithTag("emailField").performTextInput("admin@admin.com")
        composeRule.onNodeWithTag("passwordField").performTextInput("123456")
        composeRule.onNodeWithTag("confirmPasswordField").performTextInput("123456")

        // Aceptar términos y condiciones
        composeRule.onNodeWithTag("termsCheckbox").performClick()

        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("errorMessage", useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("errorMessage", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun navigate_fromHome_toPublicationDetail() {
        // 6. Registrar usuario, entrar a Home y navegar al detalle de la primera publicación
        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("nameField").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("nameField").performTextInput("Test Nav")
        composeRule.onNodeWithTag("userNameField").performTextInput("testnav")
        composeRule.onNodeWithTag("paisField").performTextInput("Colombia")
        composeRule.onNodeWithTag("semesterField").performTextInput("5")
        composeRule.onNodeWithTag("bioField").performTextInput("Estudiante")
        composeRule.onNodeWithTag("emailField").performTextInput("nav_test@admin.com")
        composeRule.onNodeWithTag("passwordField").performTextInput("123456")
        composeRule.onNodeWithTag("confirmPasswordField").performTextInput("123456")

        composeRule.onNodeWithTag("termsCheckbox").performClick()
        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("reviewItem").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithTag("reviewItem")[0].performClick()

        // Verificar que estamos en la pantalla de detalle adecuada (commentsReviewScreen)
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("commentsReviewScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("commentsReviewScreen").assertIsDisplayed()
    }

    @Test
    fun verify_publicationDetail_matchesFirebase() = runTest {
        // 7. Registrar usuario, entrar a Home, abrir publicación y comparar campos con la BD (Firebase)
        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("nameField").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("nameField").performTextInput("Firebase Compare")
        composeRule.onNodeWithTag("userNameField").performTextInput("fbcompare")
        composeRule.onNodeWithTag("paisField").performTextInput("Colombia")
        composeRule.onNodeWithTag("semesterField").performTextInput("5")
        composeRule.onNodeWithTag("bioField").performTextInput("Estudiante")
        composeRule.onNodeWithTag("emailField").performTextInput("fb_compare@admin.com")
        composeRule.onNodeWithTag("passwordField").performTextInput("123456")
        composeRule.onNodeWithTag("confirmPasswordField").performTextInput("123456")

        composeRule.onNodeWithTag("termsCheckbox").performClick()
        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("reviewItem").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithTag("reviewItem")[0].performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("commentsReviewScreen").fetchSemanticsNodes().isNotEmpty()
        }

        // Consultar datos directamente de la base de datos (Firebase Firestore)
        val reviewDataSource = ReviewFirestoreDataSourceImpl(Firebase.firestore)
        val reviews = reviewDataSource.getAllReviewsExcludingStudentId("")
        assertTrue("Debe haber reseñas en Firebase", reviews.isNotEmpty())
        val firstReview = reviews.first()
        assertNotNull("El ID de la reseña en Firebase no debe ser nulo", firstReview.id)

        // Comparar campos usando asserts explícitos con los datos de Firebase
        val authorName = firstReview.autor?.nombre
        if (!authorName.isNullOrBlank()) {
            composeRule.onNodeWithTag("reviewAuthorName").assertTextEquals(authorName)
        }
        val targetName = firstReview.resenado?.nombre
        if (!targetName.isNullOrBlank()) {
            composeRule.onNodeWithTag("reviewTargetName").assertTextEquals(targetName)
        }
        val content = firstReview.contenido
        if (!content.isNullOrBlank()) {
            composeRule.onNodeWithTag("reviewContent").assertTextContains(content, substring = true)
        }

        val dbLikes = firstReview.likesCount ?: 0
        val dbDislikes = firstReview.disLikes ?: 0

        composeRule.onNodeWithTag("reviewLikesCount").assertTextEquals(dbLikes.toString())
        composeRule.onNodeWithTag("reviewDislikesCount").assertTextEquals(dbDislikes.toString())
    }

    @Test
    fun publication_likeComment_increasesCount() {
        // 8. Guardar cantidad de likes inicial, dar like y verificar que aumente en 1
        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("nameField").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("nameField").performTextInput("Like Test")
        composeRule.onNodeWithTag("userNameField").performTextInput("liketest")
        composeRule.onNodeWithTag("paisField").performTextInput("Colombia")
        composeRule.onNodeWithTag("semesterField").performTextInput("5")
        composeRule.onNodeWithTag("bioField").performTextInput("Estudiante")
        composeRule.onNodeWithTag("emailField").performTextInput("like_test@admin.com")
        composeRule.onNodeWithTag("passwordField").performTextInput("123456")
        composeRule.onNodeWithTag("confirmPasswordField").performTextInput("123456")

        composeRule.onNodeWithTag("termsCheckbox").performClick()
        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("reviewItem").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithTag("reviewItem")[0].performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("commentsReviewScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("commentLikeButton").fetchSemanticsNodes().isNotEmpty()
        }

        val initialLikesText = composeRule.onAllNodesWithTag("commentLikeCount")[0]
            .fetchSemanticsNode().config[androidx.compose.ui.semantics.SemanticsProperties.Text].first().text
        val initialLikes = initialLikesText.toIntOrNull() ?: 0

        // Dar like
        composeRule.onAllNodesWithTag("commentLikeButton")[0].performClick()

        // Verificar explícitamente que aumente en 1
        composeRule.waitUntil(timeoutMillis = 3000) {
            val updatedText = composeRule.onAllNodesWithTag("commentLikeCount")[0]
                .fetchSemanticsNode().config[androidx.compose.ui.semantics.SemanticsProperties.Text].first().text
            (updatedText.toIntOrNull() ?: 0) == initialLikes + 1
        }
        composeRule.onAllNodesWithTag("commentLikeCount")[0].assertTextEquals((initialLikes + 1).toString())
    }

    @Test
    fun publication_unlikeComment_decreasesCount() {
        // 9. Volver a Home, seleccionar la misma reseña, quitar el like y verificar que disminuya en 1
        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("nameField").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("nameField").performTextInput("Unlike Test")
        composeRule.onNodeWithTag("userNameField").performTextInput("unliketest")
        composeRule.onNodeWithTag("paisField").performTextInput("Colombia")
        composeRule.onNodeWithTag("semesterField").performTextInput("5")
        composeRule.onNodeWithTag("bioField").performTextInput("Estudiante")
        composeRule.onNodeWithTag("emailField").performTextInput("unlike_test@admin.com")
        composeRule.onNodeWithTag("passwordField").performTextInput("123456")
        composeRule.onNodeWithTag("confirmPasswordField").performTextInput("123456")

        composeRule.onNodeWithTag("termsCheckbox").performClick()
        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("reviewItem").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithTag("reviewItem")[0].performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("commentsReviewScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("commentLikeButton").fetchSemanticsNodes().isNotEmpty()
        }

        val initialLikesText = composeRule.onAllNodesWithTag("commentLikeCount")[0]
            .fetchSemanticsNode().config[androidx.compose.ui.semantics.SemanticsProperties.Text].first().text
        val initialLikes = initialLikesText.toIntOrNull() ?: 0

        // Dar like (aumenta a initial + 1)
        composeRule.onAllNodesWithTag("commentLikeButton")[0].performClick()
        composeRule.waitUntil(timeoutMillis = 3000) {
            val updatedText = composeRule.onAllNodesWithTag("commentLikeCount")[0]
                .fetchSemanticsNode().config[androidx.compose.ui.semantics.SemanticsProperties.Text].first().text
            (updatedText.toIntOrNull() ?: 0) == initialLikes + 1
        }
        composeRule.onAllNodesWithTag("commentLikeCount")[0].assertTextEquals((initialLikes + 1).toString())

        // Ir atrás a Home
        composeRule.activity.onBackPressedDispatcher.onBackPressed()
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }

        // Volver a seleccionar la misma reseña
        composeRule.onAllNodesWithTag("reviewItem")[0].performClick()
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("commentsReviewScreen").fetchSemanticsNodes().isNotEmpty()
        }

        // Quitar el like (disminuye en 1 volviendo al valor inicial)
        composeRule.onAllNodesWithTag("commentLikeButton")[0].performClick()
        composeRule.waitUntil(timeoutMillis = 3000) {
            val finalText = composeRule.onAllNodesWithTag("commentLikeCount")[0]
                .fetchSemanticsNode().config[androidx.compose.ui.semantics.SemanticsProperties.Text].first().text
            (finalText.toIntOrNull() ?: 0) == initialLikes
        }
        composeRule.onAllNodesWithTag("commentLikeCount")[0].assertTextEquals(initialLikes.toString())
    }

    @Test
    fun casoDeUso1_completoE2E() = runTest {
        // 10. Flujo completo E2E uniendo todos los pasos anteriores
        // A. Usuario nuevo ingresa a registro y se registra con contraseña corta (1234) -> Verifica error
        composeRule.onNodeWithTag("loginScreen").assertIsDisplayed()
        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("nameField").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("nameField").performTextInput("Carlos E2E")
        composeRule.onNodeWithTag("userNameField").performTextInput("carlostest")
        composeRule.onNodeWithTag("paisField").performTextInput("Colombia")
        composeRule.onNodeWithTag("semesterField").performTextInput("5")
        composeRule.onNodeWithTag("bioField").performTextInput("Ingeniería de Sistemas")
        composeRule.onNodeWithTag("emailField").performTextInput("carlos.e2e@student.com")
        composeRule.onNodeWithTag("passwordField").performTextInput("1234")
        composeRule.onNodeWithTag("confirmPasswordField").performTextInput("1234")

        composeRule.onNodeWithTag("termsCheckbox").performClick()
        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("errorMessage", useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("errorMessage", useUnmergedTree = true).assertIsDisplayed()

        // B. El usuario corrige la contraseña a 123456, ingresa a la aplicación (Home)
        composeRule.onNodeWithTag("passwordField").performTextClearance()
        composeRule.onNodeWithTag("passwordField").performTextInput("123456")
        composeRule.onNodeWithTag("confirmPasswordField").performTextClearance()
        composeRule.onNodeWithTag("confirmPasswordField").performTextInput("123456")
        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("homeScreen").assertIsDisplayed()

        // C. El usuario ingresa a la primera publicación, consulta la BD y compara explícitamente los campos con asserts directos
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("reviewItem").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithTag("reviewItem")[0].performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("commentsReviewScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("commentsReviewScreen").assertIsDisplayed()

        val reviewDataSource = ReviewFirestoreDataSourceImpl(Firebase.firestore)
        val firebaseReviews = reviewDataSource.getAllReviewsExcludingStudentId("")
        assertTrue("Debe existir al menos una reseña en Firebase", firebaseReviews.isNotEmpty())
        val firstReview = firebaseReviews.first()
        assertNotNull("La reseña en Firebase tiene ID válido", firstReview.id)

        val authorName = firstReview.autor?.nombre
        if (!authorName.isNullOrBlank()) {
            composeRule.onNodeWithTag("reviewAuthorName").assertTextEquals(authorName)
        }
        val targetName = firstReview.resenado?.nombre
        if (!targetName.isNullOrBlank()) {
            composeRule.onNodeWithTag("reviewTargetName").assertTextEquals(targetName)
        }
        val content = firstReview.contenido
        if (!content.isNullOrBlank()) {
            composeRule.onNodeWithTag("reviewContent").assertTextContains(content, substring = true)
        }

        val dbLikes = firstReview.likesCount ?: 0
        val dbDislikes = firstReview.disLikes ?: 0

        composeRule.onNodeWithTag("reviewLikesCount").assertTextEquals(dbLikes.toString())
        composeRule.onNodeWithTag("reviewDislikesCount").assertTextEquals(dbDislikes.toString())

        // D. Va a la sección de reviews/comentarios, guarda la cantidad de likes iniciales, da like al primer comentario y verifica explícitamente que aumente en 1
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("commentLikeButton").fetchSemanticsNodes().isNotEmpty()
        }
        val initialLikesText = composeRule.onAllNodesWithTag("commentLikeCount")[0]
            .fetchSemanticsNode().config[androidx.compose.ui.semantics.SemanticsProperties.Text].first().text
        val initialLikes = initialLikesText.toIntOrNull() ?: 0

        composeRule.onAllNodesWithTag("commentLikeButton")[0].performClick()

        composeRule.waitUntil(timeoutMillis = 3000) {
            val updatedText = composeRule.onAllNodesWithTag("commentLikeCount")[0]
                .fetchSemanticsNode().config[androidx.compose.ui.semantics.SemanticsProperties.Text].first().text
            (updatedText.toIntOrNull() ?: 0) == initialLikes + 1
        }
        composeRule.onAllNodesWithTag("commentLikeCount")[0].assertTextEquals((initialLikes + 1).toString())

        // E. El usuario va atrás (Home), vuelve a seleccionar la publicación, quita el like y verifica explícitamente que disminuya en 1 (vuelva al valor inicial)
        composeRule.activity.onBackPressedDispatcher.onBackPressed()
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onAllNodesWithTag("reviewItem")[0].performClick()
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("commentsReviewScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onAllNodesWithTag("commentLikeButton")[0].performClick()
        composeRule.waitUntil(timeoutMillis = 3000) {
            val finalLikeCountText = composeRule.onAllNodesWithTag("commentLikeCount")[0]
                .fetchSemanticsNode().config[androidx.compose.ui.semantics.SemanticsProperties.Text].first().text
            (finalLikeCountText.toIntOrNull() ?: 0) == initialLikes
        }
        composeRule.onAllNodesWithTag("commentLikeCount")[0].assertTextEquals(initialLikes.toString())
    }
}
