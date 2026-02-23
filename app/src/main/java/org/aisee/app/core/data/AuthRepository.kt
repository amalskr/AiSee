package org.aisee.app.core.data

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import org.aisee.app.core.common.Resource

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun signInWithGoogle(context: Context): Resource<FirebaseUser>
    suspend fun signOut()
}

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val credentialManager: CredentialManager,
    private val webClientId: String
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun signInWithGoogle(context: Context): Resource<FirebaseUser> {
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(context, request)

            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
            val authResult = firebaseAuth.signInWithCredential(firebaseCredential).await()

            val user = authResult.user
            if (user != null) {
                Resource.Success(user)
            } else {
                Resource.Error("Sign-in succeeded but user is null")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Google Sign-In failed", e)
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }
}
