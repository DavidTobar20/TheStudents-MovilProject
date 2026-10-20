package com.example.thestudents.data.repository

import com.example.thestudents.data.Student
import com.example.thestudents.data.UserProfile
import com.example.thestudents.data.datasource.AuthRemoteDataSource
import com.example.thestudents.data.datasource.impl.firestore.ReviewFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.thestudents.data.dtos.RegisterUserDto
import com.example.thestudents.data.dtos.toStudent
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserFirestoreDataSourceImpl,
    private val reviewRemoteDataSource: ReviewFirestoreDataSourceImpl,
    private val authRemoteDataSource: AuthRemoteDataSource
) {

    suspend fun getUserProfile(studentId: String): Result<UserProfile> {
        val currentUserId = authRemoteDataSource.currentUser?.uid ?: ""

        return try {
            val response = userRemoteDataSource.getUserProfile(studentId, currentUserId)

            val profile = response.toUserProfile()

            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(studentId: String): Result<Student> {
        return try {
            val response = userRemoteDataSource.getUserById(studentId)

            val student = response.toStudent()

            Result.success(student)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerUser(
        name: String,
        username: String,
        program: String,
        semester: Int,
        bio: String,
        userId: String
    ): Result<Unit> {
        return try {
            val registerUserDto = RegisterUserDto(
                nombre = name,
                nombre_usuario = username,
                carrera = program,
                semestre = semester,
                biografia = bio
            )
            userRemoteDataSource.registerUser(registerUserDto, userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(
        name: String,
        username: String,
        program: String,
        semester: Int,
        bio: String,
        fotoUrl: String?,
        colorPerfil: String?,
        userId: String
    ): Result<Unit> {
        return try {
            val registerUserDto = RegisterUserDto(
                nombre = name,
                nombre_usuario = username,
                carrera = program,
                semestre = semester,
                biografia = bio,
                foto_url = fotoUrl,
                color_perfil = colorPerfil ?: ""
            )
            userRemoteDataSource.updateUser(registerUserDto, userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllUsers(excludeId: String?): Result<List<Student>> {
        return try {
            val users = userRemoteDataSource.getAllUsers()
            val students = coroutineScope {
                users
                    .map { it.toStudent() }
                    .filter { it.id != excludeId }
                    .map { student ->
                        // Las consultas de reseñas de cada estudiante se hacen en paralelo
                        async {
                            val receivedReviews = reviewRemoteDataSource.getReviewsByReviewedStudentId(student.id)
                            val ratings = receivedReviews.mapNotNull { it.rating?.toFloatOrNull() }
                            student.copy(
                                reviewsCount = receivedReviews.size,
                                rating = if (ratings.isEmpty()) 0f else ratings.average().toFloat()
                            )
                        }
                    }
                    .awaitAll()
            }
            Result.success(students)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun followOrUnfollowStudent(currentUserId: String, targetUserId: String): Result<Unit> {
        return try {
            userRemoteDataSource.followOrUnfollowStudent(currentUserId, targetUserId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
