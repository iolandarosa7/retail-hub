/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.images

internal class ImagePickerControllerImpl(
    val delegate: ImagePickerControllerDelegate,
) : ImagePickerController {
    override suspend fun pickImage(source: ImageSource): ByteArray? = delegate.pickImage(source)
}
