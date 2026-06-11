package com.lksnext.parkingmbakaikoa.ui.resetPassword

sealed class ResetPasswordUiState {
    data object Idle : ResetPasswordUiState()
    data object Loading : ResetPasswordUiState()
    data object Success : ResetPasswordUiState()
    data class Error(val message: String) : ResetPasswordUiState()
    data class ValidationError(val field: String, val message: String) : ResetPasswordUiState()
}

