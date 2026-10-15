package com.example.thestudents.data.repository

import com.example.thestudents.data.datasource.AuthRemoteDataSource
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
){

    val currentUser: FirebaseUser?
        get() = authRemoteDataSource.currentUser

    suspend fun signIn(email: String, password: String) : Result<Unit> {
        return try {
            authRemoteDataSource.signIn(email, password)
            Result.success(Unit)
        }
        catch (e: FirebaseAuthInvalidCredentialsException){
            Result.failure(Exception("Credenciales incorrectas"))
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(Exception("El usuario no existe"))
        }
        catch (e: Exception) {
            Result.failure(Exception("Error al iniciar sesión"))
        }

    }

    suspend fun signUp(email: String, password: String): Result<Unit>{
        return try {
            authRemoteDataSource.signUp(email, password)
            Result.success(Unit)
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(Exception("El correo electrónico ya está registrado"))
        } catch (e: FirebaseAuthWeakPasswordException) {
            Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(Exception("El formato del correo electrónico es inválido"))
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Error al registrar la cuenta"))
        }
    }

    fun signOut(){
        authRemoteDataSource.signOut()
    }

}