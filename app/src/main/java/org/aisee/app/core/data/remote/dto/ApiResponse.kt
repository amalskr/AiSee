package org.aisee.app.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val status: String? = null,
    @SerialName("http_code") val httpCode: Int? = null,
    val message: String? = null,
    val data: ApiData? = null,
    val errors: ApiError? = null
)

@Serializable
data class ApiData(
    val user: ApiUser? = null,
    val tokens: ApiTokens? = null
)

@Serializable
data class ApiUser(
    @SerialName("user_id") val userId: String? = null,
    val username: String? = null,
    val email: String? = null,
    val role: String? = null,
    @SerialName("organization_id") val organizationId: String? = null
)

@Serializable
data class ApiTokens(
    @SerialName("access_token") val accessToken: String? = null,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("token_type") val tokenType: String? = null,
    @SerialName("expires_in") val expiresIn: Int? = null
)

@Serializable
data class ApiError(
    val code: String? = null,
    val type: String? = null,
    val details: String? = null,
    val retryable: Boolean? = null
)
