package org.aisee.app.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.aisee.app.core.common.Resource
import org.aisee.app.core.data.UserRepository
import org.aisee.app.core.data.remote.dto.ApiResponse

class ForgotPasswordViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _forgotPasswordState = MutableStateFlow<Resource<ApiResponse>?>(null)
    val forgotPasswordState: StateFlow<Resource<ApiResponse>?> = _forgotPasswordState.asStateFlow()

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            _forgotPasswordState.value = Resource.Loading
            _forgotPasswordState.value = userRepository.forgotPassword(email)
        }
    }

    fun resetState() {
        _forgotPasswordState.value = null
    }
}
