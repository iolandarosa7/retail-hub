/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.common.clipboard

interface AppClipboardManager {
    val providesFeedback: Boolean

    fun copy(text: String)
}
