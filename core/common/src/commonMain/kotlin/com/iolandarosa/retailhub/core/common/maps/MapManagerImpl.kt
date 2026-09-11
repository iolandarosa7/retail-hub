/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.common.maps

import com.iolandarosa.retailhub.core.common.BuildKonfig

internal class MapManagerImpl(
    private val navigator: MapNavigator,
) : MapManager {
    override fun getStaticMapUrl(
        lat: Double,
        lng: Double,
    ): String =
        "https://api.mapbox.com/styles/v1/mapbox/satellite-streets-v12/static/" +
            "pin-s+A0CFD2($lng,$lat)/$lng,$lat,16/300x200.png?" +
            "attribution=false&logo=false&access_token=${BuildKonfig.MAPBOX_TOKEN}"

    override fun openMap(
        lat: Double,
        lng: Double,
        label: String,
    ) {
        navigator.openMap(lat, lng, label)
    }
}
