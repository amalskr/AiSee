package org.aisee.app.core.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordRequest(
    val email: String
)
