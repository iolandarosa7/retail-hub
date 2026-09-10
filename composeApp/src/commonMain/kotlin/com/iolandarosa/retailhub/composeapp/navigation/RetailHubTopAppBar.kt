/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.composeapp.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import retailhub.composeapp.generated.resources.Res
import retailhub.composeapp.generated.resources.back
import retailhub.composeapp.generated.resources.ic_back

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RetailHubTopAppBar(
    appBarConfig: AppBarConfig,
    onBack: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(appBarConfig.titleRes),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
        },
        navigationIcon = {
            if (appBarConfig.showBack) {
                IconButton(onClick = onBack) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_back),
                        contentDescription = stringResource(Res.string.back),
                    )
                }
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
            ),
    )
}
