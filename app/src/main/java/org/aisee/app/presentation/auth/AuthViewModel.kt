package org.aisee.app.presentation.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.aisee.app.core.common.Resource
import org.aisee.app.core.data.AuthRepository
import org.aisee.app.core.data.remote.dto.ApiResponse

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _googleSignInState = MutableStateFlow<Resource<ApiResponse>?>(null)
    val googleSignInState: StateFlow<Resource<ApiResponse>?> = _googleSignInState.asStateFlow()

    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            _googleSignInState.value = Resource.Loading
            _googleSignInState.value = authRepository.signInWithGoogle(context)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _googleSignInState.value = null
        }
    }

    fun resetState() {
        _googleSignInState.value = null
    }
}
