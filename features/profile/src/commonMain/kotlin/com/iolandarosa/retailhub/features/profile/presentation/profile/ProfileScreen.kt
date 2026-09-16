/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.presentation.profile

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.iolandarosa.retailhub.core.ui.error.UiError
import com.iolandarosa.retailhub.core.ui.images.ImagePickerBottomSheet
import com.iolandarosa.retailhub.core.ui.images.InitializePermissionsAndPicker
import com.iolandarosa.retailhub.core.ui.permissions.PermissionDialog
import com.iolandarosa.retailhub.core.ui.snackbar.SnackBarData
import com.iolandarosa.retailhub.core.ui.theme.Dimens
import com.iolandarosa.retailhub.features.profile.domain.extensions.toSnackBarData
import com.iolandarosa.retailhub.features.profile.domain.model.Address
import com.iolandarosa.retailhub.features.profile.domain.model.User
import com.iolandarosa.retailhub.features.profile.presentation.profile.components.AddressCard
import com.iolandarosa.retailhub.features.profile.presentation.profile.components.ContactCard
import com.iolandarosa.retailhub.features.profile.presentation.profile.components.PersonalInfoCard
import com.iolandarosa.retailhub.features.profile.presentation.profile.components.PhysicalInfoCard
import com.iolandarosa.retailhub.features.profile.presentation.profile.components.ProfileHeader
import com.iolandarosa.retailhub.features.profile.presentation.profile.components.ProfileScreenSkeleton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import retailhub.features.profile.generated.resources.Res
import retailhub.features.profile.generated.resources.ic_logout
import retailhub.features.profile.generated.resources.logout
import retailhub.features.profile.generated.resources.retry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
    navigateToLogin: () -> Unit,
    navigateToAddressDetails: (Address) -> Unit,
    showSnackBar: (SnackBarData) -> Unit,
    viewModel: ProfileViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isEnabled by remember { derivedStateOf { state.isInteractionEnabled } }
    val isRefreshing by remember { derivedStateOf { state.isRefreshing } }
    val showPermissionsDialog by remember { derivedStateOf { state.showPermissionsDialog } }

    InitializePermissionsAndPicker(
        permissionController = viewModel.permissionController,
        imagePickerController = viewModel.imagePickerController,
    )

    LaunchedEffect(Unit) {
        viewModel.onIntent(ProfileContract.Intent.LoadProfile)
    }

    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collect { effect ->
            when (effect) {
                ProfileContract.Effect.NavigateToLogin -> {
                    navigateToLogin()
                }

                is ProfileContract.Effect.NavigateToAddressDetails -> {
                    navigateToAddressDetails(effect.address)
                }

                is ProfileContract.Effect.ShowImageStorageFailure -> {
                    showSnackBar(effect.error.toSnackBarData(effect.isDelete))
                }
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
                ProfileErrorComponent(
                    error = userRequest.error,
                    onRetry = { viewModel.onIntent(ProfileContract.Intent.LoadProfile) },
                    isEnabled = isEnabled,
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
                    imageBytes = state.imageBytes,
                    isEnabled = isEnabled,
                    isRefreshing = isRefreshing,
                    onRefresh = { viewModel.onIntent(ProfileContract.Intent.RefreshProfile) },
                    onLogout = { viewModel.onIntent(ProfileContract.Intent.Logout) },
                    onAddressDetailsClick = { address ->
                        viewModel.onIntent(ProfileContract.Intent.ViewAddressDetails(address))
                    },
                    onPhotoClick = {
                        viewModel.onIntent(
                            ProfileContract.Intent.OnImageClick(
                                userRequest.user.id,
                                state.imageBytes != null,
                            ),
                        )
                    },
                )

                if (state.showImagePicker) {
                    ImagePickerBottomSheet(
                        onDismiss = { viewModel.onIntent(ProfileContract.Intent.HideImagePickerBottomSheet) },
                        onClick = {
                            viewModel.onIntent(
                                ProfileContract.Intent.CheckImagePermissions(
                                    it,
                                    userRequest.user.id,
                                ),
                            )
                        },
                    )
                }

                if (showPermissionsDialog) {
                    state.permissionDialog?.let {
                        PermissionDialog(
                            onDismiss = { viewModel.onIntent(ProfileContract.Intent.ClosePermissionsDialog) },
                            dialog = it,
                            onConfirmClick = {
                                viewModel.onIntent(
                                    ProfileContract.Intent.ConfirmPermissionAction(
                                        it.type,
                                        userRequest.user.id,
                                    ),
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PermissionDialog(
    onDismiss: () -> Unit,
    dialog: PermissionDialog,
    onConfirmClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                stringResource(dialog.titleId),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        text = {
            Text(
                stringResource(dialog.descriptionId),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirmClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(dialog.confirmButtonLabelId))
            }
        },
    )
}

@Composable
fun ProfileErrorComponent(
    error: UiError,
    isEnabled: Boolean,
    onRetry: () -> Unit,
) {
    ErrorComponent(
        modifier = Modifier.fillMaxSize().padding(Dimens.PaddingMedium),
        title = stringResource(error.titleId),
        description = error.description ?: stringResource(error.descriptionId),
        trailingContent = {
            if (error.hasRetry) {
                Button(
                    onClick = onRetry,
                    enabled = isEnabled,
                    modifier = Modifier.padding(top = Dimens.PaddingExtraLarge).fillMaxWidth(0.5f),
                ) {
                    Text(stringResource(Res.string.retry))
                }
            }
        },
    )
}

@Composable
internal fun ProfileScreenContent(
    user: User,
    imageBytes: ByteArray?,
    isEnabled: Boolean,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onLogout: () -> Unit,
    onAddressDetailsClick: (Address) -> Unit,
    onPhotoClick: () -> Unit,
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
            ProfileHeader(
                user = user,
                imageBytes = imageBytes,
                isEnabled = isEnabled,
                onPhotoClick = onPhotoClick,
            )

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
                    Icon(
                        painter = painterResource(Res.drawable.ic_logout),
                        contentDescription = null,
                        modifier = Modifier.size(Dimens.SizeIconButton),
                    )
                } else {
                    CircularProgressIndicator(
                        Modifier.size(Dimens.SizeMedium),
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                }
                Spacer(Modifier.width(Dimens.SpacingSmall))
                Text(stringResource(Res.string.logout))
            }

            Spacer(Modifier.height(Dimens.SpacingLarge))
        }
    }
}
