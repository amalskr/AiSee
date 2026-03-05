package org.aisee.app.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val status: String? = null,
    @SerialName("http_code") val httpCode: Int? = null,
    val message: String? = null,
    val errors: List<ApiError>? = null
)

@Serializable
data class ApiError(
    val code: String? = null,
    val type: String? = null,
    val details: String? = null,
    val retryable: Boolean? = null
)
