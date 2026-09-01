/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.login

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iolandarosa.retailhub.core.ui.form.components.FormFieldRenderer
import com.iolandarosa.retailhub.core.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import retailhub.features.auth.generated.resources.Res
import retailhub.features.auth.generated.resources.introduce_credentials_description
import retailhub.features.auth.generated.resources.sign_in
import retailhub.features.auth.generated.resources.sign_in_to_continue
import retailhub.features.auth.generated.resources.welcome_back

@Composable
fun LoginScreen(
    paddingValues: PaddingValues,
    navigateToProfile: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val isEnabled by remember { derivedStateOf { state.isInteractionEnabled } }
    val error by remember { derivedStateOf { state.error } }

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collect { effect ->
            when (effect) {
                LoginEffect.NavigateToProfile -> navigateToProfile()
            }
        }
    }

    Box(
        Modifier.fillMaxSize(),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .background(
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.75f),
                                    MaterialTheme.colorScheme.background,
                                ),
                        ),
                    ),
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
                color = MaterialTheme.colorScheme.onBackground,
            )

            Text(
                stringResource(Res.string.sign_in_to_continue),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            ElevatedCard(Modifier.padding(Dimens.PaddingMedium)) {
                Column(
                    Modifier.padding(Dimens.PaddingMedium),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium),
                ) {
                    Text(
                        stringResource(Res.string.introduce_credentials_description),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    state.formState.fields.forEach { field ->
                        key(field.name) {
                            FormFieldRenderer(
                                field = field,
                                modifier = Modifier.fillMaxWidth(),
                                enabled = isEnabled,
                            )
                        }
                    }

                    AnimatedVisibility(visible = error != null) {
                        error?.let {
                            Text(
                                it.description ?: stringResource(it.descriptionId),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    }

                    Button(
                        onClick = {
                            keyboardController?.hide()
                            viewModel.onIntent(LoginIntent.OnLoginClicked)
                        },
                        enabled = isEnabled,
                    ) {
                        if (!isEnabled) {
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
