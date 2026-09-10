/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.iolandarosa.retailhub.core.ui.progress.Skeleton
import com.iolandarosa.retailhub.core.ui.theme.Dimens
import com.iolandarosa.retailhub.features.auth.domain.model.User
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import retailhub.features.auth.generated.resources.Res
import retailhub.features.auth.generated.resources.contact
import retailhub.features.auth.generated.resources.ic_email
import retailhub.features.auth.generated.resources.ic_phone

@Composable
internal fun ContactCard(user: User? = null) {
    InfoCard(title = stringResource(Res.string.contact)) {
        if (user != null) {
            ContactCardContent(user)
        } else {
            ContactCardSkeleton()
        }
    }
}

@Composable
internal fun ContactCardContent(user: User) {
    ContactItem(emoji = Res.drawable.ic_email, text = user.email)
    HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.PaddingSmall))
    ContactItem(emoji = Res.drawable.ic_phone, text = user.phone)
}

@Composable
internal fun ContactCardSkeleton() {
    ContactItem(emoji = Res.drawable.ic_email)
    HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.PaddingSmall))
    ContactItem(emoji = Res.drawable.ic_phone, widthPercent = 0.5f)
}

@Composable
internal fun ContactItem(
    emoji: DrawableResource,
    text: String? = null,
    widthPercent: Float = 1f,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium),
    ) {
        Icon(painter = painterResource(emoji), contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
        if (text != null) {
            Text(text = text, style = MaterialTheme.typography.bodyMedium)
        } else {
            Skeleton(modifier = Modifier.fillMaxWidth(widthPercent).height(Dimens.SizeMedium))
        }
    }
}
