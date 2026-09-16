/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.permissions

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.Foundation.NSURL
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import kotlin.coroutines.resume

class IosPermissionControllerDelegate : PermissionControllerDelegate {
    override suspend fun checkPermission(permission: AppPermission): AppPermissionStatus =
        when (permission) {
            AppPermission.Camera -> {
                val status = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
                when (status) {
                    AVAuthorizationStatusAuthorized -> AppPermissionStatus.Granted
                    AVAuthorizationStatusNotDetermined -> AppPermissionStatus.ShouldRequest(showRational = false)
                    else -> AppPermissionStatus.Denied
                }
            }

            AppPermission.Gallery -> {
                val status = PHPhotoLibrary.authorizationStatus()
                when (status) {
                    PHAuthorizationStatusAuthorized -> AppPermissionStatus.Granted
                    PHAuthorizationStatusNotDetermined -> AppPermissionStatus.ShouldRequest(showRational = false)
                    else -> AppPermissionStatus.Denied
                }
            }
        }

    override suspend fun requestPermission(permission: AppPermission): AppPermissionStatus =
        suspendCancellableCoroutine { cont ->
            when (permission) {
                AppPermission.Camera -> {
                    AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                        cont.resume(
                            if (granted) {
                                AppPermissionStatus.Granted
                            } else {
                                AppPermissionStatus.Denied
                            },
                        )
                    }
                }

                AppPermission.Gallery -> {
                    PHPhotoLibrary.requestAuthorization { status ->
                        cont.resume(
                            if (status ==
                                PHAuthorizationStatusAuthorized
                            ) {
                                AppPermissionStatus.Granted
                            } else {
                                AppPermissionStatus.Denied
                            },
                        )
                    }
                }
            }
        }

    override fun launchSettings() {
        NSURL.URLWithString(UIApplicationOpenSettingsURLString)?.let {
            UIApplication.sharedApplication.openURL(it)
        }
    }
}
