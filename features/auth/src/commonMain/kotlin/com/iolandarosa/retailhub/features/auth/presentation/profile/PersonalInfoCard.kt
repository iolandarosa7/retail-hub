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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.iolandarosa.retailhub.core.ui.progress.Skeleton
import com.iolandarosa.retailhub.core.ui.theme.Dimens
import com.iolandarosa.retailhub.features.auth.domain.model.User
import org.jetbrains.compose.resources.stringResource
import retailhub.features.auth.generated.resources.Res
import retailhub.features.auth.generated.resources.age
import retailhub.features.auth.generated.resources.birth_date
import retailhub.features.auth.generated.resources.gender
import retailhub.features.auth.generated.resources.personal

@Composable
internal fun PersonalInfoCard(user: User? = null) {
    InfoCard(title = stringResource(Res.string.personal)) {
        if (user != null) {
            PersonalInfoCardContent(user)
        } else {
            PersonalInfoSkeleton()
        }
    }
}

@Composable
internal fun PersonalInfoCardContent(user: User) {
    InfoRow(label = stringResource(Res.string.birth_date), value = user.birthDate)
    InfoRow(label = stringResource(Res.string.age), value = user.age.toString())
    InfoRow(label = stringResource(Res.string.gender), value = user.gender)
}

@Composable
internal fun PersonalInfoSkeleton() {
    InfoRow(label = stringResource(Res.string.birth_date), widthPercent = 0.3f)
    InfoRow(label = stringResource(Res.string.age), widthPercent = 0.1f)
    InfoRow(label = stringResource(Res.string.gender), widthPercent = 0.2f)
}

@Composable
internal fun InfoRow(
    label: String,
    value: String? = null,
    widthPercent: Float = 1f,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (value != null) {
            Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
        } else {
            Skeleton(modifier = Modifier.fillMaxWidth(widthPercent).height(Dimens.SizeSmall))
        }
    }
}
