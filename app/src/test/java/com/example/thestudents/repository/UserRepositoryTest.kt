package com.example.thestudents.repository

import com.example.thestudents.data.datasource.AuthRemoteDataSource
import com.example.thestudents.data.datasource.impl.firestore.ReviewFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.thestudents.data.dtos.ReviewDto
import com.example.thestudents.data.dtos.StudentDto
import com.example.thestudents.data.dtos.UserProfileFirestoreDto
import com.example.thestudents.data.repository.UserRepository
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UserRepositoryTest {

    private val mockDataSource = mockk<UserFirestoreDataSourceImpl>()
    private val authDataSource = mockk<AuthRemoteDataSource>()
    private val reviewDataSource = mockk<ReviewFirestoreDataSourceImpl>()

    private val repository = UserRepository(mockDataSource, reviewDataSource, authDataSource)

    @Test
    fun `al llamar getUserById, si es valido un Result con elk userInfo`() = runTest {
        // AAA
        val dto = StudentDto(
            id = "123",
            nombre_usuario = "juan123",
            nombre = "juan",
            biografia = "Desarrollador Android",
            foto_url = null,
            color_perfil = "#4A90E2",
            carrera = "Ingeniería de Sistemas",
            semestre = 5
        )

        //arrange
        coEvery { mockDataSource.getUserById("123") } returns dto

        // Act
        val result = repository.getUserById("123")

        // Assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.id).isEqualTo("123")
        Truth.assertThat(result.getOrNull()?.name).isEqualTo("juan")
    }
    @Test
    fun `al llamar getUserById, si el id es invalido un Result con failure`() = runTest {
        // AAA
        coEvery { mockDataSource.getUserById("999") } returns null

        // Act
        val result = repository.getUserById("999")
        //Assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message).isEqualTo("Usuario no encontrado")
    }
    @Test
    fun `al llamar getUserProfile, si es valido retorna un Result con el UserProfile`() = runTest {
        // Arrange
        val dto = UserProfileFirestoreDto(
            id = "123",
            nombre_usuario = "juan123",
            nombre = "juan",
            biografia = "Desarrollador Android",
            foto_url = null,
            color_perfil = "#4A90E2",
            carrera = "Ingeniería de Sistemas",
            semestre = 5,
            resenas_recibidas = emptyList(),
            resenas_creadas = emptyList(),
            followersCount = 10,
            followingCount = 5,
            followed = false
        )
        coEvery { authDataSource.currentUser } returns null
        coEvery { mockDataSource.getUserProfile("123", "") } returns dto

        // Act
        val result = repository.getUserProfile("123")

        // Assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.student?.id).isEqualTo("123")
        Truth.assertThat(result.getOrNull()?.student?.name).isEqualTo("juan")
    }

    @Test
    fun `al llamar getUserProfile, si el id es invalido retorna un Result con failure`() = runTest {
        // Arrange
        coEvery { authDataSource.currentUser } returns null
        coEvery { mockDataSource.getUserProfile("999", "") } throws Exception("Usuario no encontrado")

        // Act
        val result = repository.getUserProfile("999")

        // Assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message).isEqualTo("Usuario no encontrado")
    }

    @Test
    fun `al llamar getUserById, si el datasource devuelve campos vacios se mapean correctamente`() = runTest {
        // Arrange
        val dtoVacio = StudentDto(
            id = "123",
            nombre_usuario = null,
            nombre = null,
            biografia = null,
            foto_url = null,
            color_perfil = null,
            carrera = null,
            semestre = null
        )
        coEvery { mockDataSource.getUserById("123") } returns dtoVacio

        // Act
        val result = repository.getUserById("123")

        // Assert
        Truth.assertThat(result.isSuccess).isTrue()
        val student = result.getOrNull()
        Truth.assertThat(student?.id).isEqualTo("123")
        Truth.assertThat(student?.name).isEmpty()
        Truth.assertThat(student?.username).isEmpty()
        Truth.assertThat(student?.program).isEmpty()
        Truth.assertThat(student?.semester).isEqualTo(1) // Valor por defecto definido en toStudent()
        Truth.assertThat(student?.bio).isEmpty()
        Truth.assertThat(student?.initials).isEqualTo("US") // Iniciales por defecto
        Truth.assertThat(student?.profileImage).isNull()
    }

    @Test
    fun `al llamar getAllUsers con excludeId, excluye a ese usuario y calcula el rating promedio`() = runTest {
        // Arrange
        val student1 = StudentDto(id = "user_1", nombre = "Carlos")
        val student2 = StudentDto(id = "user_2", nombre = "Maria")

        val review1 = ReviewDto(id = "r1", rating = "4.0")
        val review2 = ReviewDto(id = "r2", rating = "5.0")

        coEvery { mockDataSource.getAllUsers() } returns listOf(student1, student2)
        coEvery { reviewDataSource.getReviewsByReviewedStudentId("user_2") } returns listOf(review1, review2)

        // Act
        val result = repository.getAllUsers(excludeId = "user_1")

        // Assert
        Truth.assertThat(result.isSuccess).isTrue()
        val list = result.getOrNull()
        Truth.assertThat(list).hasSize(1)
        Truth.assertThat(list?.first()?.id).isEqualTo("user_2")
        Truth.assertThat(list?.first()?.reviewsCount).isEqualTo(2)
        Truth.assertThat(list?.first()?.rating).isEqualTo(4.5f)
    }

    @Test
    fun `al llamar followOrUnfollowStudent exitosamente, retorna Result success`() = runTest {
        // Arrange
        coEvery { mockDataSource.followOrUnfollowStudent("user_1", "user_2") } returns Unit

        // Act
        val result = repository.followOrUnfollowStudent("user_1", "user_2")

        // Assert
        Truth.assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `al llamar getFollowers, retorna la lista de estudiantes mapeada`() = runTest {
        // Arrange
        val follower = StudentDto(id = "follower_1", nombre = "Ana")
        coEvery { mockDataSource.getFollowers("user_1") } returns listOf(follower)

        // Act
        val result = repository.getFollowers("user_1")

        // Assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()).hasSize(1)
        Truth.assertThat(result.getOrNull()?.first()?.name).isEqualTo("Ana")
    }
}
