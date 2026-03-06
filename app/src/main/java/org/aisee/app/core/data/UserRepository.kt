package org.aisee.app.core.data

import org.aisee.app.core.common.Resource
import org.aisee.app.core.data.remote.AiSeeApiClient
import org.aisee.app.core.data.remote.dto.ApiResponse
import org.aisee.app.core.data.remote.dto.CreateUserRequest
import org.aisee.app.core.data.remote.dto.ForgotPasswordRequest
import org.aisee.app.core.data.remote.dto.LoginRequest

interface UserRepository {
    suspend fun registerUser(firstName: String, lastName: String, email: String, password: String, phoneNumber: String): Resource<ApiResponse>
    suspend fun login(username: String, password: String): Resource<ApiResponse>
    suspend fun forgotPassword(email: String): Resource<ApiResponse>
}

class UserRepositoryImpl(private val apiClient: AiSeeApiClient) : UserRepository {

    override suspend fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        phoneNumber: String
    ): Resource<ApiResponse> {
        return try {
            val request = CreateUserRequest(
                username = email.substringBefore("@"),
                password = password,
                email = email,
                firstName = firstName,
                lastName = lastName,
                phoneNumber = "+$phoneNumber"
            )
            val response = apiClient.createUser(request)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Registration failed", e)
        }
    }

    override suspend fun login(username: String, password: String): Resource<ApiResponse> {
        return try {
            val request = LoginRequest(username = username, password = password)
            val response = apiClient.login(request)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Login failed", e)
        }
    }

    override suspend fun forgotPassword(email: String): Resource<ApiResponse> {
        return try {
            val request = ForgotPasswordRequest(email = email)
            val response = apiClient.forgotPassword(request)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Request failed", e)
        }
    }
}
