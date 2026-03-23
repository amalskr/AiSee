package org.aisee.app.core.data

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.aisee.app.core.common.Resource
import org.aisee.app.core.data.remote.AiSeeApiClient
import org.aisee.app.core.data.remote.dto.ApiResponse
import org.aisee.app.core.data.remote.dto.GoogleSignInRequest

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun signInWithGoogle(context: Context): Resource<ApiResponse>
    suspend fun signOut()
}

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val credentialManager: CredentialManager,
    private val webClientId: String,
    private val apiClient: AiSeeApiClient
) : AuthRepository {

    companion object {
        // Web client ID expected by the backend for Google token verification
        private const val BACKEND_WEB_CLIENT_ID =
            "42711319336-7grioifs7tjgsan0vibilqe1cka1r9vg.apps.googleusercontent.com"
    }

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun signInWithGoogle(context: Context): Resource<ApiResponse> {
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(BACKEND_WEB_CLIENT_ID)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(context, request)

            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
            val idToken = googleIdTokenCredential.idToken

            val response = apiClient.googleSignIn(GoogleSignInRequest(idToken = idToken))
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Google Sign-In failed", e)
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }
}
