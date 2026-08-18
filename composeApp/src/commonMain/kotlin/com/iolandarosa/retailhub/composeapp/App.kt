package com.iolandarosa.retailhub.composeapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iolandarosa.retailhub.core.ui.theme.RetailHubTheme
import com.iolandarosa.retailhub.features.auth.login.LoginScreen

@Composable
fun App() {
    RetailHubTheme {
        Scaffold(Modifier.fillMaxSize()) { innerPadding ->
            LoginScreen(paddingValues = innerPadding)
        }
    }
}