/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.common.maps

import platform.Foundation.NSCharacterSet
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.URLQueryAllowedCharacterSet
import platform.Foundation.stringByAddingPercentEncodingWithAllowedCharacters
import platform.UIKit.UIApplication

class IosMapNavigator : MapNavigator {
    override fun openMap(
        lat: Double,
        lng: Double,
        label: String,
    ) {
        // Double-cast (as Any as NSString) is the standard way to bypass the
        // strict type check while leveraging the runtime bridging.
        val encodedLabel =
            (label as Any as NSString).stringByAddingPercentEncodingWithAllowedCharacters(
                NSCharacterSet.URLQueryAllowedCharacterSet,
            ) ?: label.replace(" ", "%20")

        val urlString = "http://maps.apple.com/?q=$encodedLabel&ll=$lat,$lng"
        val url = NSURL.URLWithString(urlString)

        if (url != null && UIApplication.sharedApplication.canOpenURL(url)) {
            UIApplication.sharedApplication.openURL(
                url = url,
                options = emptyMap<Any?, Any?>(),
                completionHandler = null,
            )
        }
    }
}
