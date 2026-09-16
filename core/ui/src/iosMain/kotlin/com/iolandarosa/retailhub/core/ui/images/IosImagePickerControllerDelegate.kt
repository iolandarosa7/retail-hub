/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.images

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSData
import platform.Foundation.getBytes
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerCameraCaptureMode
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject
import kotlin.coroutines.resume

class IosImagePickerControllerDelegate : ImagePickerControllerDelegate {
    // Retain the delegate to prevent deallocation (since UIImagePickerController.delegate is weak)
    private var currentDelegate: NSObject? = null

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun pickImage(source: ImageSource): ByteArray? =
        suspendCancellableCoroutine { cont ->
            val root = UIApplication.sharedApplication.keyWindow?.rootViewController
            if (root == null) {
                cont.resume(null)
                return@suspendCancellableCoroutine
            }

            val imagePicker = UIImagePickerController()

            val cameraDelegate =
                object : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {
                    override fun imagePickerController(
                        picker: UIImagePickerController,
                        didFinishPickingMediaWithInfo: Map<Any?, *>,
                    ) {
                        val image =
                            didFinishPickingMediaWithInfo[UIImagePickerControllerEditedImage] as? UIImage
                                ?: didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage

                        val imageData = image?.let { UIImageJPEGRepresentation(it, 0.8) }
                        val bytes = imageData?.toByteArray()

                        picker.dismissViewControllerAnimated(true, null)
                        cont.resume(bytes)
                    }

                    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
                        picker.dismissViewControllerAnimated(true, null)
                        cont.resume(null)
                    }
                }

            imagePicker.setSourceType(
                when (source) {
                    ImageSource.Camera -> UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
                    else -> UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
                },
            )
            imagePicker.setAllowsEditing(true)
            if (source == ImageSource.Camera) {
                imagePicker.setCameraCaptureMode(
                    UIImagePickerControllerCameraCaptureMode.UIImagePickerControllerCameraCaptureModePhoto,
                )
            }
            imagePicker.setDelegate(cameraDelegate)

            currentDelegate = cameraDelegate

            try {
                root.presentViewController(imagePicker, true, null)
            } catch (_: Exception) {
                currentDelegate = null
                cont.resume(null)
            }
        }

    @OptIn(ExperimentalForeignApi::class)
    private fun NSData.toByteArray(): ByteArray {
        val length = this.length.toInt()
        val bytes = ByteArray(length)
        if (length > 0) {
            bytes.usePinned {
                this.getBytes(it.addressOf(0), this.length)
            }
        }
        return bytes
    }
}
