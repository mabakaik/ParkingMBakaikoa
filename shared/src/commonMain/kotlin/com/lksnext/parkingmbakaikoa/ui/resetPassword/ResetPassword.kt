package com.lksnext.parkingmbakaikoa.ui.resetPassword

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lksnext.parkingmbakaikoa.data.repository.AuthRepository
import com.lksnext.parkingmbakaikoa.ui.navigation.Routes
import com.lksnext.parkingmbakaikoa.ui.theme.PrimaryColor
import com.lksnext.parkingmbakaikoa.ui.theme.SecondaryColor
import org.jetbrains.compose.resources.stringResource
import parkingmbakaikoa.shared.generated.resources.Res
import parkingmbakaikoa.shared.generated.resources.doYouRemember
import parkingmbakaikoa.shared.generated.resources.email
import parkingmbakaikoa.shared.generated.resources.emailSent
import parkingmbakaikoa.shared.generated.resources.login
import parkingmbakaikoa.shared.generated.resources.resetPassword
import parkingmbakaikoa.shared.generated.resources.resetPasswordSubtitle
import parkingmbakaikoa.shared.generated.resources.sendEmail

@Composable
fun ResetPasswordScreen(navController: NavController, authRepository: AuthRepository) {
    val viewModel = viewModel { ResetPasswordViewModel(authRepository) }
    val uiState by viewModel.uiState.collectAsState()

    var email by remember { mutableStateOf("") }

    val isLoading = uiState is ResetPasswordUiState.Loading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text(
                text = stringResource(Res.string.resetPassword),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(Res.string.resetPasswordSubtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = SecondaryColor,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(Res.string.email)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
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


            when(uiState){
                is ResetPasswordUiState.ValidationError -> Text(
                    text = (uiState as ResetPasswordUiState.ValidationError).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
                is ResetPasswordUiState.Error -> Text(
                    text = (uiState as ResetPasswordUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
                is ResetPasswordUiState.Success -> Text(
                    text = stringResource(Res.string.emailSent),
                    color = PrimaryColor,
                    style = MaterialTheme.typography.bodySmall
                )
                else -> {}
            }
            Spacer(modifier = Modifier.height(12.dp))

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = PrimaryColor
                )
            } else {
                Button(
                    onClick = { viewModel.resetPassword(email) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        stringResource(Res.string.sendEmail),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(
                    text = stringResource(Res.string.doYouRemember),
                    color = SecondaryColor,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(Res.string.login),
                    color = PrimaryColor,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.clickable {
                        viewModel.resetState()
                        navController.navigate(Routes.Login.name)
                    }
                )
            }
        }
    }
}
