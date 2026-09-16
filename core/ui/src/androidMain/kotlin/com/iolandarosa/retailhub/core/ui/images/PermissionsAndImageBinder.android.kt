/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.images

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import com.iolandarosa.retailhub.core.ui.extensions.toAndroid
import com.iolandarosa.retailhub.core.ui.permissions.AndroidPermissionControllerControllerDelegate
import com.iolandarosa.retailhub.core.ui.permissions.AppPermissionStatus
import com.iolandarosa.retailhub.core.ui.permissions.PermissionController
import com.iolandarosa.retailhub.core.ui.permissions.PermissionControllerImpl
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.ByteArrayOutputStream
import kotlin.coroutines.resume

@Composable
actual fun InitializePermissionsAndPicker(
    permissionController: PermissionController,
    imagePickerController: ImagePickerController,
) {
    BindPermissionsAndPicker(
        permissionController = permissionController,
        imagePickerController = imagePickerController,
    )
}

@Composable
fun BindPermissionsAndPicker(
    permissionController: PermissionController,
    imagePickerController: ImagePickerController,
) {
    val context = LocalContext.current

    // 1. Permission Launcher
    var permissionCallback: ((Boolean) -> Unit)? = null
    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { granted -> permissionCallback?.invoke(granted) }

    // 2. Gallery Launcher
    var galleryCallback: ((ByteArray?) -> Unit)? = null
    val galleryLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent(),
        ) { uri ->
            val bytes = uri?.let { context.contentResolver.openInputStream(it)?.readBytes() }
            galleryCallback?.invoke(bytes)
        }

    // 3. Camera Launcher
    var cameraCallback: ((ByteArray?) -> Unit)? = null
    val cameraLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicturePreview(),
        ) { bitmap ->
            val stream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            cameraCallback?.invoke(bitmap?.let { stream.toByteArray() })
        }

    val permissionDelegate =
        (permissionController as? PermissionControllerImpl)?.delegate as? AndroidPermissionControllerControllerDelegate
    val imageDelegate =
        (imagePickerController as? ImagePickerControllerImpl)?.delegate as?
            AndroidPermissionControllerControllerDelegate

    DisposableEffect(permissionDelegate, imageDelegate) {
        permissionDelegate?.activityProvider = { context.findActivity() }

        permissionDelegate?.requestPermissionHandler = { permission ->
            suspendCancellableCoroutine { cont ->
                permissionCallback = { granted ->
                    cont.resume(
                        if (granted) {
                            AppPermissionStatus.Granted
                        } else {
                            AppPermissionStatus.Denied
                        },
                    )
                }

                val androidPermission = permission.toAndroid()
                if (androidPermission.isBlank()) {
                    cont.resume(AppPermissionStatus.Granted)
                } else {
                    runCatching {
                        permissionLauncher.launch(androidPermission)
                    }.onFailure {
                        cont.resume(AppPermissionStatus.Denied)
                    }
                }
            }
        }

        imageDelegate?.pickImageHandler = { source ->
            suspendCancellableCoroutine { cont ->
                when (source) {
                    ImageSource.Gallery -> {
                        galleryCallback = { cont.resume(it) }
                        galleryLauncher.launch("image/*")
                    }

                    ImageSource.Camera -> {
                        cameraCallback = { cont.resume(it) }
                        cameraLauncher.launch(null)
                    }
                }
            }
        }

        onDispose {
            permissionDelegate?.requestPermissionHandler = null
            imageDelegate?.pickImageHandler = null
            permissionDelegate?.activityProvider = null
        }
    }
}

private fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}
