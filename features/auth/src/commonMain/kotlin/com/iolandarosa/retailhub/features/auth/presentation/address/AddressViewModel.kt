/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iolandarosa.retailhub.core.common.clipboard.AppClipboardManager
import com.iolandarosa.retailhub.core.common.dispatcher.DispatcherProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddressViewModel(
    private val dispatcherProvider: DispatcherProvider,
    private val clipboardManager: AppClipboardManager,
) : ViewModel() {
    private val _effects = Channel<AddressContract.Effect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: AddressContract.Intent) {
        when (intent) {
            is AddressContract.Intent.OnClipboardCopy -> copyClipboard(intent.value)
        }
    }

    private fun copyClipboard(text: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            clipboardManager.copy(text)
            if (!clipboardManager.providesFeedback) {
                _effects.send(AddressContract.Effect.ShowCopySuccess)
            }
        }
    }
}
