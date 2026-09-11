/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.common.maps

interface MapNavigator {
    fun openMap(
        lat: Double,
        lng: Double,
        label: String,
    )
}
