package org.aisee.app.core.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.aisee.app.core.data.remote.dto.ApiResponse
import org.aisee.app.core.data.remote.dto.CreateUserRequest
import org.aisee.app.core.data.remote.dto.ForgotPasswordRequest
import org.aisee.app.core.data.remote.dto.GoogleSignInRequest
import org.aisee.app.core.data.remote.dto.LoginRequest

class AiSeeApiClient(private val httpClient: HttpClient) {

    companion object {
        const val BASE_URL = "http://35.201.105.188"
    }

    suspend fun createUser(request: CreateUserRequest): ApiResponse {
        return httpClient.post("$BASE_URL/auth/signup") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun login(request: LoginRequest): ApiResponse {
        return httpClient.post("$BASE_URL/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun googleSignIn(request: GoogleSignInRequest): ApiResponse {
        return httpClient.post("$BASE_URL/auth/social/google") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun forgotPassword(request: ForgotPasswordRequest): ApiResponse {
        return httpClient.post("$BASE_URL/auth/forgot-password") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}
