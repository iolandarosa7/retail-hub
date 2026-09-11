/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.address

import com.iolandarosa.retailhub.features.auth.BuildKonfig
import com.iolandarosa.retailhub.features.auth.domain.model.Address

interface AddressContract {
    data class State(
        val address: Address,
    ) {
        val mapUrl: String get() =
            "https://api.mapbox.com/styles/v1/mapbox/satellite-streets-v12/static/" +
                "pin-s+A0CFD2(${address.coordinates.lng},${address.coordinates.lat})/" +
                "${address.coordinates.lng},${address.coordinates.lat},16/300x200.png?" +
                "attribution=false&logo=false&access_token=${BuildKonfig.MAPBOX_TOKEN}"
        val coordinatesStr: String get() = "${address.coordinates.lat}, ${address.coordinates.lng}"
    }

    sealed interface Intent {
        data class OnClipboardCopy(
            val value: String,
        ) : Intent
    }

    sealed interface Effect {
        data object ShowCopySuccess : Effect
    }
}
