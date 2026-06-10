package com.lksnext.parkingmbakaikoa.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lksnext.parkingmbakaikoa.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthStateViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn.asStateFlow()

    private var unsubscribeListener: (() -> Unit)? = null

    init {
        startListeningToAuthChanges()
    }

    private fun startListeningToAuthChanges() {
        unsubscribeListener = authRepository.observeAuthState { isLogged ->
            _isLoggedIn.value = isLogged
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _isLoggedIn.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        unsubscribeListener?.invoke()
    }
}

