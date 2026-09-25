/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.presentation.profile.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import com.iolandarosa.retailhub.core.ui.error.UiError
import com.iolandarosa.retailhub.core.ui.form.FormState
import com.iolandarosa.retailhub.core.ui.form.components.FormFieldRenderer
import com.iolandarosa.retailhub.core.ui.theme.Dimens
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import retailhub.features.profile.generated.resources.Res
import retailhub.features.profile.generated.resources.ic_error
import retailhub.features.profile.generated.resources.sign_in
import retailhub.features.profile.generated.resources.sign_in_access_profile
import retailhub.features.profile.generated.resources.you_are_unauthenticated

@Composable
fun UnauthenticatedContent(
    formState: FormState,
    isEnabled: Boolean,
    error: UiError?,
    onSignInClick: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(Dimens.PaddingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge),
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_error),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(Dimens.SizeCircleImage)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
        )

        Text(
            stringResource(Res.string.you_are_unauthenticated),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )

        Text(
            stringResource(Res.string.sign_in_access_profile),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall),
        ) {
            formState.fields.forEach { field ->
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
        }

        Button(
            onClick = {
                keyboardController?.hide()
                onSignInClick()
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
