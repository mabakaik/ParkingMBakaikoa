package com.lksnext.parkingmbakaikoa.ui.register

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
import parkingmbakaikoa.shared.generated.resources.firstNameMinLength
import parkingmbakaikoa.shared.generated.resources.invalidEmail
import parkingmbakaikoa.shared.generated.resources.lastNameMinLength
import parkingmbakaikoa.shared.generated.resources.registerError
import parkingmbakaikoa.shared.generated.resources.weakPassword

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog: StateFlow<Boolean> = _showSuccessDialog.asStateFlow()

    private val _navigateToLogin = MutableStateFlow(false)
    val navigateToLogin: StateFlow<Boolean> = _navigateToLogin.asStateFlow()

    fun register(firstName: String, lastName: String, email: String, password: String) {
        viewModelScope.launch {
            if (!ValidationUtils.isValidName(firstName)) {
                _uiState.value = RegisterUiState.ValidationError("firstName", getString(Res.string.firstNameMinLength))
                return@launch
            }

            if (!ValidationUtils.isValidName(lastName)) {
                _uiState.value = RegisterUiState.ValidationError("lastName", getString(Res.string.lastNameMinLength))
                return@launch
            }

            if (!ValidationUtils.isValidEmail(email)) {
                _uiState.value = RegisterUiState.ValidationError("email", getString(Res.string.invalidEmail))
                return@launch
            }

            if (!ValidationUtils.isStrongPassword(password)) {
                _uiState.value = RegisterUiState.ValidationError(
                    "password",
                    getString(Res.string.weakPassword)
                )
                return@launch
            }

            _uiState.value = RegisterUiState.Loading

            val registerResult = authRepository.register(firstName, lastName, email, password)

            registerResult.fold(
                onSuccess = {
                    _uiState.value = RegisterUiState.Success
                    _showSuccessDialog.value = true
                },
                onFailure = {
                    _uiState.value = RegisterUiState.Error(it.message ?: getString(Res.string.registerError))
                }
            )
        }
    }

    fun onSuccessDialogConfirm() {
        _showSuccessDialog.value = false
        _navigateToLogin.value = true
    }

    fun onNavigationComplete() {
        _navigateToLogin.value = false
        resetState()
    }

    fun resetState() {
        _uiState.value = RegisterUiState.Idle
    }

    /** Solo para previews y tests */
    internal fun setStateForPreview(state: RegisterUiState) {
        _uiState.value = state
    }
}

