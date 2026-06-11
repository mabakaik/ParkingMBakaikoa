package com.lksnext.parkingmbakaikoa.ui.resetPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lksnext.parkingmbakaikoa.data.repository.AuthRepository
import com.lksnext.parkingmbakaikoa.ui.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResetPasswordViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ResetPasswordUiState>(ResetPasswordUiState.Idle)
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    fun resetPassword(email: String) {
        if (!ValidationUtils.isValidEmail(email)) {
            _uiState.value = ResetPasswordUiState.ValidationError("email", "Email inválido")
            return
        }

        viewModelScope.launch {
            _uiState.value = ResetPasswordUiState.Loading

            val result = authRepository.sendPasswordResetEmail(email)
            _uiState.value = result.fold(
                onSuccess = { ResetPasswordUiState.Success },
                onFailure = { ResetPasswordUiState.Error(it.message ?: "Error al enviar el correo de recuperación") }
            )
        }
    }

    fun resetState() {
        _uiState.value = ResetPasswordUiState.Idle
    }

    /** Solo para previews y tests */
    internal fun setStateForPreview(state: ResetPasswordUiState) {
        _uiState.value = state
    }
}

