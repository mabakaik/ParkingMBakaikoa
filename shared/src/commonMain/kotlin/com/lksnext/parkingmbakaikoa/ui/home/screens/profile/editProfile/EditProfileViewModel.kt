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
import org.jetbrains.compose.resources.getString
import parkingmbakaikoa.shared.generated.resources.Res
import parkingmbakaikoa.shared.generated.resources.emailRequired
import parkingmbakaikoa.shared.generated.resources.firstNameMinLength
import parkingmbakaikoa.shared.generated.resources.firstNameRequired
import parkingmbakaikoa.shared.generated.resources.invalidEmail
import parkingmbakaikoa.shared.generated.resources.lastNameMinLength
import parkingmbakaikoa.shared.generated.resources.lastNameRequired
import parkingmbakaikoa.shared.generated.resources.loadUserError
import parkingmbakaikoa.shared.generated.resources.saveError

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
                        emailError = exception.message ?: getString(Res.string.loadUserError)
                    )
                }
            }
        }
    }

    fun onFirstNameChange(firstName: String) {
        viewModelScope.launch {
            val error = validateFirstName(firstName)
            _uiState.update {
                it.copy(
                    firstName = firstName,
                    firstNameError = error
                )
            }
        }
    }

    fun onLastNameChange(lastName: String) {
        viewModelScope.launch {
            val error = validateLastName(lastName)
            _uiState.update {
                it.copy(
                    lastName = lastName,
                    lastNameError = error
                )
            }
        }
    }

    fun onEmailChange(email: String) {
        viewModelScope.launch {
            val error = validateEmail(email)
            _uiState.update {
                it.copy(
                    email = email,
                    emailError = error
                )
            }
        }
    }

    fun saveProfile() {
        val state = _uiState.value
        val user = currentUser ?: return

        viewModelScope.launch {
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
                return@launch
            }

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
                        emailError = exception.message ?: getString(Res.string.saveError)
                    )
                }
            }
        }
    }

    private suspend fun validateFirstName(firstName: String): String? {
        return when {
            firstName.isBlank() -> getString(Res.string.firstNameRequired)
            !ValidationUtils.isValidName(firstName) -> getString(Res.string.firstNameMinLength)
            else -> null
        }
    }

    private suspend fun validateLastName(lastName: String): String? {
        return when {
            lastName.isBlank() -> getString(Res.string.lastNameRequired)
            !ValidationUtils.isValidName(lastName) -> getString(Res.string.lastNameMinLength)
            else -> null
        }
    }

    private suspend fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> getString(Res.string.emailRequired)
            !ValidationUtils.isValidEmail(email) -> getString(Res.string.invalidEmail)
            else -> null
        }
    }
}


