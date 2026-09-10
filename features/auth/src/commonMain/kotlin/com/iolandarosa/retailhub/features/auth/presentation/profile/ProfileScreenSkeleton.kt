/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.iolandarosa.retailhub.core.ui.progress.Skeleton
import com.iolandarosa.retailhub.core.ui.theme.Dimens

@Composable
internal fun ProfileScreenSkeleton() {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(Dimens.PaddingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge),
    ) {
        ProfileHeader()

        ContactCard()

        PersonalInfoCard()

        PhysicalInfoCard()

        AddressCard()

        Skeleton(
            modifier = Modifier.fillMaxWidth().height(ButtonDefaults.MinHeight),
            shape = RoundedCornerShape(Dimens.CIRCLE_RADIUS),
        )

        Spacer(Modifier.height(Dimens.SpacingLarge))
    }
}
