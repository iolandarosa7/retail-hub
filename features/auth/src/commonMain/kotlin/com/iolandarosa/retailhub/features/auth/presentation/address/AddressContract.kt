/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.address

interface AddressContract {
    sealed interface Intent {
        data class OnClipboardCopy(
            val value: String,
        ) : Intent
    }

    sealed interface Effect {
        data object ShowCopySuccess : Effect
    }
}
