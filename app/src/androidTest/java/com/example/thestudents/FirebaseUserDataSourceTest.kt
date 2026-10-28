package com.example.thestudents

import android.R.id
import android.util.Log
import com.example.thestudents.data.datasource.impl.firestore.ReviewFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.thestudents.data.dtos.CreateReviewGenericDto
import com.example.thestudents.data.dtos.RegisterUserDto
import com.example.thestudents.data.dtos.UserProfileFirestoreDto
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test


class FirebaseUserDataSourceTest {

    private val db =  Firebase.firestore
    private lateinit var dataSource: UserFirestoreDataSourceImpl // luego lo inicializamos
    private lateinit var reviewDataSource: ReviewFirestoreDataSourceImpl
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


    @Before
    fun setUp() = runTest {
        try {
            db.useEmulator("10.0.2.2", 8080)
        }catch (e: Exception) {
        }

        dataSource = UserFirestoreDataSourceImpl(db)
        reviewDataSource = ReviewFirestoreDataSourceImpl(db)
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
    fun getUserById_validId_correctUser() = runTest {
        //arrange
        val id = "user_9"
       //act
        val result = dataSource.getUserById(id)
        //assert
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.id).isEqualTo(id)

    }

    @Test
    fun getUserById_invalidId_returnsNull() = runTest {
        //arrange
        val id = "user_999"
        //act
        val result = dataSource.getUserById(id)
        //assert
        Truth.assertThat(result).isNull()
    }

    @Test
    fun registerUser_validData_documentExists() = runTest {
        //arrange
        val userId = "user_100"
        val registerUserDto = RegisterUserDto(
            nombre = "Usuario Nuevo",
            nombre_usuario = "usuario_nuevo",
            carrera = "Ingeniería de Sistemas",
            semestre = 5,
            biografia = "Biografia de prueba",
            fcmToken = ""
        )
        //act
        dataSource.registerUser(registerUserDto, userId)
        //assert
        val result = dataSource.getUserById(userId)
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.id).isEqualTo(userId)
        Truth.assertThat(result?.nombre).isEqualTo(registerUserDto.nombre)
        Truth.assertThat(result?.nombre_usuario).isEqualTo(registerUserDto.nombre_usuario)
    }

    @Test
    fun followOrUnfollowStudent_followUser_followedTrue() = runTest {
        //arrange
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)
        //act
        dataSource.followOrUnfollowStudent(currentUser.id!!, targetUser.id!!)
        //assert
        val targetUserUpdated = dataSource.getUserProfile(targetUser.id, currentUser.id) as UserProfileFirestoreDto // es necesario porque esa función devuelve el tipo genérico UserProfileGenericDto.
        Truth.assertThat(targetUserUpdated.followed).isTrue()
    }

    @Test
    fun followOrUnfollowStudent_followUser_followersCountIncreased() = runTest {
        //arrange
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)
        val odlData = dataSource.getUserProfile(targetUser.id!!, currentUser.id!!) as UserProfileFirestoreDto
        //act
        dataSource.followOrUnfollowStudent(currentUser.id, targetUser.id)
        //assert
        val targetUserUpdated = dataSource.getUserProfile(targetUser.id, currentUser.id) as UserProfileFirestoreDto // es necesario porque esa función devuelve el tipo genérico UserProfileGenericDto.
        Truth.assertThat(targetUserUpdated.followersCount).isGreaterThan(odlData.followersCount)
    }

    @Test
    fun followOrUnfollowStudent_unfollowUser_followedFalse() = runTest {
        //arrange
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)

        //act
        dataSource.followOrUnfollowStudent(currentUser.id!!, targetUser.id!!)
        dataSource.followOrUnfollowStudent(currentUser.id, targetUser.id)
        //assert
        val targetUserUpdated = dataSource.getUserProfile(targetUser.id, currentUser.id) as UserProfileFirestoreDto // es necesario porque esa función devuelve el tipo genérico UserProfileGenericDto.
        Truth.assertThat(targetUserUpdated.followed).isFalse()
    }

    @Test
    fun getUserReviews_validId_returnsCreatedReview() = runTest {
        //arrange
        val autor = generateUser(1)
        val resenado = generateUser(2)
        val createReviewDto = CreateReviewGenericDto(
            autor_id = autor.id,
            resenado_id = resenado.id,
            materia = "Estructuras de Datos (ISIS1206)",
            periodo = "2025-1",
            contenido = "Reseña de prueba",
            rating = "5"
        )
        reviewDataSource.createReview(createReviewDto)
        //act
        val result = dataSource.getUserProfile(autor.id!!, "") as UserProfileFirestoreDto
        //assert
        Truth.assertThat(result.resenas_creadas).isNotEmpty()
        Truth.assertThat(result.resenas_creadas.first().contenido).isEqualTo(createReviewDto.contenido)
    }

    @Test
    fun getAllUsers_usersExist_returnsAllUsers() = runTest {
        //arrange
        val expectedSize = 10
        //act
        val result = dataSource.getAllUsers()
        //assert
        Truth.assertThat(result).hasSize(expectedSize)
        Truth.assertThat(result.map { it.id }).contains("user_0")
        Truth.assertThat(result.map { it.id }).contains("user_9")
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