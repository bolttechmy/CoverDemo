package io.bolttech.coverdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class LoginViewModel : ViewModel() {

    private val _email = mutableStateOf("user@example.com")
    val email: State<String> get() = _email

    private val _password = mutableStateOf("password")
    val password: State<String> get() = _password

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _loginSuccessful = mutableStateOf<Boolean?>(null)
    val loginSuccessful: State<Boolean?> get() = _loginSuccessful

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun login() {
        _isLoading.value = true
        _loginSuccessful.value = null

        // Simulate a network request
        viewModelScope.launch {
            delay(2000) // Simulate network delay
            _isLoading.value = false
            _loginSuccessful.value = _email.value == "user@example.com" && _password.value == "password"
        }
    }
}
