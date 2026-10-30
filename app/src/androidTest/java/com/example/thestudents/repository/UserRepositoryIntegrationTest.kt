package com.example.thestudents.repository

import android.util.Log
import com.example.thestudents.data.datasource.AuthRemoteDataSource
import com.example.thestudents.data.datasource.impl.firestore.ReviewFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.thestudents.data.dtos.CreateReviewGenericDto
import com.example.thestudents.data.dtos.UserProfileFirestoreDto
import com.example.thestudents.data.repository.UserRepository
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserRepositoryIntegrationTest {

    private val db =  Firebase.firestore
    private val auth = Firebase.auth


    private fun generateUser(i: Int): UserProfileFirestoreDto = UserProfileFirestoreDto(
        id = "user_$i",
        nombre_usuario = "usuario_$i",
        nombre = "Nombre $i",
        biografia = "Biografia del usuario $i",
        foto_url = "https://picsum.photos/200/200?random=$i",
        color_perfil = "#4A90E2",
        carrera = "Ingeniería de Sistemas",
        semestre = (i % 10) + 1,
        resenas_recibidas = emptyList(),
        resenas_creadas = emptyList(),
        followersCount = i * 10,
        followingCount = i * 5,
        followed = false
    )
    private lateinit var userRepository: UserRepository
    private lateinit var reviewDataSource: ReviewFirestoreDataSourceImpl

    @Before
    fun setUp() = runTest {
        try {
            db.useEmulator("10.0.2.2", 8080)
        }catch (e: Exception) {
        }
        try {
            auth.useEmulator("10.0.2.2", 9099)
        }catch (e: Exception) {
        }

        reviewDataSource = ReviewFirestoreDataSourceImpl(db)

        userRepository = UserRepository(
            UserFirestoreDataSourceImpl(db),
            reviewDataSource,
            AuthRemoteDataSource(auth)
        )


        //replicables que no cambie luego de hacerlas varias veces
        //autonomas

        val batch = db.batch()
        repeat(10){ i ->
            val user = generateUser(i)
            batch.set(db.collection("users").document(user.id!!), user)
        }
        Log.d("TAG","Antes de commit")
        batch.commit().await()
        Log.d("TAG","Despues de commit")
    }


    @Test
    fun getUserById_validId_returnsCorrectStudent() = runTest {
        //arrange
        val id = "user_9"
        val expectedName = "Nombre 9"
        //act
        val result = userRepository.getUserById(id)
        //assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.id).isEqualTo(id)
        Truth.assertThat(result.getOrNull()?.name).isEqualTo(expectedName)
    }

    @Test
    fun getUserById_invalidId_returnsFailure() = runTest {
        //arrange
        val id = "user_999"
        //act
        val result = userRepository.getUserById(id)
        //assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message).isEqualTo("Usuario no encontrado")
    }

    @Test
    fun getUserById_validId_mapsDtoToStudent() = runTest {
        //arrange
        val id = "user_5"
        //act
        val result = userRepository.getUserById(id)
        //assert
        val student = result.getOrNull()
        Truth.assertThat(student).isNotNull()
        Truth.assertThat(student?.username).isEqualTo("usuario_5")
        Truth.assertThat(student?.program).isEqualTo("Ingeniería de Sistemas")
        Truth.assertThat(student?.semester).isEqualTo(6)
        Truth.assertThat(student?.initials).isEqualTo("N5")
    }

    @Test
    fun getUserProfile_validId_returnsProfile() = runTest {
        //arrange
        val id = "user_3"
        //act
        val result = userRepository.getUserProfile(id)
        //assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.student?.name).isEqualTo("Nombre 3")
        Truth.assertThat(result.getOrNull()?.student?.followersCount).isEqualTo(30)
    }

    @Test
    fun getUserProfile_invalidId_returnsFailure() = runTest {
        //arrange
        val id = "user_999"
        //act
        val result = userRepository.getUserProfile(id)
        //assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message).isEqualTo("Usuario no encontrado en Firestore")
    }

    @Test
    fun getAllUsers_excludeId_doesNotReturnExcludedUser() = runTest {
        //arrange
        val excludeId = "user_0"
        //act
        val result = userRepository.getAllUsers(excludeId)
        //assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()).hasSize(9)
        Truth.assertThat(result.getOrNull()?.map { it.id }).doesNotContain(excludeId)
    }

    @Test
    fun getAllUsers_withReviews_calculatesRatingAverage() = runTest {
        //arrange
        val resenadoId = "user_2"
        reviewDataSource.createReview(
            CreateReviewGenericDto(
                autor_id = "user_1",
                resenado_id = resenadoId,
                materia = "Estructuras de Datos (ISIS1206)",
                periodo = "2025-1",
                contenido = "Reseña 1",
                rating = "4"
            )
        )
        reviewDataSource.createReview(
            CreateReviewGenericDto(
                autor_id = "user_3",
                resenado_id = resenadoId,
                materia = "Estructuras de Datos (ISIS1206)",
                periodo = "2025-1",
                contenido = "Reseña 2",
                rating = "2"
            )
        )
        //act
        val result = userRepository.getAllUsers(null)
        //assert
        val student = result.getOrNull()?.first { it.id == resenadoId }
        Truth.assertThat(student?.reviewsCount).isEqualTo(2)
        Truth.assertThat(student?.rating).isEqualTo(3f)
    }

    @Test
    fun followOrUnfollowStudent_followUser_appearsInFollowers() = runTest {
        //arrange
        val currentUserId = "user_1"
        val targetUserId = "user_2"
        //act
        val result = userRepository.followOrUnfollowStudent(currentUserId, targetUserId)
        //assert
        Truth.assertThat(result.isSuccess).isTrue()
        val followers = userRepository.getFollowers(targetUserId)
        Truth.assertThat(followers.getOrNull()?.map { it.id }).contains(currentUserId)
    }

    @After
    fun tearDown() = runTest {
        //Delete Users
        val users = db.collection("users").get().await()
        for (userDoc in users){
            val followers = userDoc.reference.collection("followers").get().await()
            for (followerDoc in followers) {
                followerDoc.reference.delete().await()
            }
            val following = userDoc.reference.collection("following").get().await()
            for (followingDoc in following) {
                followingDoc.reference.delete().await()
            }
        }
        users.documents.forEach { doc ->
            db.collection("users").document(doc.id).delete().await()
        }

        //Delete Reviews
        val reviews = db.collection("reviews").get().await()
        for (reviewDoc in reviews) {
            reviewDoc.reference.delete().await()
        }
    }


}