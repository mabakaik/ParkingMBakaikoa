package com.lksnext.parkingmbakaikoa.ui.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.lksnext.parkingmbakaikoa.ui.theme.PrimaryColor
import com.lksnext.parkingmbakaikoa.ui.theme.SecondaryColor
import org.jetbrains.compose.resources.stringResource
import parkingmbakaikoa.shared.generated.resources.Res
import parkingmbakaikoa.shared.generated.resources.appName
import parkingmbakaikoa.shared.generated.resources.register
import parkingmbakaikoa.shared.generated.resources.firstName
import parkingmbakaikoa.shared.generated.resources.lastName
import parkingmbakaikoa.shared.generated.resources.email
import parkingmbakaikoa.shared.generated.resources.password
import parkingmbakaikoa.shared.generated.resources.createAccount
import parkingmbakaikoa.shared.generated.resources.haveAccount
import parkingmbakaikoa.shared.generated.resources.login

import androidx.compose.foundation.clickable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lksnext.parkingmbakaikoa.data.repository.AuthRepository
import com.lksnext.parkingmbakaikoa.ui.navigation.Routes

@Composable
fun RegisterScreen(navController: NavController, authRepository: AuthRepository) {
    val viewModel = viewModel { RegisterViewModel(authRepository) }
    val uiState by viewModel.uiState.collectAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading = uiState is RegisterUiState.Loading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 40.dp)
        ) {
            Text(
                text = stringResource(Res.string.appName),
                style = MaterialTheme.typography.headlineLarge,
                color = PrimaryColor,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(Res.string.createAccount),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text(stringResource(Res.string.firstName)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryColor,
                    focusedLabelColor = PrimaryColor,
                    cursorColor = PrimaryColor
                ),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(stringResource(Res.string.lastName)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryColor,
                    focusedLabelColor = PrimaryColor,
                    cursorColor = PrimaryColor
                ),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(Res.string.email)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryColor,
                    focusedLabelColor = PrimaryColor,
                    cursorColor = PrimaryColor
                ),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(Res.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryColor,
                    focusedLabelColor = PrimaryColor,
                    cursorColor = PrimaryColor
                ),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (uiState is RegisterUiState.ValidationError) {
                Text(
                    text = (uiState as RegisterUiState.ValidationError).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            } else if (uiState is RegisterUiState.Error) {
                Text(
                    text = (uiState as RegisterUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = PrimaryColor
                )
            } else {
                Button(
                    onClick = { viewModel.register(firstName, lastName, email, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        stringResource(Res.string.register),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(
                    text = stringResource(Res.string.haveAccount),
                    color = SecondaryColor,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(Res.string.login),
                    color = PrimaryColor,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.clickable{
                        viewModel.resetState()
                        navController.navigate(Routes.Login.name)
                    }
                )
            }
        }
    }
}



