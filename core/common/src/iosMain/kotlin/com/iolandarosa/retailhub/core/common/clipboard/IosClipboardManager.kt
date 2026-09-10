/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.common.clipboard

import platform.UIKit.UIPasteboard

class IosClipboardManager : AppClipboardManager {
    override val providesFeedback: Boolean
        get() = false

    override fun copy(text: String) {
        UIPasteboard.generalPasteboard.string = text
    }
}
