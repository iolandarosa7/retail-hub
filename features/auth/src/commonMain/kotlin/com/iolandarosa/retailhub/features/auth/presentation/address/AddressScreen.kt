/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.address

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.iolandarosa.retailhub.core.ui.theme.Dimens
import com.iolandarosa.retailhub.features.auth.domain.model.Address
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import retailhub.features.auth.generated.resources.Res
import retailhub.features.auth.generated.resources.coordinates
import retailhub.features.auth.generated.resources.copied_to_clipboard
import retailhub.features.auth.generated.resources.copy_to_clipboard
import retailhub.features.auth.generated.resources.country
import retailhub.features.auth.generated.resources.ic_copy
import retailhub.features.auth.generated.resources.ic_open_in_new
import retailhub.features.auth.generated.resources.location
import retailhub.features.auth.generated.resources.map_image_of_user_location
import retailhub.features.auth.generated.resources.open_in_maps

@Composable
fun AddressScreen(
    paddingValues: PaddingValues,
    onShowMessage: (String) -> Unit,
    viewModel: AddressViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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
        AddressSummary(state.address)

        MapImage(state.staticMapUrl)

        LocationDetails(state.address)

        CountrySection(state.address.country)

        CoordinatesSection(
            coordinatesStr = state.coordinatesStr,
            onCopyClick = { viewModel.onIntent(AddressContract.Intent.OnClipboardCopy(it)) },
        )

        OpenInMapsButton { viewModel.onIntent(AddressContract.Intent.OpenMap) }

        Spacer(Modifier.height(Dimens.SpacingLarge))
    }
}

@Composable
private fun AddressSummary(address: Address) {
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

@Composable
private fun MapImage(
    mapUrl: String,
    aspectRatio: Float = 1.8f,
) {
    AsyncImage(
        model = mapUrl,
        contentDescription = stringResource(Res.string.map_image_of_user_location),
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
                .clip(RoundedCornerShape(Dimens.CornerRadiusSmall)),
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun LocationDetails(address: Address) {
    Column {
        SectionTitle(stringResource(Res.string.location))

        DetailRow(address.city)
        DetailRow("${address.state} · ${address.stateCode}")
    }
}

@Composable
private fun CountrySection(country: String) {
    Column {
        SectionTitle(stringResource(Res.string.country))
        DetailRow(country)
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
private fun OpenInMapsButton(openMap: () -> Unit) {
    Button(
        onClick = openMap,
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
    )
}
