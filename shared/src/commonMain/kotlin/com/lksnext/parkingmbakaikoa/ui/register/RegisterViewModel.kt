package com.lksnext.parkingmbakaikoa.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lksnext.parkingmbakaikoa.data.repository.AuthRepository
import com.lksnext.parkingmbakaikoa.ui.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(firstName: String, lastName: String, email: String, password: String) {

        if (!ValidationUtils.isValidName(firstName)) {
            _uiState.value = RegisterUiState.ValidationError("firstName", "El nombre debe tener al menos 2 caracteres")
            return
        }

        if (!ValidationUtils.isValidName(lastName)) {
            _uiState.value = RegisterUiState.ValidationError("lastName", "Los apellidos deben tener al menos 2 caracteres")
            return
        }

        if (!ValidationUtils.isValidEmail(email)) {
            _uiState.value = RegisterUiState.ValidationError("email", "Email inválido")
            return
        }

        if (!ValidationUtils.isStrongPassword(password)) {
            _uiState.value = RegisterUiState.ValidationError(
                "password",
                "La contraseña debe tener mínimo 8 caracteres, una mayúscula, un número y un símbolo especial"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading

            val registerResult = authRepository.register(firstName, lastName, email, password)
            
            registerResult.fold(
                onSuccess = {
                    val loginResult = authRepository.login(email, password)
                    _uiState.value = loginResult.fold(
                        onSuccess = { RegisterUiState.Success },
                        onFailure = { 
                            RegisterUiState.Error("Registro exitoso pero error al iniciar sesión: ${it.message ?: "Error desconocido"}")
                        }
                    )
                },
                onFailure = {
                    _uiState.value = RegisterUiState.Error(it.message ?: "Error en el registro")
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = RegisterUiState.Idle
    }

    /** Solo para previews y tests */
    internal fun setStateForPreview(state: RegisterUiState) {
        _uiState.value = state
    }
}

