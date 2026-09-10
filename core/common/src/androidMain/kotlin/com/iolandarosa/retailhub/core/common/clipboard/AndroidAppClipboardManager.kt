/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.common.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class AndroidAppClipboardManager(
    private val context: Context,
) : AppClipboardManager {
    override val providesFeedback: Boolean
        get() = true

    override fun copy(text: String) {
        val clipboard = context.getSystemService(ClipboardManager::class.java)
        clipboard.setPrimaryClip(ClipData.newPlainText("RetailHub", text))
    }
}
