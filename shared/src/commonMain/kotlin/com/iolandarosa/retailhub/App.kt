package com.iolandarosa.retailhub

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.iolandarosa.retailhub.core.ui.theme.RetailHubTheme
import com.iolandarosa.retailhub.features.auth.presentation.login.LoginScreen

@Composable
@Preview
fun App() {
    RetailHubTheme {
        Scaffold(Modifier.fillMaxSize()) { innerPadding ->
            LoginScreen(paddingValues = innerPadding)
        }
    }
}