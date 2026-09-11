/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.address

import com.iolandarosa.retailhub.features.auth.domain.model.Address

interface AddressContract {
    data class State(
        val address: Address,
        val staticMapUrl: String,
    ) {
        val coordinatesStr: String get() = "${address.coordinates.lat}, ${address.coordinates.lng}"
    }

    sealed interface Intent {
        data class OnClipboardCopy(
            val value: String,
        ) : Intent

        data object OpenMap : Intent
    }

    sealed interface Effect {
        data object ShowCopySuccess : Effect
    }
}
