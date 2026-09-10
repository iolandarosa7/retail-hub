/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iolandarosa.retailhub.core.ui.error.ErrorComponent
import com.iolandarosa.retailhub.core.ui.theme.Dimens
import com.iolandarosa.retailhub.features.auth.domain.model.Address
import com.iolandarosa.retailhub.features.auth.domain.model.User
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import retailhub.features.auth.generated.resources.Res
import retailhub.features.auth.generated.resources.ic_logout
import retailhub.features.auth.generated.resources.logout
import retailhub.features.auth.generated.resources.retry

@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
    navigateToLogin: () -> Unit,
    navigateToAddressDetails: (Address) -> Unit,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isEnabled by remember { derivedStateOf { state.isInteractionEnabled } }
    val isRefreshing by remember { derivedStateOf { state.isRefreshing } }

    LaunchedEffect(Unit) {
        viewModel.onIntent(ProfileContract.Intent.LoadProfile)
    }

    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collect { effect ->
            when (effect) {
                ProfileContract.Effect.NavigateToLogin -> navigateToLogin()
                is ProfileContract.Effect.NavigateToAddressDetails -> navigateToAddressDetails(effect.address)
            }
        }
    }

    Box(
        modifier =
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
    ) {
        when (val userRequest = state.userRequest) {
            is ProfileContract.UserRequestState.Error -> {
                ErrorComponent(
                    modifier = Modifier.fillMaxSize().padding(Dimens.PaddingMedium),
                    title = stringResource(userRequest.error.titleId),
                    description = userRequest.error.description ?: stringResource(userRequest.error.descriptionId),
                    trailingContent = {
                        if (userRequest.error.hasRetry) {
                            Button(
                                onClick = { viewModel.onIntent(ProfileContract.Intent.LoadProfile) },
                                enabled = isEnabled,
                                modifier = Modifier.padding(top = Dimens.PaddingExtraLarge).fillMaxWidth(0.5f),
                            ) {
                                Text(stringResource(Res.string.retry))
                            }
                        }
                    },
                )
            }

            ProfileContract.UserRequestState.Initial,
            ProfileContract.UserRequestState.Loading,
            -> {
                ProfileScreenSkeleton()
            }

            is ProfileContract.UserRequestState.Success -> {
                ProfileScreenContent(
                    user = userRequest.user,
                    isEnabled = isEnabled,
                    isRefreshing = isRefreshing,
                    onRefresh = { viewModel.onIntent(ProfileContract.Intent.RefreshProfile) },
                    onLogout = { viewModel.onIntent(ProfileContract.Intent.Logout) },
                    onAddressDetailsClick = { address ->
                        viewModel.onIntent(ProfileContract.Intent.ViewAddressDetails(address))
                    },
                )
            }
        }
    }
}

@Composable
internal fun ProfileScreenContent(
    user: User,
    isEnabled: Boolean,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onLogout: () -> Unit,
    onAddressDetailsClick: (Address) -> Unit,
) {
    PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = onRefresh) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(Dimens.PaddingMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge),
        ) {
            ProfileHeader(user)

            ContactCard(user)

            PersonalInfoCard(user)

            PhysicalInfoCard(user)

            AddressCard(address = user.address, onClick = { onAddressDetailsClick(user.address) })

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                enabled = isEnabled,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    ),
            ) {
                if (isEnabled) {
                    Icon(painter = painterResource(Res.drawable.ic_logout), contentDescription = null)
                } else {
                    CircularProgressIndicator(
                        Modifier.size(Dimens.SizeMedium),
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                }
                Spacer(Modifier.width(Dimens.SpacingMedium))
                Text(stringResource(Res.string.logout))
            }

            Spacer(Modifier.height(Dimens.SpacingLarge))
        }
    }
}
