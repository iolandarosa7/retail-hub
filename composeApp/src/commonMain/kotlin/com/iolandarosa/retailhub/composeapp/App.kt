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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.iolandarosa.retailhub.core.ui.theme.RetailHubTheme
import com.iolandarosa.retailhub.features.auth.presentation.login.LoginScreen
import com.iolandarosa.retailhub.features.profile.presentation.address.AddressScreen
import com.iolandarosa.retailhub.features.profile.presentation.profile.ProfileScreen
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun App() {
    val navigator = rememberNavigator(initialRoute = ProfileRoute)
    val appBarConfig = navigator.backStack.last().appBarConfig()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val showSnackBar: (String) -> Unit = { message ->
        scope.launch { snackBarHostState.showSnackbar(message) }
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
            snackbarHost = { SnackbarHost(snackBarHostState) },
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
                                viewModel = koinViewModel(),
                            )
                        }

                        entry<AddressRoute> { key ->
                            AddressScreen(
                                paddingValues = innerPadding,
                                onShowMessage = showSnackBar,
                                viewModel = koinViewModel(parameters = { parametersOf(key.address) }),
                            )
                        }
                    },
            )
        }
    }
}
