package com.example.thestudents.repository

import android.util.Log
import com.example.thestudents.data.datasource.AuthRemoteDataSource
import com.example.thestudents.data.datasource.impl.firestore.ReviewFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
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

    @Before
    fun setUp() = runTest {
        try {
            db.useEmulator("10.0.2.2", 8080)
        }catch (e: Exception) {
        }

        userRepository= UserRepository(UserFirestoreDataSourceImpl(db),
            ReviewFirestoreDataSourceImpl(db),AuthRemoteDataSource(auth))


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
        val excpectedName = "Name 9"
        //act
        val result = userRepository.getUserById(id)
        //assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result?.getOrNull()?.name).isEqualTo(excpectedName)
    }

    @Test
    fun getUserById_invalidId_returnFailure() = runTest {
        //arrange
        val id = "user_999"
        //act
        val result = userRepository.getUserById(id)
        //assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result?.exceptionOrNull()?.message).isEqualTo("Usuario no encontrado")
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