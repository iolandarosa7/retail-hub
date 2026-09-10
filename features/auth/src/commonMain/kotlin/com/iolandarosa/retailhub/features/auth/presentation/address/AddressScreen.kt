/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iolandarosa.retailhub.core.ui.theme.Dimens
import com.iolandarosa.retailhub.features.auth.domain.model.Address
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import retailhub.features.auth.generated.resources.Res
import retailhub.features.auth.generated.resources.coordinates
import retailhub.features.auth.generated.resources.copied_to_clipboard
import retailhub.features.auth.generated.resources.copy_to_clipboard
import retailhub.features.auth.generated.resources.country
import retailhub.features.auth.generated.resources.ic_address
import retailhub.features.auth.generated.resources.ic_copy
import retailhub.features.auth.generated.resources.ic_open_in_new
import retailhub.features.auth.generated.resources.location
import retailhub.features.auth.generated.resources.open_in_maps

@Composable
fun AddressScreen(
    paddingValues: PaddingValues,
    address: Address,
    onShowMessage: (String) -> Unit,
    viewModel: AddressViewModel = koinViewModel(),
) {
    val copiedMessage = stringResource(Res.string.copied_to_clipboard)

    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collect { effect ->
            when (effect) {
                AddressContract.Effect.ShowCopySuccess -> {
                    onShowMessage(copiedMessage)
                }
            }
        }
    }

    Column(
        modifier =
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(Dimens.PaddingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge),
    ) {
        AddressSummary(address)

        MapPlaceholder()

        LocationDetails(address)

        CountrySection(address)

        CoordinatesSection(
            "${address.coordinates.lat}, ${address.coordinates.lng}",
            onCopyClick = { viewModel.onIntent(AddressContract.Intent.OnClipboardCopy(it)) },
        )

        OpenInMapsButton()

        Spacer(Modifier.height(Dimens.SpacingLarge))
    }
}

@Composable
private fun AddressSummary(address: Address) {
    Row(horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingExtraSmall)) {
        Icon(
            painter = painterResource(Res.drawable.ic_address),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(Dimens.SizeIconLarge),
        )

        Column {
            Text(
                text = address.street,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                text = "${address.city}, ${address.stateCode} ${address.postalCode}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun MapPlaceholder() {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "MAP",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Icon(
                painter = painterResource(Res.drawable.ic_address),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun LocationDetails(address: Address) {
    Column {
        SectionTitle(stringResource(Res.string.location))

        DetailRow(address.city)
        DetailRow("${address.state} · ${address.stateCode}")
        DetailRow(address.postalCode)
    }
}

@Composable
private fun CountrySection(address: Address) {
    Column {
        SectionTitle(stringResource(Res.string.country))
        DetailRow(address.country)
    }
}

@Composable
private fun CoordinatesSection(
    coordinatesStr: String,
    onCopyClick: (value: String) -> Unit,
) {
    Column {
        SectionTitle(stringResource(Res.string.coordinates))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = coordinatesStr,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )

            IconButton(onClick = { onCopyClick(coordinatesStr) }) {
                Icon(
                    painter = painterResource(Res.drawable.ic_copy),
                    contentDescription = stringResource(Res.string.copy_to_clipboard),
                    tint = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
    }
}

@Composable
private fun OpenInMapsButton() {
    Button(
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            ),
    ) {
        Text(text = stringResource(Res.string.open_in_maps))
        Spacer(Modifier.size(Dimens.SpacingSmall))
        Icon(
            painter = painterResource(Res.drawable.ic_open_in_new),
            contentDescription = null,
            modifier = Modifier.size(Dimens.SizeIconButton),
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = Dimens.PaddingSmall),
    )
}

@Composable
private fun DetailRow(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(vertical = 2.dp),
    )
}
