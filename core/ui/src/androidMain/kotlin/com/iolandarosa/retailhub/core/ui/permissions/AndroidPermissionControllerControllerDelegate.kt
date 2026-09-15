/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.iolandarosa.retailhub.core.datastore.domain.PreferencesManager
import com.iolandarosa.retailhub.core.ui.extension.toPreferencesKey
import com.iolandarosa.retailhub.core.ui.extensions.toAndroid
import com.iolandarosa.retailhub.core.ui.images.ImagePickerControllerDelegate
import com.iolandarosa.retailhub.core.ui.images.ImageSource
import kotlinx.coroutines.flow.firstOrNull

class AndroidPermissionControllerControllerDelegate(
    private val context: Context,
    private val preferencesManager: PreferencesManager,
) : PermissionControllerDelegate,
    ImagePickerControllerDelegate {
    var requestPermissionHandler: (suspend (AppPermission) -> AppPermissionStatus)? = null
    var pickImageHandler: (suspend (ImageSource) -> ByteArray?)? = null
    var activityProvider: (() -> Activity?)? = null

    override suspend fun checkPermission(permission: AppPermission): AppPermissionStatus {
        val androidPermission = permission.toAndroid()

        if (androidPermission.isBlank()) {
            return AppPermissionStatus.Granted
        }

        val status = ContextCompat.checkSelfPermission(context, androidPermission)
        if (status == PackageManager.PERMISSION_GRANTED) {
            return AppPermissionStatus.Granted
        }

        val activity = activityProvider?.invoke()

        return if (activity?.shouldShowRequestPermissionRationale(androidPermission) == true) {
            AppPermissionStatus.ShouldRequest(showRational = true)
        } else {
            val hasRequested =
                preferencesManager.hasRequestedPermission(permission.toPreferencesKey()).firstOrNull() ?: false
            if (hasRequested) {
                AppPermissionStatus.Denied
            } else {
                AppPermissionStatus.ShouldRequest(showRational = false)
            }
        }
    }

    override suspend fun requestPermission(permission: AppPermission): AppPermissionStatus =
        requestPermissionHandler?.invoke(permission) ?: AppPermissionStatus.Denied

    override suspend fun pickImage(source: ImageSource): ByteArray? = pickImageHandler?.invoke(source)

    override fun launchSettings() {
        runCatching {
            val intent =
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            context.startActivity(intent)
        }.onFailure { error("Failure while launching settings ${it.message}") }
    }
}
