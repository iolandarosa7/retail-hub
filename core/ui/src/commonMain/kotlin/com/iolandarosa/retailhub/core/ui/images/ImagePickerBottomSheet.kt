/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.images

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.iolandarosa.retailhub.core.ui.permissions.AppPermission
import com.iolandarosa.retailhub.core.ui.theme.Dimens
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import retailhub.core.ui.generated.resources.Res
import retailhub.core.ui.generated.resources.camera
import retailhub.core.ui.generated.resources.gallery
import retailhub.core.ui.generated.resources.ic_camera_add
import retailhub.core.ui.generated.resources.ic_gallery_add
import retailhub.core.ui.generated.resources.image_bottom_sheet_description

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerBottomSheet(
    onDismiss: () -> Unit,
    onClick: (AppPermission) -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(Dimens.PaddingMedium),
            verticalArrangement = Arrangement.spacedBy(Dimens.SizeMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                stringResource(Res.string.image_bottom_sheet_description),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium),
            ) {
                Button(
                    onClick = { onClick(AppPermission.Camera) },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(painter = painterResource(Res.drawable.ic_camera_add), contentDescription = null)
                    Spacer(modifier = Modifier.width(Dimens.SizeSmall))
                    Text(stringResource(Res.string.camera))
                }

                Button(
                    onClick = { onClick(AppPermission.Gallery) },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(painter = painterResource(Res.drawable.ic_gallery_add), contentDescription = null)
                    Spacer(modifier = Modifier.width(Dimens.SizeSmall))
                    Text(stringResource(Res.string.gallery))
                }
            }
        }
    }
}
