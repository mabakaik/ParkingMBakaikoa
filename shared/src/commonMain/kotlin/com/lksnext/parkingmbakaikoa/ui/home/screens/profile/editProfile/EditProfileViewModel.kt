package com.lksnext.parkingmbakaikoa.ui.home.screens.profile.editProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lksnext.parkingmbakaikoa.data.models.User
import com.lksnext.parkingmbakaikoa.data.repository.UserRepository
import com.lksnext.parkingmbakaikoa.ui.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private var currentUser: User? = null

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = userRepository.getCurrentUser()
            result.onSuccess { user ->
                currentUser = user
                _uiState.update {
                    it.copy(
                        firstName = user.firstName,
                        lastName = user.lastName,
                        email = user.email,
                        isLoading = false
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        emailError = exception.message ?: "Error al cargar usuario"
                    )
                }
            }
        }
    }

    fun onFirstNameChange(firstName: String) {
        _uiState.update {
            it.copy(
                firstName = firstName,
                firstNameError = validateFirstName(firstName)
            )
        }
    }

    fun onLastNameChange(lastName: String) {
        _uiState.update {
            it.copy(
                lastName = lastName,
                lastNameError = validateLastName(lastName)
            )
        }
    }

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = validateEmail(email)
            )
        }
    }

    fun saveProfile() {
        val state = _uiState.value
        val user = currentUser ?: return

        // Validar todos los campos
        val firstNameError = validateFirstName(state.firstName)
        val lastNameError = validateLastName(state.lastName)
        val emailError = validateEmail(state.email)

        if (firstNameError != null || lastNameError != null || emailError != null) {
            _uiState.update {
                it.copy(
                    firstNameError = firstNameError,
                    lastNameError = lastNameError,
                    emailError = emailError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val updatedUser = user.copy(
                firstName = state.firstName.trim(),
                lastName = state.lastName.trim(),
                email = state.email.trim()
            )

            val result = userRepository.updateUser(updatedUser)
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSaved = true) }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        emailError = exception.message ?: "Error al guardar"
                    )
                }
            }
        }
    }

    private fun validateFirstName(firstName: String): String? {
        return when {
            firstName.isBlank() -> "El nombre es obligatorio"
            !ValidationUtils.isValidName(firstName) -> "El nombre debe tener al menos 2 caracteres"
            else -> null
        }
    }

    private fun validateLastName(lastName: String): String? {
        return when {
            lastName.isBlank() -> "Los apellidos son obligatorios"
            !ValidationUtils.isValidName(lastName) -> "Los apellidos deben tener al menos 2 caracteres"
            else -> null
        }
    }

    private fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "El email es obligatorio"
            !ValidationUtils.isValidEmail(email) -> "Email inválido"
            else -> null
        }
    }
}


