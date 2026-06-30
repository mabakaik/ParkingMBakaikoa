package com.lksnext.parkingmbakaikoa.ui.login

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
import parkingmbakaikoa.shared.generated.resources.passwordEmpty
import parkingmbakaikoa.shared.generated.resources.unknownError

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            if (!ValidationUtils.isValidEmail(email)) {
                _uiState.value = LoginUiState.ValidationError("email", getString(Res.string.invalidEmail))
                return@launch
            }

            if (password.isBlank()) {
                _uiState.value = LoginUiState.ValidationError("password", getString(Res.string.passwordEmpty))
                return@launch
            }


            _uiState.value = LoginUiState.Loading
            val result = authRepository.login(email, password)
            _uiState.value = result.fold(
                onSuccess = { LoginUiState.Success },
                onFailure = { LoginUiState.Error(it.message ?: getString(Res.string.unknownError)) }
            )
        }
//        _uiState.value = LoginUiState.Success;
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }

    /** Solo para previews y tests */
    internal fun setStateForPreview(state: LoginUiState) {
        _uiState.value = state
    }
}

