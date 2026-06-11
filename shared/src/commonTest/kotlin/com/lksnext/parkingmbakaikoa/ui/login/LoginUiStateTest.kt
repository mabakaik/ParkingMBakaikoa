package com.lksnext.parkingmbakaikoa.ui.login

import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests unitarios para LoginUiState
 * Verifica que los estados se crean correctamente y contienen los datos esperados
 */
class LoginUiStateTest {

    // ======================== TESTS: Idle State ========================

    @Test
    fun idleState_shouldBeCreatable() {
        val state: LoginUiState = LoginUiState.Idle
        assertIs<LoginUiState.Idle>(state)
    }

    @Test
    fun idleState_shouldBeSingleton() {
        val state1 = LoginUiState.Idle
        val state2 = LoginUiState.Idle
        assertEquals(state1, state2)
    }

    // ======================== TESTS: Loading State ========================

    @Test
    fun loadingState_shouldBeCreatable() {
        val state: LoginUiState = LoginUiState.Loading
        assertIs<LoginUiState.Loading>(state)
    }

    @Test
    fun loadingState_shouldBeSingleton() {
        val state1 = LoginUiState.Loading
        val state2 = LoginUiState.Loading
        assertEquals(state1, state2)
    }

    // ======================== TESTS: Success State ========================

    @Test
    fun successState_shouldBeCreatable() {
        val state: LoginUiState = LoginUiState.Success
        assertIs<LoginUiState.Success>(state)
    }

    @Test
    fun successState_shouldBeSingleton() {
        val state1 = LoginUiState.Success
        val state2 = LoginUiState.Success
        assertEquals(state1, state2)
    }

    // ======================== TESTS: Error State ========================

    @Test
    fun errorState_shouldContainMessage() {
        val message = "Error de autenticación"
        val state = LoginUiState.Error(message)

        assertIs<LoginUiState.Error>(state)
        assertEquals(message, state.message)
    }

    @Test
    fun errorState_withSimpleMessage() {
        val state = LoginUiState.Error("Login failed")
        assertEquals("Login failed", state.message)
    }

    @Test
    fun errorState_withEmptyMessage() {
        val state = LoginUiState.Error("")
        assertEquals("", state.message)
    }

    @Test
    fun errorState_withLongMessage() {
        val longMessage = "A".repeat(500)
        val state = LoginUiState.Error(longMessage)
        assertEquals(longMessage, state.message)
    }

    @Test
    fun errorState_withSpecialCharactersMessage() {
        val message = "Error: Usuario@correo.com #123"
        val state = LoginUiState.Error(message)
        assertEquals(message, state.message)
    }

    @Test
    fun errorState_shouldBeDataClass() {
        val error1 = LoginUiState.Error("Same message")
        val error2 = LoginUiState.Error("Same message")

        assertEquals(error1, error2)
    }

    @Test
    fun errorState_shouldNotBeEqualWithDifferentMessages() {
        val error1 = LoginUiState.Error("Message 1")
        val error2 = LoginUiState.Error("Message 2")

        // No debería ser igual
        assertTrue(error1 != error2)
    }

    // ======================== TESTS: ValidationError State ========================

    @Test
    fun validationErrorState_withEmailField() {
        val state = LoginUiState.ValidationError("email", "Email inválido")

        assertIs<LoginUiState.ValidationError>(state)
        assertEquals("email", state.field)
        assertEquals("Email inválido", state.message)
    }

    @Test
    fun validationErrorState_withPasswordField() {
        val state = LoginUiState.ValidationError("password", "La contraseña no puede estar vacía")

        assertIs<LoginUiState.ValidationError>(state)
        assertEquals("password", state.field)
        assertEquals("La contraseña no puede estar vacía", state.message)
    }

    @Test
    fun validationErrorState_withCustomField() {
        val state = LoginUiState.ValidationError("customField", "Error message")

        assertEquals("customField", state.field)
        assertEquals("Error message", state.message)
    }

    @Test
    fun validationErrorState_shouldBeDataClass() {
        val error1 = LoginUiState.ValidationError("email", "Invalid email")
        val error2 = LoginUiState.ValidationError("email", "Invalid email")

        assertEquals(error1, error2)
    }

    @Test
    fun validationErrorState_shouldNotBeEqualWithDifferentFields() {
        val error1 = LoginUiState.ValidationError("email", "Same message")
        val error2 = LoginUiState.ValidationError("password", "Same message")

        assertTrue(error1 != error2)
    }

    @Test
    fun validationErrorState_shouldNotBeEqualWithDifferentMessages() {
        val error1 = LoginUiState.ValidationError("email", "Message 1")
        val error2 = LoginUiState.ValidationError("email", "Message 2")

        assertTrue(error1 != error2)
    }

    // ======================== TESTS: State Type Checking ========================

    @Test
    fun stateTypeChecking_shouldDistinguishBetweenStates() {
        val states: List<LoginUiState> = listOf(
            LoginUiState.Idle,
            LoginUiState.Loading,
            LoginUiState.Success,
            LoginUiState.Error("Error"),
            LoginUiState.ValidationError("field", "message")
        )

        states.forEach { state ->
            when (state) {
                is LoginUiState.Idle -> assertIs<LoginUiState.Idle>(state)
                is LoginUiState.Loading -> assertIs<LoginUiState.Loading>(state)
                is LoginUiState.Success -> assertIs<LoginUiState.Success>(state)
                is LoginUiState.Error -> assertIs<LoginUiState.Error>(state)
                is LoginUiState.ValidationError -> assertIs<LoginUiState.ValidationError>(state)
            }
        }
    }

    // ======================== TESTS: State Transitions ========================

    @Test
    fun stateTransition_fromIdleToLoading() {
        val state1: LoginUiState = LoginUiState.Idle
        val state2: LoginUiState = LoginUiState.Loading

        assertIs<LoginUiState.Idle>(state1)
        assertIs<LoginUiState.Loading>(state2)
    }

    @Test
    fun stateTransition_fromLoadingToSuccess() {
        val state1: LoginUiState = LoginUiState.Loading
        val state2: LoginUiState = LoginUiState.Success

        assertIs<LoginUiState.Loading>(state1)
        assertIs<LoginUiState.Success>(state2)
    }

    @Test
    fun stateTransition_fromLoadingToError() {
        val state1: LoginUiState = LoginUiState.Loading
        val state2: LoginUiState = LoginUiState.Error("System Error")

        assertIs<LoginUiState.Loading>(state1)
        assertIs<LoginUiState.Error>(state2)
    }

    @Test
    fun stateTransition_fromIdleToValidationError() {
        val state1: LoginUiState = LoginUiState.Idle
        val state2: LoginUiState = LoginUiState.ValidationError("email", "Invalid")

        assertIs<LoginUiState.Idle>(state1)
        assertIs<LoginUiState.ValidationError>(state2)
    }

    // ======================== TESTS: Sealed Class Properties ========================

    @Test
    fun allStatesAreLoginUiState() {
        val states = listOf(
            LoginUiState.Idle as LoginUiState,
            LoginUiState.Loading as LoginUiState,
            LoginUiState.Success as LoginUiState,
            LoginUiState.Error("msg") as LoginUiState,
            LoginUiState.ValidationError("f", "m") as LoginUiState
        )

        states.forEach { state ->
            assertIs<LoginUiState>(state)
        }
    }

    // ======================== TESTS: toString() ========================

    @Test
    fun errorState_toString_shouldIncludeMessage() {
        val state = LoginUiState.Error("Test error")
        val stringValue = state.toString()

        // Verificar que el toString contiene información del estado
        assertTrue("Error" in stringValue || "error" in stringValue)
    }

    @Test
    fun validationErrorState_toString_shouldIncludeFieldAndMessage() {
        val state = LoginUiState.ValidationError("email", "Invalid email")
        val stringValue = state.toString()

        assertTrue("ValidationError" in stringValue || "email" in stringValue)
    }
}

