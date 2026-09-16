/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.storage.data

import android.content.Context
import com.iolandarosa.retailhub.core.common.dispatcher.DispatcherProvider
import com.iolandarosa.retailhub.core.storage.domain.ImageStorageDelegate
import com.iolandarosa.retailhub.core.storage.domain.model.ImageStorageResult
import kotlinx.coroutines.withContext
import java.io.File

internal class AndroidImageStorageImpl(
    private val context: Context,
    private val dispatcherProvider: DispatcherProvider,
) : ImageStorageDelegate {
    private val imagesDir: File by lazy {
        File(context.filesDir, "images").apply { mkdirs() }
    }

    override suspend fun load(fileName: String): ByteArray? =
        withContext(dispatcherProvider.io) {
            val file = File(imagesDir, fileName)
            if (file.exists()) {
                runCatching { file.readBytes() }.getOrNull()
            } else {
                null
            }
        }

    override suspend fun save(
        fileName: String,
        byteArray: ByteArray,
    ): ImageStorageResult =
        withContext(dispatcherProvider.io) {
            runCatching {
                File(imagesDir, fileName).writeBytes(byteArray)
                ImageStorageResult.Success
            }.getOrElse { ImageStorageResult.Failure.Exception(it) }
        }

    override suspend fun delete(fileName: String): ImageStorageResult =
        withContext(dispatcherProvider.io) {
            runCatching {
                val file = File(imagesDir, fileName)
                if (file.exists()) {
                    if (file.delete()) {
                        ImageStorageResult.Success
                    } else {
                        ImageStorageResult.Failure.General
                    }
                } else {
                    ImageStorageResult.Success
                }
            }.getOrElse { ImageStorageResult.Failure.Exception(it) }
        }
}
