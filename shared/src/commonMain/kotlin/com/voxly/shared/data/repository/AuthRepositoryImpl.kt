package com.voxly.shared.data.repository

import com.voxly.shared.data.remote.AuthApiService
import com.voxly.shared.data.remote.LoginRequest
import com.voxly.shared.data.remote.OtpRequest
import com.voxly.shared.data.remote.OtpVerifyRequest
import com.voxly.shared.data.remote.SignUpEmailRequest
import com.voxly.shared.domain.model.LearningGoal
import com.voxly.shared.domain.model.User
import com.voxly.shared.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepositoryImpl(
    private val api: AuthApiService,
    private val tokenStore: TokenStore,
) : AuthRepository {

    private val _currentUser = MutableStateFlow<User?>(null)

    override fun currentUser(): Flow<User?> = _currentUser.asStateFlow()

    override suspend fun signUpWithEmail(
        email: String, password: String, name: String,
        country: String, goal: LearningGoal,
    ): Result<User> = runCatching {
        val resp = api.signUpWithEmail(
            SignUpEmailRequest(email, password, name, country, goal.name)
        )
        tokenStore.saveToken(resp.token)
        val user = resp.user.toDomain()
        _currentUser.value = user
        user
    }

    override suspend fun signUpWithGoogle(idToken: String): Result<User> = runCatching {
        throw NotImplementedError("Google sign-up not yet connected")
    }

    override suspend fun logIn(email: String, password: String): Result<User> = runCatching {
        val resp = api.login(LoginRequest(email, password))
        tokenStore.saveToken(resp.token)
        val user = resp.user.toDomain()
        _currentUser.value = user
        user
    }

    override suspend fun logOut(): Result<Unit> = runCatching {
        tokenStore.clearToken()
        _currentUser.value = null
    }

    override suspend fun sendOtp(email: String): Result<Unit> = runCatching {
        api.sendOtp(OtpRequest(email))
    }

    override suspend fun verifyOtp(email: String, code: String): Result<User> = runCatching {
        val resp = api.verifyOtp(OtpVerifyRequest(email, code))
        tokenStore.saveToken(resp.token)
        val user = resp.user.toDomain()
        _currentUser.value = user
        user
    }

    override suspend fun resetPassword(email: String): Result<Unit> = runCatching {
        api.resetPassword(OtpRequest(email))
    }
}

interface TokenStore {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
}
