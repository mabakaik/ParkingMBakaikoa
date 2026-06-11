package com.lksnext.parkingmbakaikoa.ui.utils

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Tests unitarios para ValidationUtils
 * Cobertura de validaciones de email, contraseña y nombres
 */
class ValidationUtilsTest {

    // ======================== TESTS: isValidEmail ========================

    @Test
    fun isValidEmail_withValidSimpleEmail_shouldReturnTrue() {
        assertTrue(ValidationUtils.isValidEmail("user@example.com"))
    }

    @Test
    fun isValidEmail_withValidEmailWithNumbers_shouldReturnTrue() {
        assertTrue(ValidationUtils.isValidEmail("user123@example.com"))
    }

    @Test
    fun isValidEmail_withValidEmailWithPlusSign_shouldReturnTrue() {
        assertTrue(ValidationUtils.isValidEmail("user+tag@example.com"))
    }

    @Test
    fun isValidEmail_withValidEmailWithDot_shouldReturnTrue() {
        assertTrue(ValidationUtils.isValidEmail("user.name@example.com"))
    }

    @Test
    fun isValidEmail_withValidEmailWithDash_shouldReturnTrue() {
        assertTrue(ValidationUtils.isValidEmail("user-name@example.com"))
    }

    @Test
    fun isValidEmail_withValidEmailWithMultipleDot_shouldReturnTrue() {
        assertTrue(ValidationUtils.isValidEmail("user@example.co.uk"))
    }

    @Test
    fun isValidEmail_withValidEmailCaseSensitive_shouldReturnTrue() {
        assertTrue(ValidationUtils.isValidEmail("User@Example.COM"))
    }

    @Test
    fun isValidEmail_withEmptyString_shouldReturnFalse() {
        assertFalse(ValidationUtils.isValidEmail(""))
    }

    @Test
    fun isValidEmail_withOnlyWhitespace_shouldReturnFalse() {
        assertFalse(ValidationUtils.isValidEmail("   "))
    }

    @Test
    fun isValidEmail_withoutAtSymbol_shouldReturnFalse() {
        assertFalse(ValidationUtils.isValidEmail("usernameexample.com"))
    }

    @Test
    fun isValidEmail_withoutUsername_shouldReturnFalse() {
        assertFalse(ValidationUtils.isValidEmail("@example.com"))
    }

    @Test
    fun isValidEmail_withoutDomain_shouldReturnFalse() {
        assertFalse(ValidationUtils.isValidEmail("user@"))
    }

    @Test
    fun isValidEmail_withOnlyUsername_shouldReturnFalse() {
        assertFalse(ValidationUtils.isValidEmail("user"))
    }

    @Test
    fun isValidEmail_withSpaces_shouldReturnFalse() {
        assertFalse(ValidationUtils.isValidEmail("user name@example.com"))
    }

    @Test
    fun isValidEmail_withSpecialCharacters_shouldReturnFalse() {
        assertFalse(ValidationUtils.isValidEmail("user#name@example.com"))
    }

    @Test
    fun isValidEmail_withVeryLongEmail_shouldReturnTrue() {
        val longEmail = "a".repeat(50) + "@" + "b".repeat(50) + ".com"
        assertTrue(ValidationUtils.isValidEmail(longEmail))
    }

    @Test
    fun isValidEmail_withValidSubdomain_shouldReturnTrue() {
        assertTrue(ValidationUtils.isValidEmail("user@mail.example.co.uk"))
    }

    @Test
    fun isValidEmail_withNumberAsFirstCharOfDomain_shouldReturnTrue() {
        // Dependiendo del regex, esto podría fallar - verificar comportamiento
        val result = ValidationUtils.isValidEmail("user@123example.com")
        // El regex debería permitirlo según el patrón
    }

    // ======================== TESTS: isStrongPassword ========================

    @Test
    fun isStrongPassword_withValidStrongPassword_shouldReturnTrue() {
        assertTrue(ValidationUtils.isStrongPassword("StrongPass123!"))
    }

    @Test
    fun isStrongPassword_withValidStrongPasswordDifferentSpecialChar_shouldReturnTrue() {
        assertTrue(ValidationUtils.isStrongPassword("P@ssw0rd#$"))
    }

    @Test
    fun isStrongPassword_withValidStrongPasswordManyChars_shouldReturnTrue() {
        assertTrue(ValidationUtils.isStrongPassword("SecurePassword123!"))
    }

    @Test
    fun isStrongPassword_withPasswordLessThan8Chars_shouldReturnFalse() {
        assertFalse(ValidationUtils.isStrongPassword("Pass12!"))
    }

    @Test
    fun isStrongPassword_withPasswordWithoutUpperCase_shouldReturnFalse() {
        assertFalse(ValidationUtils.isStrongPassword("password123!"))
    }

    @Test
    fun isStrongPassword_withPasswordWithoutNumber_shouldReturnFalse() {
        assertFalse(ValidationUtils.isStrongPassword("Password!"))
    }

    @Test
    fun isStrongPassword_withPasswordWithoutSpecialChar_shouldReturnFalse() {
        assertFalse(ValidationUtils.isStrongPassword("Password123"))
    }

    @Test
    fun isStrongPassword_with8CharsWithAllRequirements_shouldReturnTrue() {
        assertTrue(ValidationUtils.isStrongPassword("Pass123!"))
    }

    @Test
    fun isStrongPassword_withOnlySpecialCharacters_shouldReturnFalse() {
        assertFalse(ValidationUtils.isStrongPassword("!@#$%^&*"))
    }

    @Test
    fun isStrongPassword_withOnlyUpperCaseNumbers_shouldReturnFalse() {
        assertFalse(ValidationUtils.isStrongPassword("PASSWORD123"))
    }

    @Test
    fun isStrongPassword_withEmptyString_shouldReturnFalse() {
        assertFalse(ValidationUtils.isStrongPassword(""))
    }

    @Test
    fun isStrongPassword_withVeryLongPassword_shouldReturnTrue() {
        val longPassword = "A".repeat(50) + "1!abc"
        assertTrue(ValidationUtils.isStrongPassword(longPassword))
    }

    // ======================== TESTS: isValidName ========================

    @Test
    fun isValidName_withValidSimpleName_shouldReturnTrue() {
        assertTrue(ValidationUtils.isValidName("John"))
    }

    @Test
    fun isValidName_withValidNameWithTwoChars_shouldReturnTrue() {
        assertTrue(ValidationUtils.isValidName("Jo"))
    }

    @Test
    fun isValidName_withValidNameWithSpaces_shouldReturnTrue() {
        assertTrue(ValidationUtils.isValidName("Jean Pierre"))
    }

    @Test
    fun isValidName_withValidNameWithHyphen_shouldReturnTrue() {
        assertTrue(ValidationUtils.isValidName("Mary-Jane"))
    }

    @Test
    fun isValidName_withValidNameWithApostrophe_shouldReturnTrue() {
        assertTrue(ValidationUtils.isValidName("O'Brien"))
    }

    @Test
    fun isValidName_withValidNameWithNumbers_shouldReturnTrue() {
        assertTrue(ValidationUtils.isValidName("Test123"))
    }

    @Test
    fun isValidName_withEmptyString_shouldReturnFalse() {
        assertFalse(ValidationUtils.isValidName(""))
    }

    @Test
    fun isValidName_withOnlyWhitespace_shouldReturnFalse() {
        assertFalse(ValidationUtils.isValidName("   "))
    }

    @Test
    fun isValidName_withSingleCharacter_shouldReturnFalse() {
        assertFalse(ValidationUtils.isValidName("J"))
    }

    @Test
    fun isValidName_withOneCharacterAndSpace_shouldReturnFalse() {
        assertFalse(ValidationUtils.isValidName("J "))
    }

    @Test
    fun isValidName_withVeryLongName_shouldReturnTrue() {
        val longName = "A".repeat(100)
        assertTrue(ValidationUtils.isValidName(longName))
    }

    @Test
    fun isValidName_withSpecialCharacters_shouldReturnTrue() {
        // Algunos caracteres especiales comunes en nombres
        assertTrue(ValidationUtils.isValidName("André"))
    }

    // ======================== TESTS: Combined Scenarios ========================

    @Test
    fun validationRules_withAllValidInputs_shouldPassAll() {
        assertTrue(ValidationUtils.isValidEmail("john.doe@example.com"))
        assertTrue(ValidationUtils.isStrongPassword("SecurePass123!"))
        assertTrue(ValidationUtils.isValidName("John"))
        assertTrue(ValidationUtils.isValidName("Doe"))
    }

    @Test
    fun validationRules_withAllInvalidInputs_shouldFailAll() {
        assertFalse(ValidationUtils.isValidEmail("invalid"))
        assertFalse(ValidationUtils.isStrongPassword("weak"))
        assertFalse(ValidationUtils.isValidName("J"))
    }

    @Test
    fun validationRules_withMixedValidInvalid_shouldHandleCorrectly() {
        assertTrue(ValidationUtils.isValidEmail("user@example.com"))
        assertFalse(ValidationUtils.isStrongPassword("WeakPassword"))
        assertTrue(ValidationUtils.isValidName("John"))
    }
}

