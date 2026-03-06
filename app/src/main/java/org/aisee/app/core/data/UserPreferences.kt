package org.aisee.app.core.data

import android.content.Context
import android.content.SharedPreferences
import org.aisee.app.core.data.remote.dto.ApiResponse

class UserPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveFromResponse(response: ApiResponse) {
        val user = response.data?.user ?: return
        val tokens = response.data.tokens
        prefs.edit()
            .putString(KEY_USER_ID, user.userId)
            .putString(KEY_USERNAME, user.username)
            .putString(KEY_EMAIL, user.email)
            .putString(KEY_ROLE, user.role)
            .putString(KEY_ORGANIZATION_ID, user.organizationId)
            .putString(KEY_ACCESS_TOKEN, tokens?.accessToken)
            .putString(KEY_REFRESH_TOKEN, tokens?.refreshToken)
            .apply()
    }

    val userId: String? get() = prefs.getString(KEY_USER_ID, null)
    val username: String? get() = prefs.getString(KEY_USERNAME, null)
    val email: String? get() = prefs.getString(KEY_EMAIL, null)
    val role: String? get() = prefs.getString(KEY_ROLE, null)
    val organizationId: String? get() = prefs.getString(KEY_ORGANIZATION_ID, null)
    val accessToken: String? get() = prefs.getString(KEY_ACCESS_TOKEN, null)
    val refreshToken: String? get() = prefs.getString(KEY_REFRESH_TOKEN, null)

    val isLoggedIn: Boolean get() = accessToken != null

    fun clear() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_ROLE = "role"
        private const val KEY_ORGANIZATION_ID = "organization_id"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }
}
