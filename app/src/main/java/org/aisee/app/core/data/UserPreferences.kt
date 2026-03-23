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
        val metadata = user.metadata
        val fullName = listOfNotNull(user.firstName, user.lastName)
            .joinToString(" ")
            .ifEmpty { null }
        prefs.edit()
            .putString(KEY_USER_ID, user.userId)
            .putString(KEY_USERNAME, user.username)
            .putString(KEY_EMAIL, user.email)
            .putString(KEY_ROLE, user.role)
            .putString(KEY_ORGANIZATION_ID, user.organizationId)
            .putString(KEY_PERMISSIONS, user.permissions?.joinToString(","))
            .putBoolean(KEY_IS_ACTIVE, metadata?.isActive ?: true)
            .putString(KEY_CREATED_AT, metadata?.createdAt)
            .putString(KEY_LAST_LOGIN_AT, metadata?.lastLoginAt)
            .putString(KEY_ACCESS_TOKEN, tokens?.accessToken)
            .putString(KEY_REFRESH_TOKEN, tokens?.refreshToken)
            .putString(KEY_TOKEN_TYPE, tokens?.tokenType)
            .putInt(KEY_EXPIRES_IN, tokens?.expiresIn ?: 0)
            .putString(KEY_AUTH_PROVIDER, metadata?.authProvider)
            .apply {
                if (fullName != null) putString(KEY_FULL_NAME, fullName)
            }
            .apply()
    }

    fun saveGoogleSignIn(displayName: String?, email: String?, uid: String) {
        prefs.edit()
            .putString(KEY_USER_ID, uid)
            .putString(KEY_FULL_NAME, displayName)
            .putString(KEY_EMAIL, email)
            .putString(KEY_USERNAME, "${email ?: displayName} (Google)")
            .putString(KEY_ACCESS_TOKEN, "firebase_google_$uid")
            .apply()
    }

    fun saveLocalFields(fullName: String, phoneNumber: String) {
        prefs.edit()
            .putString(KEY_FULL_NAME, fullName)
            .putString(KEY_PHONE_NUMBER, phoneNumber)
            .apply()
    }

    val fullName: String? get() = prefs.getString(KEY_FULL_NAME, null)
    val phoneNumber: String? get() = prefs.getString(KEY_PHONE_NUMBER, null)
    val userId: String? get() = prefs.getString(KEY_USER_ID, null)
    val username: String? get() = prefs.getString(KEY_USERNAME, null)
    val email: String? get() = prefs.getString(KEY_EMAIL, null)
    val role: String? get() = prefs.getString(KEY_ROLE, null)
    val organizationId: String? get() = prefs.getString(KEY_ORGANIZATION_ID, null)
    val permissions: List<String> get() = prefs.getString(KEY_PERMISSIONS, null)?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
    val isActive: Boolean get() = prefs.getBoolean(KEY_IS_ACTIVE, true)
    val createdAt: String? get() = prefs.getString(KEY_CREATED_AT, null)
    val lastLoginAt: String? get() = prefs.getString(KEY_LAST_LOGIN_AT, null)
    val accessToken: String? get() = prefs.getString(KEY_ACCESS_TOKEN, null)
    val refreshToken: String? get() = prefs.getString(KEY_REFRESH_TOKEN, null)
    val tokenType: String? get() = prefs.getString(KEY_TOKEN_TYPE, null)
    val expiresIn: Int get() = prefs.getInt(KEY_EXPIRES_IN, 0)
    val authProvider: String? get() = prefs.getString(KEY_AUTH_PROVIDER, null)

    val isLoggedIn: Boolean get() = accessToken != null

    val appOpenCount: Int get() = prefs.getInt(KEY_APP_OPEN_COUNT, 0)
    val hasShownReview: Boolean get() = prefs.getBoolean(KEY_HAS_SHOWN_REVIEW, false)

    fun incrementAppOpenCount() {
        prefs.edit().putInt(KEY_APP_OPEN_COUNT, appOpenCount + 1).apply()
    }

    fun markReviewShown() {
        prefs.edit().putBoolean(KEY_HAS_SHOWN_REVIEW, true).apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_FULL_NAME = "full_name"
        private const val KEY_PHONE_NUMBER = "phone_number"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_ROLE = "role"
        private const val KEY_ORGANIZATION_ID = "organization_id"
        private const val KEY_PERMISSIONS = "permissions"
        private const val KEY_IS_ACTIVE = "is_active"
        private const val KEY_CREATED_AT = "created_at"
        private const val KEY_LAST_LOGIN_AT = "last_login_at"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_TOKEN_TYPE = "token_type"
        private const val KEY_EXPIRES_IN = "expires_in"
        private const val KEY_AUTH_PROVIDER = "auth_provider"
        private const val KEY_APP_OPEN_COUNT = "app_open_count"
        private const val KEY_HAS_SHOWN_REVIEW = "has_shown_review"
    }
}
