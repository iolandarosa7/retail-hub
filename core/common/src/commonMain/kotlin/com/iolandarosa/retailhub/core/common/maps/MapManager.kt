/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.common.maps

interface MapManager {
    fun getStaticMapUrl(
        lat: Double,
        lng: Double,
    ): String

    fun openMap(
        lat: Double,
        lng: Double,
        label: String,
    )
}
