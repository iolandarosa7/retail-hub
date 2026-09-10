/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.composeapp.navigation

import retailhub.composeapp.generated.resources.Res
import retailhub.composeapp.generated.resources.address_details
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppBarConfigTest {
    @Test
    fun appBarConfigInstance_hasExpectedValues() {
        val appBarConfig =
            AppBarConfig(
                titleRes = Res.string.address_details,
                showBack = true,
            )

        assertEquals(Res.string.address_details, appBarConfig.titleRes)
        assertTrue(appBarConfig.showBack)
    }
}
