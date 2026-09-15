/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.AsyncImage
import com.iolandarosa.retailhub.core.ui.progress.Skeleton
import com.iolandarosa.retailhub.core.ui.theme.Dimens
import com.iolandarosa.retailhub.features.auth.domain.model.User
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import retailhub.features.auth.generated.resources.Res
import retailhub.features.auth.generated.resources.ic_camera
import retailhub.features.auth.generated.resources.update_profile_picture

@Composable
internal fun ProfileHeader(
    user: User? = null,
    imageBytes: ByteArray? = null,
    isEnabled: Boolean = false,
    onPhotoClick: (() -> Unit)? = null,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall),
    ) {
        if (user != null) {
            ProfileHeaderContent(user, imageBytes, isEnabled, onPhotoClick)
        } else {
            ProfileSkeleton()
        }
    }
}

@Composable
internal fun ProfileHeaderContent(
    user: User,
    imageBytes: ByteArray?,
    isEnabled: Boolean,
    onPhotoClick: (() -> Unit)?,
) {
    Box(
        modifier = Modifier.size(Dimens.SizeCircleImage + (Dimens.SizeIconButton / 2)),
    ) {
        AsyncImage(
            model = imageBytes ?: user.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(Dimens.SizeCircleImage)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
        )

        onPhotoClick?.let {
            FilledIconButton(
                onClick = it,
                modifier = Modifier.align(Alignment.BottomEnd),
                enabled = isEnabled,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_camera),
                    contentDescription = stringResource(Res.string.update_profile_picture),
                )
            }
        }
    }

    Text(
        text = user.name,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
    )

    Text(
        text = user.role,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
internal fun ProfileSkeleton() {
    val nameFraction = 0.7f
    val roleFraction = 0.3f

    Skeleton(
        modifier = Modifier.size(Dimens.SizeCircleImage),
        shape = CircleShape,
        contentDescription = "Loading User Profile",
    )

    Skeleton(
        modifier = Modifier.fillMaxWidth(nameFraction).height(Dimens.SizeExtraLarge),
        shape = RoundedCornerShape(Dimens.CIRCLE_RADIUS),
    )
    Skeleton(modifier = Modifier.fillMaxWidth(roleFraction).height(Dimens.SizeMedium))
}
