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
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.iolandarosa.retailhub.core.ui.progress.Skeleton
import com.iolandarosa.retailhub.core.ui.theme.Dimens
import com.iolandarosa.retailhub.features.auth.domain.model.User
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import retailhub.features.auth.generated.resources.Res
import retailhub.features.auth.generated.resources.address
import retailhub.features.auth.generated.resources.address_formatted
import retailhub.features.auth.generated.resources.ic_address
import retailhub.features.auth.generated.resources.ic_arrow_forward

@Composable
internal fun AddressCard(user: User? = null) {
    InfoCard(title = stringResource(Res.string.address)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_address),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
            )
            if (user != null) {
                Text(
                    text = stringResource(Res.string.address_formatted, user.address, user.city, user.country),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                )
            } else {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Dimens.SpacingExtraSmall)) {
                    Skeleton(modifier = Modifier.fillMaxWidth().height(Dimens.SizeSmall))
                    Skeleton(modifier = Modifier.fillMaxWidth(0.5f).height(Dimens.SizeSmall))
                }
            }

            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_forward),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
