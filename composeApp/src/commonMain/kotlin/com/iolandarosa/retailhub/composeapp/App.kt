package com.iolandarosa.retailhub.composeapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.iolandarosa.retailhub.composeapp.navigation.ProfileRoute
import com.iolandarosa.retailhub.composeapp.navigation.LoginRoute
import com.iolandarosa.retailhub.composeapp.navigation.rememberNavigator
import com.iolandarosa.retailhub.core.ui.theme.RetailHubTheme
import com.iolandarosa.retailhub.features.auth.login.LoginScreen

@Composable
fun App() {
    val navigator = rememberNavigator(initialRoute = LoginRoute)

    RetailHubTheme {
        Scaffold(Modifier.fillMaxSize()) { innerPadding ->
            NavDisplay(
                backStack = navigator.backStack,
                onBack = navigator::pop,
                entryProvider = entryProvider {
                    entry<LoginRoute> {
                        LoginScreen(
                            paddingValues = innerPadding,
                            navigateToProfile = { navigator.navigate(ProfileRoute) }
                        )
                    }

                    entry<ProfileRoute> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Button(onClick = navigator::pop) {
                                Text("Auth Profile")
                            }
                        }
                    }
                }
            )
        }
    }
}
