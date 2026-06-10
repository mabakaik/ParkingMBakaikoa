package com.lksnext.parkingmbakaikoa.ui.login

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
    data class ValidationError(val field: String, val message: String) : LoginUiState()
}

