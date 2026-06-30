package com.lksnext.parkingmbakaikoa.ui.home.screens.profile.editProfile

data class EditProfileUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)

