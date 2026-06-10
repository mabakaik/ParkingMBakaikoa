package com.lksnext.parkingmbakaikoa.ui.register

sealed class RegisterUiState {
    data object Idle : RegisterUiState()
    data object Loading : RegisterUiState()
    data object Success : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
    data class ValidationError(val field: String, val message: String) : RegisterUiState()
}

