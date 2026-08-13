package com.iolandarosa.retailhub.features.auth.presentation.login

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
    val usernameState = rememberTextFieldState()
    val passwordState = rememberTextFieldState()

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
                        state = usernameState,
                        label = { Text(stringResource(Res.string.username))}
                    )
                    OutlinedTextField(
                        state = passwordState,
                        label = { Text(stringResource(Res.string.password))}
                    )
                    Button(
                        onClick = {
                            viewModel.login("emilys", "emilyspass")
                        },
                    ) {
                        Text(stringResource(Res.string.sign_in))
                    }
                }
            }

            Spacer(Modifier.weight(1f))
        }
    }
}