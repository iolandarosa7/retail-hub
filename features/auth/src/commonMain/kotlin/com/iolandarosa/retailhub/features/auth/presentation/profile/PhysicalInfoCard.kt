/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.iolandarosa.retailhub.core.ui.theme.Dimens
import com.iolandarosa.retailhub.features.auth.domain.model.User
import org.jetbrains.compose.resources.stringResource
import retailhub.features.auth.generated.resources.Res
import retailhub.features.auth.generated.resources.blood_group
import retailhub.features.auth.generated.resources.eyes
import retailhub.features.auth.generated.resources.hair_color
import retailhub.features.auth.generated.resources.hair_type
import retailhub.features.auth.generated.resources.height
import retailhub.features.auth.generated.resources.physical
import retailhub.features.auth.generated.resources.weight

@Composable
internal fun PhysicalInfoCard(user: User) {
    InfoCard(title = stringResource(Res.string.physical)) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)) {
            Row(Modifier.fillMaxWidth()) {
                PhysicalGridItem(
                    modifier = Modifier.weight(1f),
                    value = "${user.height} cm",
                    label = stringResource(Res.string.height),
                )
                PhysicalGridItem(
                    modifier = Modifier.weight(1f),
                    value = "${user.weight} kg",
                    label = stringResource(Res.string.weight),
                )
            }
            Row(Modifier.fillMaxWidth()) {
                PhysicalGridItem(
                    modifier = Modifier.weight(1f),
                    value = user.bloodGroup,
                    label = stringResource(Res.string.blood_group),
                )
                PhysicalGridItem(
                    modifier = Modifier.weight(1f),
                    value = user.eyeColor,
                    label = stringResource(Res.string.eyes),
                )
            }
            Row(Modifier.fillMaxWidth()) {
                PhysicalGridItem(
                    modifier = Modifier.weight(1f),
                    value = user.hairColor,
                    label = stringResource(Res.string.hair_color),
                )
                PhysicalGridItem(
                    modifier = Modifier.weight(1f),
                    value = user.hairType,
                    label = stringResource(Res.string.hair_type),
                )
            }
        }
    }
}

@Composable
internal fun PhysicalGridItem(
    modifier: Modifier,
    value: String,
    label: String,
) {
    Column(modifier = modifier) {
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
    }
}
