package com.iolandarosa.retailhub.features.auth.presentation.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iolandarosa.retailhub.features.auth.presentation.login.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column {
        Button(
            onClick = {
                viewModel.login("emilys", "emilyspass")
            }
        ) {
            Text("Login")
        }
    }
}