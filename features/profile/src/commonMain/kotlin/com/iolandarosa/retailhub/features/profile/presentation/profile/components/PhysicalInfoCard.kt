/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.iolandarosa.retailhub.core.ui.progress.Skeleton
import com.iolandarosa.retailhub.core.ui.theme.Dimens
import com.iolandarosa.retailhub.features.profile.domain.model.User
import org.jetbrains.compose.resources.stringResource
import retailhub.features.profile.generated.resources.Res
import retailhub.features.profile.generated.resources.blood_group
import retailhub.features.profile.generated.resources.eyes
import retailhub.features.profile.generated.resources.hair_color
import retailhub.features.profile.generated.resources.hair_type
import retailhub.features.profile.generated.resources.height
import retailhub.features.profile.generated.resources.height_value
import retailhub.features.profile.generated.resources.physical
import retailhub.features.profile.generated.resources.weight
import retailhub.features.profile.generated.resources.weight_value

@Composable
internal fun PhysicalInfoCard(user: User? = null) {
    InfoCard(title = stringResource(Res.string.physical)) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)) {
            Row(Modifier.fillMaxWidth()) {
                PhysicalGridItem(
                    modifier = Modifier.weight(1f),
                    value =
                        if (user != null) {
                            stringResource(Res.string.height_value, user.height)
                        } else {
                            null
                        },
                    label = stringResource(Res.string.height),
                )
                PhysicalGridItem(
                    modifier = Modifier.weight(1f),
                    value =
                        if (user != null) {
                            stringResource(Res.string.weight_value, user.weight)
                        } else {
                            null
                        },
                    label = stringResource(Res.string.weight),
                )
            }
            Row(Modifier.fillMaxWidth()) {
                PhysicalGridItem(
                    modifier = Modifier.weight(1f),
                    value = user?.bloodGroup,
                    label = stringResource(Res.string.blood_group),
                )
                PhysicalGridItem(
                    modifier = Modifier.weight(1f),
                    value = user?.eyeColor,
                    label = stringResource(Res.string.eyes),
                )
            }
            Row(Modifier.fillMaxWidth()) {
                PhysicalGridItem(
                    modifier = Modifier.weight(1f),
                    value = user?.hairColor,
                    label = stringResource(Res.string.hair_color),
                )
                PhysicalGridItem(
                    modifier = Modifier.weight(1f),
                    value = user?.hairType,
                    label = stringResource(Res.string.hair_type),
                )
            }
        }
    }
}

@Composable
internal fun PhysicalGridItem(
    modifier: Modifier,
    value: String?,
    label: String,
) {
    Column(modifier = modifier) {
        if (value != null) {
            Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        } else {
            Skeleton(modifier = Modifier.fillMaxWidth(0.5f).height(Dimens.SizeMedium))
        }
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
    }
}
