/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.common.maps

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri

class AndroidMapNavigator(
    private val context: Context,
) : MapNavigator {
    @SuppressLint("UseKtx")
    override fun openMap(
        lat: Double,
        lng: Double,
        label: String,
    ) {
        try {
            val uri = Uri.parse("geo:0,0?q=$lat,$lng(${Uri.encode(label)})")
            val intent =
                Intent(Intent.ACTION_VIEW, uri).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            context.startActivity(intent)
        } catch (_: Exception) {
        }
    }
}
