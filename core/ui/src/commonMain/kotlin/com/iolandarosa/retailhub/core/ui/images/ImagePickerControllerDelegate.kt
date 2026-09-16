/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.images

interface ImagePickerControllerDelegate {
    suspend fun pickImage(source: ImageSource): ByteArray?
}
