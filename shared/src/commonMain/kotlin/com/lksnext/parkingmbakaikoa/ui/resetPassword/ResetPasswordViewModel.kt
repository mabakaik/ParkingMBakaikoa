package com.lksnext.parkingmbakaikoa.ui.resetPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lksnext.parkingmbakaikoa.data.repository.AuthRepository
import com.lksnext.parkingmbakaikoa.ui.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import parkingmbakaikoa.shared.generated.resources.Res
import parkingmbakaikoa.shared.generated.resources.invalidEmail
import parkingmbakaikoa.shared.generated.resources.resetPasswordError

class ResetPasswordViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ResetPasswordUiState>(ResetPasswordUiState.Idle)
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    fun resetPassword(email: String) {
        viewModelScope.launch {
            if (!ValidationUtils.isValidEmail(email)) {
                _uiState.value = ResetPasswordUiState.ValidationError("email", getString(Res.string.invalidEmail))
                return@launch
            }

            _uiState.value = ResetPasswordUiState.Loading

            val result = authRepository.sendPasswordResetEmail(email)
            _uiState.value = result.fold(
                onSuccess = { ResetPasswordUiState.Success },
                onFailure = { ResetPasswordUiState.Error(it.message ?: getString(Res.string.resetPasswordError)) }
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

