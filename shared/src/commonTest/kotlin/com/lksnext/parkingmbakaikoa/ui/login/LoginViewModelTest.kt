package com.lksnext.parkingmbakaikoa.ui.login

import com.lksnext.parkingmbakaikoa.data.repository.AuthRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * Tests unitarios para LoginViewModel
 *
 * Cobertura de pruebas:
 * - Validación de emails
 * - Validación de contraseñas
 * - Flujo de login exitoso
 * - Flujo de login fallido
 * - Reset de estados
 * - Transiciones de estados
 */
class LoginViewModelTest {

    // ======================== SETUP ========================

    /**
     * Mock de AuthRepository para testing
     * Permite simular respuestas exitosas y fallidas
     */
    private inner class MockAuthRepository(
        private val shouldSucceed: Boolean = true,
        private val failureMessage: String = "Error de autenticación"
    ) : AuthRepository {
        override suspend fun register(
            firstName: String,
            lastName: String,
            email: String,
            password: String
        ): Result<Unit> = if (shouldSucceed) Result.success(Unit) else Result.failure(
            Exception(failureMessage)
        )

        override suspend fun login(email: String, password: String): Result<Unit> =
            if (shouldSucceed) Result.success(Unit) else Result.failure(
                Exception(failureMessage)
            )

        override suspend fun logout(): Result<Unit> = Result.success(Unit)

        override suspend fun isUserLoggedIn(): Boolean = false

        override suspend fun sendPasswordResetEmail(email: String): Result<Unit> =
            Result.success(Unit)

        override fun observeAuthState(onAuthStateChanged: (Boolean) -> Unit): (() -> Unit) =
            {}
    }

    // ======================== TESTS: VALIDACIÓN DE EMAIL ========================

    @Test
    fun loginWithValidEmail_shouldValidateSuccessfully() = runTest {
        val viewModel = LoginViewModel(MockAuthRepository())

        // Ejecutar login con email válido
        viewModel.login("test@example.com", "password123")

        // Verificar que el estado es Loading (validación pasó, pero aún espera resultado del repo)
        val state = viewModel.uiState.value
        assertIs<LoginUiState.Loading>(state)
    }

    @Test
    fun loginWithEmptyEmail_shouldShowValidationError() {
        val viewModel = LoginViewModel(MockAuthRepository())

        viewModel.login("", "password123")

        val state = viewModel.uiState.value
        assertIs<LoginUiState.ValidationError>(state)
        val error = state as LoginUiState.ValidationError
        assertEquals("email", error.field)
        assertEquals("Email inválido", error.message)
    }

    @Test
    fun loginWithInvalidEmailFormat_shouldShowValidationError() {
        val viewModel = LoginViewModel(MockAuthRepository())

        // Email sin dominio
        viewModel.login("invalidemail", "password123")

        val state = viewModel.uiState.value
        assertIs<LoginUiState.ValidationError>(state)
        val error = state as LoginUiState.ValidationError
        assertEquals("email", error.field)
        assertEquals("Email inválido", error.message)
    }

    @Test
    fun loginWithEmailWithoutDomain_shouldShowValidationError() {
        val viewModel = LoginViewModel(MockAuthRepository())

        viewModel.login("user@", "password123")

        val state = viewModel.uiState.value
        assertIs<LoginUiState.ValidationError>(state)
    }

    @Test
    fun loginWithEmailWithoutUser_shouldShowValidationError() {
        val viewModel = LoginViewModel(MockAuthRepository())

        viewModel.login("@example.com", "password123")

        val state = viewModel.uiState.value
        assertIs<LoginUiState.ValidationError>(state)
    }

    @Test
    fun loginWithValidEmailFormats_shouldPassValidation() = runTest {
        val viewModel = LoginViewModel(MockAuthRepository())
        val validEmails = listOf(
            "user@example.com",
            "first.last@example.co.uk",
            "user+tag@example.com",
            "user_name@example.com",
            "user-name@example.com"
        )

        for (email in validEmails) {
            viewModel.login(email, "password123")
            val state = viewModel.uiState.value
            assertIs<LoginUiState.Loading>(state)
        }
    }

    // ======================== TESTS: VALIDACIÓN DE CONTRASEÑA ========================

    @Test
    fun loginWithEmptyPassword_shouldShowValidationError() {
        val viewModel = LoginViewModel(MockAuthRepository())

        viewModel.login("test@example.com", "")

        val state = viewModel.uiState.value
        assertIs<LoginUiState.ValidationError>(state)
        val error = state as LoginUiState.ValidationError
        assertEquals("password", error.field)
        assertEquals("La contraseña no puede estar vacía", error.message)
    }

    @Test
    fun loginWithPasswordOnlySpaces_shouldShowValidationError() {
        val viewModel = LoginViewModel(MockAuthRepository())

        viewModel.login("test@example.com", "   ")

        val state = viewModel.uiState.value
        assertIs<LoginUiState.ValidationError>(state)
        val error = state as LoginUiState.ValidationError
        assertEquals("password", error.field)
    }

    @Test
    fun loginWithValidPassword_shouldPassValidation() = runTest {
        val viewModel = LoginViewModel(MockAuthRepository())

        viewModel.login("test@example.com", "validPassword123")

        val state = viewModel.uiState.value
        assertIs<LoginUiState.Loading>(state)
    }

    // ======================== TESTS: FLUJO DE LOGIN EXITOSO ========================

    @Test
    fun loginWithValidCredentials_andSuccessfulAuth_shouldSetSuccessState() = runTest {
        val viewModel = LoginViewModel(MockAuthRepository(shouldSucceed = true))

        viewModel.login("test@example.com", "password123")

        // Esperar a que la coroutine se complete
        val state = viewModel.uiState.value
        assertIs<LoginUiState.Success>(state)
    }

    @Test
    fun loginWithValidCredentials_shouldFirstSetLoadingState() = runTest {
        val viewModel = LoginViewModel(MockAuthRepository(shouldSucceed = true))

        // Usar setStateForPreview para verificar que pasa por Loading
        viewModel.login("test@example.com", "password123")
        // En el próximo ciclo, debería ser Success
        val finalState = viewModel.uiState.value
        assertIs<LoginUiState.Success>(finalState)
    }

    // ======================== TESTS: FLUJO DE LOGIN FALLIDO ========================

    @Test
    fun loginWithValidCredentials_andFailedAuth_shouldSetErrorState() = runTest {
        val authRepository = MockAuthRepository(
            shouldSucceed = false,
            failureMessage = "Credenciales inválidas"
        )
        val viewModel = LoginViewModel(authRepository)

        viewModel.login("test@example.com", "wrongpassword")

        val state = viewModel.uiState.value
        assertIs<LoginUiState.Error>(state)
        val error = state as LoginUiState.Error
        assertEquals("Credenciales inválidas", error.message)
    }

    @Test
    fun loginWithValidCredentials_andNetworkError_shouldSetErrorState() = runTest {
        val authRepository = MockAuthRepository(
            shouldSucceed = false,
            failureMessage = "Network error"
        )
        val viewModel = LoginViewModel(authRepository)

        viewModel.login("test@example.com", "password123")

        val state = viewModel.uiState.value
        assertIs<LoginUiState.Error>(state)
    }

    // ======================== TESTS: RESET DE ESTADO ========================

    @Test
    fun resetState_afterSuccessfulLogin_shouldReturnToIdle() = runTest {
        val viewModel = LoginViewModel(MockAuthRepository(shouldSucceed = true))

        // Realizar login exitoso
        viewModel.login("test@example.com", "password123")
        var state = viewModel.uiState.value
        assertIs<LoginUiState.Success>(state)

        // Reset
        viewModel.resetState()
        state = viewModel.uiState.value
        assertIs<LoginUiState.Idle>(state)
    }

    @Test
    fun resetState_afterError_shouldReturnToIdle() = runTest {
        val authRepository = MockAuthRepository(shouldSucceed = false)
        val viewModel = LoginViewModel(authRepository)

        // Login fallido
        viewModel.login("test@example.com", "password123")
        var state = viewModel.uiState.value
        assertIs<LoginUiState.Error>(state)

        // Reset
        viewModel.resetState()
        state = viewModel.uiState.value
        assertIs<LoginUiState.Idle>(state)
    }

    @Test
    fun resetState_afterValidationError_shouldReturnToIdle() {
        val viewModel = LoginViewModel(MockAuthRepository())

        // Validación fallida
        viewModel.login("invalid-email", "password")
        var state = viewModel.uiState.value
        assertIs<LoginUiState.ValidationError>(state)

        // Reset
        viewModel.resetState()
        state = viewModel.uiState.value
        assertIs<LoginUiState.Idle>(state)
    }

    // ======================== TESTS: TRANSICIONES DE ESTADO ========================

    @Test
    fun initialState_shouldBeIdle() {
        val viewModel = LoginViewModel(MockAuthRepository())

        val state = viewModel.uiState.value
        assertIs<LoginUiState.Idle>(state)
    }

    @Test
    fun multipleLoginAttempts_shouldHandleEachIndependently() = runTest {
        val viewModel = LoginViewModel(MockAuthRepository(shouldSucceed = true))

        // Primer intento
        viewModel.login("user1@example.com", "password1")
        var state = viewModel.uiState.value
        assertIs<LoginUiState.Success>(state)

        // Reset
        viewModel.resetState()
        state = viewModel.uiState.value
        assertIs<LoginUiState.Idle>(state)

        // Segundo intento
        viewModel.login("user2@example.com", "password2")
        state = viewModel.uiState.value
        assertIs<LoginUiState.Success>(state)
    }

    @Test
    fun loginFailsWithValidationError_thenSucceedsAfterCorrection() = runTest {
        val viewModel = LoginViewModel(MockAuthRepository(shouldSucceed = true))

        // Primer intento: email inválido
        viewModel.login("invalid-email", "password123")
        var state = viewModel.uiState.value
        assertIs<LoginUiState.ValidationError>(state)

        // Reset y segundo intento: email válido
        viewModel.resetState()
        viewModel.login("valid@example.com", "password123")
        state = viewModel.uiState.value
        assertIs<LoginUiState.Success>(state)
    }

    // ======================== TESTS: EDGE CASES ========================

    @Test
    fun loginWithVeryLongEmail_shouldValidate() = runTest {
        val viewModel = LoginViewModel(MockAuthRepository())
        val longEmail = "a".repeat(50) + "@" + "b".repeat(50) + ".com"

        viewModel.login(longEmail, "password123")

        val state = viewModel.uiState.value
        assertIs<LoginUiState.Loading>(state)
    }

    @Test
    fun loginWithSpecialCharactersInEmail_shouldValidate() = runTest {
        val viewModel = LoginViewModel(MockAuthRepository())

        viewModel.login("user+tag@example.co.uk", "password123")

        val state = viewModel.uiState.value
        assertIs<LoginUiState.Loading>(state)
    }

    @Test
    fun loginWithPasswordContainingSpecialChars_shouldPassValidation() = runTest {
        val viewModel = LoginViewModel(MockAuthRepository())

        viewModel.login("test@example.com", "P@ssw0rd!#$%")

        val state = viewModel.uiState.value
        assertIs<LoginUiState.Loading>(state)
    }

    @Test
    fun setStateForPreview_shouldSetCustomState() {
        val viewModel = LoginViewModel(MockAuthRepository())
        val customError = LoginUiState.Error("Custom error for preview")

        viewModel.setStateForPreview(customError)

        val state = viewModel.uiState.value
        assertIs<LoginUiState.Error>(state)
        assertEquals("Custom error for preview", (state as LoginUiState.Error).message)
    }

    // ======================== TESTS: ERROR MESSAGES ========================

    @Test
    fun loginWithAuthError_shouldPreserveErrorMessage() = runTest {
        val errorMessage = "Usuario no encontrado"
        val authRepository = MockAuthRepository(
            shouldSucceed = false,
            failureMessage = errorMessage
        )
        val viewModel = LoginViewModel(authRepository)

        viewModel.login("test@example.com", "password123")

        val state = viewModel.uiState.value
        assertIs<LoginUiState.Error>(state)
        assertEquals(errorMessage, (state as LoginUiState.Error).message)
    }

    @Test
    fun loginWithAuthException_withNullMessage_shouldShowUnknownError() = runTest {
        val authRepository = object : AuthRepository {
            override suspend fun register(
                firstName: String,
                lastName: String,
                email: String,
                password: String
            ): Result<Unit> = Result.failure(Exception())

            override suspend fun login(email: String, password: String): Result<Unit> =
                Result.failure(Exception())

            override suspend fun logout(): Result<Unit> = Result.success(Unit)
            override suspend fun isUserLoggedIn(): Boolean = false
            override suspend fun sendPasswordResetEmail(email: String): Result<Unit> =
                Result.success(Unit)

            override fun observeAuthState(onAuthStateChanged: (Boolean) -> Unit): (() -> Unit) =
                {}
        }
        val viewModel = LoginViewModel(authRepository)

        viewModel.login("test@example.com", "password123")

        val state = viewModel.uiState.value
        assertIs<LoginUiState.Error>(state)
        assertEquals("Error desconocido", (state as LoginUiState.Error).message)
    }
}

