package com.iolandarosa.retailhub.features.auth.presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iolandarosa.retailhub.core.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import retailhub.shared.generated.resources.Res
import retailhub.shared.generated.resources.introduce_credentials_description
import retailhub.shared.generated.resources.password
import retailhub.shared.generated.resources.sign_in
import retailhub.shared.generated.resources.sign_in_to_continue
import retailhub.shared.generated.resources.username
import retailhub.shared.generated.resources.welcome_back

@Composable
fun LoginScreen(paddingValues: PaddingValues, viewModel: LoginViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondaryContainer,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )

        Column(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium),
        ) {
            Spacer(Modifier.weight(1f))
            Text(
                stringResource(Res.string.welcome_back),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                stringResource(Res.string.sign_in_to_continue),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            ElevatedCard {
                Column(
                    Modifier.padding(Dimens.PaddingMedium),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
                ) {
                    Text(
                        stringResource(Res.string.introduce_credentials_description),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    OutlinedTextField(
                        value = state.username.value,
                        onValueChange = viewModel::onUsernameChanged,
                        label = { Text(stringResource(Res.string.username))},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                        isError = state.username.errorStringId != null,
                        supportingText = {
                            state.username.errorStringId?.let { Text(stringResource(it)) }
                        }
                    )
                    OutlinedTextField(
                        value = state.password.value,
                        onValueChange = viewModel::onPasswordChanged,
                        label = { Text(stringResource(Res.string.password))},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            keyboardController?.hide()
                            viewModel.login()
                        }),
                        visualTransformation = PasswordVisualTransformation(),
                        isError = state.password.errorStringId != null,
                        supportingText = {
                            state.password.errorStringId?.let { Text(stringResource(it)) }
                        }
                    )

                    AnimatedVisibility(visible = state.loginRequest is LoginRequestState.Error) {
                        (state.loginRequest as? LoginRequestState.Error)?.error?.let {
                            Text(
                                it.description ?: stringResource(it.descriptionId),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }

                    Button(
                        onClick = {
                            keyboardController?.hide()
                            viewModel.login()
                        },
                        enabled = state.loginRequest != LoginRequestState.Loading
                    ) {
                        if (state.loginRequest == LoginRequestState.Loading) {
                            CircularProgressIndicator(Modifier.size(Dimens.SizeMedium))
                            Spacer(Modifier.width(Dimens.SpacingMedium))
                        }
                        Text(stringResource(Res.string.sign_in))
                    }
                }
            }

            Spacer(Modifier.weight(1f))
        }
    }
}