package org.aisee.app.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val username: String,
    val password: String,
    val email: String,
    val role: String = "user",
    @SerialName("is_active") val isActive: Boolean = true,
    @SerialName("organization_id") val organizationId: Int = 1
)
