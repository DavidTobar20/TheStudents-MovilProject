package com.example.thestudents.e2e

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.thestudents.MainActivity
import com.example.thestudents.data.datasource.AuthRemoteDataSource
import com.example.thestudents.data.datasource.impl.firestore.ReviewFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.thestudents.data.local.SeedHelper
import com.example.thestudents.data.dtos.RegisterUserDto
import com.example.thestudents.data.repository.AuthRepository
import com.example.thestudents.data.repository.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CasoDeUso2E2E {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository
    private lateinit var userDatasource: UserFirestoreDataSourceImpl

    private val email = "seguidor@admin.com"
    private val password = "123456"

    @Before
    fun setup() {
        hiltRule.inject()
        try {
            Firebase.auth.useEmulator("10.0.2.2", 9099) // auth emulator
            Firebase.firestore.useEmulator("10.0.2.2", 8080) // firestore emulator
        } catch (e: Exception) {
        }

        val authRemoteDataSource = AuthRemoteDataSource(Firebase.auth)
        userDatasource = UserFirestoreDataSourceImpl(Firebase.firestore)
        val reviewRemoteDataSource = ReviewFirestoreDataSourceImpl(Firebase.firestore)

        userRepository = UserRepository(userDatasource, reviewRemoteDataSource, authRemoteDataSource)
        authRepository = AuthRepository(authRemoteDataSource)

        // Usuario ya registrado (Auth + documento en "users") que todavía no sigue a nadie
        runBlocking {
            val result = authRepository.signUp(email, password)
            val uid = authRepository.currentUser?.uid
            if (result.isSuccess && uid != null) {
                userDatasource.registerUser(
                    RegisterUserDto(
                        nombre = "Usuario Seguidor",
                        nombre_usuario = "seguidor",
                        carrera = "Ingeniería de Sistemas",
                        semestre = 5,
                        biografia = "Estudiante",
                        fcmToken = ""
                    ),
                    uid
                )
            } else {
                authRepository.signIn(email, password)
            }

            // Datos de prueba: usuario autor, reseña y comentario (igual que el SeedHelper del profe)
            SeedHelper.seedUsersAndReviews(Firebase.firestore)

            authRepository.signOut()
        }
    }

    @After
    fun cleanDatabase() = runTest {
        // Se borra el usuario para que en la siguiente prueba vuelva a empezar sin seguir a nadie
        val user = Firebase.auth.currentUser
        user?.delete()?.await()
        Firebase.auth.signOut()
    }

    @Test
    fun login_registeredUser_navigateHome() {
        // 1. Un usuario ya registrado realiza login y llega a Home
        composeRule.onNodeWithTag("loginScreen").assertIsDisplayed()
        composeRule.onNodeWithTag("emailField").performTextInput(email)
        composeRule.onNodeWithTag("passwordField").performTextInput(password)
        composeRule.onNodeWithTag("btnLogin").performClick()

        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithTag("homeScreen").onFirst().assertIsDisplayed()
    }

    @Test
    fun navigate_fromHome_toUserProfile() {
        // 2. Login, y desde Home va al perfil del autor de la primera publicación
        composeRule.onNodeWithTag("emailField").performTextInput(email)
        composeRule.onNodeWithTag("passwordField").performTextInput(password)
        composeRule.onNodeWithTag("btnLogin").performClick()

        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("reviewItem").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithTag("reviewAuthorName", useUnmergedTree = true)[0].performClick()

        // Verificar que estamos en la pantalla de perfil del usuario (studentDetailScreen)
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("studentDetailScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("studentDetailScreen").assertIsDisplayed()
    }

    @Test
    fun verify_userProfile_matchesFirebase() = runTest {
        // 3. Login, ir al perfil del usuario y comparar su información con la BD (Firebase)
        composeRule.onNodeWithTag("emailField").performTextInput(email)
        composeRule.onNodeWithTag("passwordField").performTextInput(password)
        composeRule.onNodeWithTag("btnLogin").performClick()

        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("reviewItem").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithTag("reviewAuthorName", useUnmergedTree = true)[0].performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("studentDetailScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("profileName").fetchSemanticsNodes().isNotEmpty()
        }

        // Consultar datos directamente de la base de datos (Firebase Firestore)
        val currentUserId = Firebase.auth.currentUser?.uid
        assertNotNull("El usuario debe haber iniciado sesión", currentUserId)

        val reviewDataSource = ReviewFirestoreDataSourceImpl(Firebase.firestore)
        val reviews = reviewDataSource.getAllReviewsExcludingStudentId(currentUserId!!)
        assertTrue("Debe haber reseñas en Firebase", reviews.isNotEmpty())
        val authorId = reviews.first().autor?.id
        assertNotNull("La primera reseña debe tener autor", authorId)

        val dbUser = userDatasource.getUserById(authorId!!)
        assertNotNull("El usuario debe existir en Firebase", dbUser)

        // Comparar campos usando asserts explícitos con los datos de Firebase
        composeRule.onNodeWithTag("profileName").assertTextEquals(dbUser!!.nombre.orEmpty())
        if (!dbUser.nombre_usuario.isNullOrBlank()) {
            composeRule.onNodeWithTag("profileUserName").assertTextEquals("@${dbUser.nombre_usuario}")
        }
    }

    @Test
    fun userProfile_follow_increasesFollowers() {
        // 4. Login, ir al perfil del usuario, darle follow y verificar que aumentan sus seguidores
        composeRule.onNodeWithTag("emailField").performTextInput(email)
        composeRule.onNodeWithTag("passwordField").performTextInput(password)
        composeRule.onNodeWithTag("btnLogin").performClick()

        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("reviewItem").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithTag("reviewAuthorName", useUnmergedTree = true)[0].performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("studentDetailScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("btnFollow").fetchSemanticsNodes().isNotEmpty()
        }

        // Todavía no lo sigue
        composeRule.onNodeWithTag("btnFollow").assertTextEquals("Seguir")

        // followersCount está dentro de un elemento clickeable: useUnmergedTree = true
        val initialFollowersText = composeRule.onNodeWithTag("followersCount", useUnmergedTree = true)
            .fetchSemanticsNode().config[SemanticsProperties.Text].first().text
        val initialFollowers = initialFollowersText.toIntOrNull() ?: 0

        // Dar follow
        composeRule.onNodeWithTag("btnFollow").performClick()

        // Verificar explícitamente que aumente en 1
        composeRule.waitUntil(timeoutMillis = 5000) {
            val updatedText = composeRule.onNodeWithTag("followersCount", useUnmergedTree = true)
                .fetchSemanticsNode().config[SemanticsProperties.Text].first().text
            (updatedText.toIntOrNull() ?: 0) == initialFollowers + 1
        }
        composeRule.onNodeWithTag("followersCount", useUnmergedTree = true)
            .assertTextEquals((initialFollowers + 1).toString())
    }

    @Test
    fun home_followedTab_showsFollowedUserReview() = runTest {
        // 5. Login, seguir al usuario, volver a Home y verificar en "Seguidos" que aparezca una publicación suya
        composeRule.onNodeWithTag("emailField").performTextInput(email)
        composeRule.onNodeWithTag("passwordField").performTextInput(password)
        composeRule.onNodeWithTag("btnLogin").performClick()

        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("reviewItem").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithTag("reviewAuthorName", useUnmergedTree = true)[0].performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("studentDetailScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("btnFollow").fetchSemanticsNodes().isNotEmpty()
        }

        // Dar follow y esperar a que se guarde (el botón cambia a "Dejar de seguir")
        composeRule.onNodeWithTag("btnFollow").performClick()
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithText("Seguir").fetchSemanticsNodes().isEmpty()
        }

        // Nombre del autor que se acaba de seguir (primera reseña en la BD)
        val currentUserId = Firebase.auth.currentUser?.uid
        assertNotNull("El usuario debe haber iniciado sesión", currentUserId)
        val reviewDataSource = ReviewFirestoreDataSourceImpl(Firebase.firestore)
        val followedAuthorName = reviewDataSource.getAllReviewsExcludingStudentId(currentUserId!!)
            .first().autor?.nombre.orEmpty()

        // Ir atrás a Home (la navegación debe hacerse en el hilo de la UI)
        composeRule.runOnUiThread {
            composeRule.activity.onBackPressedDispatcher.onBackPressed()
        }
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }

        // Ir a la sección de publicaciones de seguidos
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithText("Seguidos").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Seguidos").performClick()

        // Verificar que aparezca al menos una publicación del usuario que acabó de seguir
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("reviewAuthorName", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .any { node ->
                    node.config.getOrNull(SemanticsProperties.Text)?.firstOrNull()?.text == followedAuthorName
                }
        }
        composeRule.onAllNodesWithTag("reviewItem").onFirst().assertIsDisplayed()
    }

    @Test
    fun casoDeUso2_completoE2E() = runTest {
        // 6. Flujo completo E2E uniendo todos los pasos anteriores
        // A. Un usuario ya registrado realiza login en la aplicación
        composeRule.onNodeWithTag("loginScreen").assertIsDisplayed()
        composeRule.onNodeWithTag("emailField").performTextInput(email)
        composeRule.onNodeWithTag("passwordField").performTextInput(password)
        composeRule.onNodeWithTag("btnLogin").performClick()

        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithTag("homeScreen").onFirst().assertIsDisplayed()

        // B. Va al perfil de un usuario: el autor de la primera publicación
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("reviewItem").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithTag("reviewAuthorName", useUnmergedTree = true)[0].performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("studentDetailScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("profileName").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("studentDetailScreen").assertIsDisplayed()

        // C. Verificar que la información del usuario sea correcta (comparando con la BD)
        val currentUserId = Firebase.auth.currentUser?.uid
        assertNotNull("El usuario debe haber iniciado sesión", currentUserId)

        val reviewDataSource = ReviewFirestoreDataSourceImpl(Firebase.firestore)
        val reviews = reviewDataSource.getAllReviewsExcludingStudentId(currentUserId!!)
        assertTrue("Debe haber reseñas en Firebase", reviews.isNotEmpty())
        val firstReview = reviews.first()
        val followedUserId = firstReview.autor?.id
        assertNotNull("La primera reseña debe tener autor", followedUserId)

        val dbUser = userDatasource.getUserById(followedUserId!!)
        assertNotNull("El usuario a seguir debe existir en Firebase", dbUser)

        composeRule.onNodeWithTag("profileName").assertTextEquals(dbUser!!.nombre.orEmpty())
        if (!dbUser.nombre_usuario.isNullOrBlank()) {
            composeRule.onNodeWithTag("profileUserName").assertTextEquals("@${dbUser.nombre_usuario}")
        }

        // D. Le da follow y se verifica que aumenta la cantidad de seguidores
        composeRule.onNodeWithTag("btnFollow").assertTextEquals("Seguir")

        val initialFollowersText = composeRule.onNodeWithTag("followersCount", useUnmergedTree = true)
            .fetchSemanticsNode().config[SemanticsProperties.Text].first().text
        val initialFollowers = initialFollowersText.toIntOrNull() ?: 0

        composeRule.onNodeWithTag("btnFollow").performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            val updatedText = composeRule.onNodeWithTag("followersCount", useUnmergedTree = true)
                .fetchSemanticsNode().config[SemanticsProperties.Text].first().text
            (updatedText.toIntOrNull() ?: 0) == initialFollowers + 1
        }
        composeRule.onNodeWithTag("followersCount", useUnmergedTree = true)
            .assertTextEquals((initialFollowers + 1).toString())

        // E. Vuelve al home, va a la sección de seguidos y verifica que aparezca al menos una publicación del usuario seguido
        composeRule.runOnUiThread {
            composeRule.activity.onBackPressedDispatcher.onBackPressed()
        }
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithText("Seguidos").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Seguidos").performClick()
        Thread.sleep(5000);
        val followedAuthorName = firstReview.autor?.nombre.orEmpty()
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("reviewAuthorName", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .any { node ->
                    node.config.getOrNull(SemanticsProperties.Text)?.firstOrNull()?.text == followedAuthorName
                }
        }
        composeRule.onAllNodesWithTag("reviewItem").onFirst().assertIsDisplayed()
    }
}
