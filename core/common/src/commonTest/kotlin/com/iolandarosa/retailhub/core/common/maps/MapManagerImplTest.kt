/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.common.maps

import com.iolandarosa.retailhub.core.common.BuildKonfig
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import kotlin.test.Test
import kotlin.test.assertEquals

class MapManagerImplTest {
    private val mapNavigator: MapNavigator = mock()
    private val mapManager: MapManager = MapManagerImpl(mapNavigator)

    @Test
    fun values_getStaticMapUrl_hasExpectedValue() {
        val lat = 1.0
        val lng = 2.0
        assertEquals(
            "https://api.mapbox.com/styles/v1/mapbox/satellite-streets-v12/static/" +
                "pin-s+A0CFD2($lng,$lat)/$lng,$lat,16/300x200.png?" +
                "attribution=false&logo=false&access_token=${BuildKonfig.MAPBOX_TOKEN}",
            mapManager.getStaticMapUrl(lat, lng),
        )
    }

    @Test
    fun values_openMap_expectedMethodCalled() {
        every { mapNavigator.openMap(any(), any(), any()) } returns Unit

        val lat = 1.0
        val lng = 2.0
        val street = "street"

        mapManager.openMap(lat, lng, street)

        verify { mapNavigator.openMap(lat, lng, street) }
    }
}
