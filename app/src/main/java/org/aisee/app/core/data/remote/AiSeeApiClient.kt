package org.aisee.app.core.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.aisee.app.core.data.remote.dto.ApiResponse
import org.aisee.app.core.data.remote.dto.CreateUserRequest

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
}
