/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.composeapp

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.iolandarosa.retailhub.composeapp.navigation.AddressRoute
import com.iolandarosa.retailhub.composeapp.navigation.LoginRoute
import com.iolandarosa.retailhub.composeapp.navigation.ProfileRoute
import com.iolandarosa.retailhub.composeapp.navigation.RetailHubTopAppBar
import com.iolandarosa.retailhub.composeapp.navigation.appBarConfig
import com.iolandarosa.retailhub.composeapp.navigation.rememberNavigator
import com.iolandarosa.retailhub.core.ui.snackbar.AppSnackBarVisuals
import com.iolandarosa.retailhub.core.ui.snackbar.SnackBarData
import com.iolandarosa.retailhub.core.ui.snackbar.SnackBarType
import com.iolandarosa.retailhub.core.ui.snackbar.showSnackBar
import com.iolandarosa.retailhub.core.ui.theme.RetailHubTheme
import com.iolandarosa.retailhub.features.auth.presentation.login.LoginScreen
import com.iolandarosa.retailhub.features.profile.presentation.address.AddressScreen
import com.iolandarosa.retailhub.features.profile.presentation.profile.ProfileScreen
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import retailhub.composeapp.generated.resources.Res
import retailhub.composeapp.generated.resources.error_unknown

@Composable
fun App() {
    val navigator = rememberNavigator(initialRoute = ProfileRoute)
    val appBarConfig = navigator.backStack.last().appBarConfig()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val showSnackBar: (SnackBarData) -> Unit = { data ->
        scope.launch {
            snackBarHostState.showSnackBar(data)
        }
    }

    RetailHubTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                AnimatedContent(
                    targetState = appBarConfig,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "TopAppBarAnimation",
                ) { config ->
                    config?.let {
                        RetailHubTopAppBar(it, onBack = navigator::pop)
                    }
                }
            },
            snackbarHost = {
                SnackbarHost(snackBarHostState) { data ->
                    val visuals = data.visuals as? AppSnackBarVisuals
                    val message = visuals?.message

                    Snackbar(
                        containerColor =
                            when (visuals?.type) {
                                SnackBarType.ERROR -> MaterialTheme.colorScheme.errorContainer
                                SnackBarType.SUCCESS -> MaterialTheme.colorScheme.primaryContainer
                                SnackBarType.INFO -> MaterialTheme.colorScheme.inverseSurface
                                null -> MaterialTheme.colorScheme.inverseSurface
                            },
                    ) {
                        if (!message.isNullOrBlank()) {
                            Text(message)
                        } else {
                            Text(stringResource(visuals?.messageId ?: Res.string.error_unknown))
                        }
                    }
                }
            },
        ) { innerPadding ->
            NavDisplay(
                backStack = navigator.backStack,
                onBack = navigator::pop,
                entryProvider =
                    entryProvider {
                        entry<LoginRoute> {
                            LoginScreen(
                                paddingValues = innerPadding,
                                navigateToProfile = { navigator.navigateInitialRoute(ProfileRoute) },
                                viewModel = koinViewModel(),
                            )
                        }

                        entry<ProfileRoute> {
                            ProfileScreen(
                                paddingValues = innerPadding,
                                navigateToLogin = { navigator.navigateInitialRoute(LoginRoute) },
                                navigateToAddressDetails = { navigator.navigate(AddressRoute(it)) },
                                showSnackBar = showSnackBar,
                                viewModel = koinViewModel(),
                            )
                        }

                        entry<AddressRoute> { key ->
                            AddressScreen(
                                paddingValues = innerPadding,
                                showSnackBar = showSnackBar,
                                viewModel = koinViewModel(parameters = { parametersOf(key.address) }),
                            )
                        }
                    },
            )
        }
    }
}
