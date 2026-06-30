package com.lksnext.parkingmbakaikoa.ui.utils

object ValidationUtils {
    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
        return email.isNotEmpty() && emailRegex.matches(email)
    }

    fun isStrongPassword(password: String): Boolean {
        if (password.length < 8) return false
        
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasNumber = password.any { it.isDigit() }
        val hasSpecialChar = password.any { it in "!@#$%^&*()-_=+[]{}|;:,.<>?" }
        
        return hasUpperCase && hasNumber && hasSpecialChar
    }

    fun isValidName(name: String): Boolean {
        return name.isNotBlank() && name.trim().length >= 2
    }

    fun isValidPlate(plate: String): Boolean {
        return plate.isNotBlank() && plate.trim().length >= 3
    }
}

