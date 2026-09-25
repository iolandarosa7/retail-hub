/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.iolandarosa.retailhub.core.ui.theme.Dimens
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import retailhub.features.home.generated.resources.Res
import retailhub.features.home.generated.resources.app_name
import retailhub.features.home.generated.resources.go_to_profile
import retailhub.features.home.generated.resources.ic_account

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    navigateToProfile: () -> Unit,
    viewModel: HomeViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(HomeContract.Intent.LoadAuthUserImage)
    }

    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collect { effect ->
            when (effect) {
                HomeContract.Effect.NavigateUserProfile -> navigateToProfile()
            }
        }
    }

    HomeContent(
        paddingValues,
        state,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun HomeContent(
    paddingValues: PaddingValues,
    state: HomeContract.State,
    onIntent: (HomeContract.Intent) -> Unit,
) {
    Column(
        Modifier
            .padding(paddingValues)
            .fillMaxSize(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(Dimens.PaddingMedium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                stringResource(Res.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )

            IconButton(
                onClick = { onIntent(HomeContract.Intent.ClickProfile) },
            ) {
                AsyncImage(
                    model = state.authUserImage.bytes ?: state.authUserImage.url,
                    contentDescription = stringResource(Res.string.go_to_profile),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(Res.drawable.ic_account),
                    error = painterResource(Res.drawable.ic_account),
                    modifier =
                        Modifier
                            .clip(CircleShape)
                            .size(Dimens.SizeLarge)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                )
            }
        }
    }
}
