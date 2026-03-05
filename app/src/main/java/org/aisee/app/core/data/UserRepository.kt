package org.aisee.app.core.data

import org.aisee.app.core.common.Resource
import org.aisee.app.core.data.remote.AiSeeApiClient
import org.aisee.app.core.data.remote.dto.ApiResponse
import org.aisee.app.core.data.remote.dto.CreateUserRequest

interface UserRepository {
    suspend fun registerUser(firstName: String, lastName: String, email: String, password: String): Resource<ApiResponse>
}

class UserRepositoryImpl(private val apiClient: AiSeeApiClient) : UserRepository {

    override suspend fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Resource<ApiResponse> {
        return try {
            val request = CreateUserRequest(
                username = email.substringBefore("@"),
                password = password,
                email = email,
                firstName = firstName,
                lastName = lastName
            )
            val response = apiClient.createUser(request)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Registration failed", e)
        }
    }
}
