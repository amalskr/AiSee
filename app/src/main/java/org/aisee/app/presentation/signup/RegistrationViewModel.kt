package org.aisee.app.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.aisee.app.core.common.Resource
import org.aisee.app.core.data.UserRepository
import org.aisee.app.core.data.remote.dto.ApiResponse

class RegistrationViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registrationState = MutableStateFlow<Resource<ApiResponse>?>(null)
    val registrationState: StateFlow<Resource<ApiResponse>?> = _registrationState.asStateFlow()

    fun registerUser(username: String, email: String, password: String) {
        viewModelScope.launch {
            _registrationState.value = Resource.Loading
            _registrationState.value = userRepository.registerUser(username, email, password)
        }
    }

    fun resetState() {
        _registrationState.value = null
    }
}
