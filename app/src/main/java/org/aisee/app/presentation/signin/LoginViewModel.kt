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

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<ApiResponse>?>(null)
    val loginState: StateFlow<Resource<ApiResponse>?> = _loginState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading
            _loginState.value = userRepository.login(username, password)
        }
    }

    fun resetState() {
        _loginState.value = null
    }
}
