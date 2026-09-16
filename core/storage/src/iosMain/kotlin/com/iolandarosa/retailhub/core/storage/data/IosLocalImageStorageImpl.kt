/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.storage.data

import com.iolandarosa.retailhub.core.common.dispatcher.DispatcherProvider
import com.iolandarosa.retailhub.core.storage.domain.LocalImageStorageDelegate
import com.iolandarosa.retailhub.core.storage.domain.model.ImageStorageResult
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import kotlinx.coroutines.withContext
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSData
import platform.Foundation.NSDataWritingAtomic
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.data
import platform.Foundation.dataWithBytes
import platform.Foundation.dataWithContentsOfURL
import platform.Foundation.getBytes
import platform.Foundation.writeToURL

@OptIn(ExperimentalForeignApi::class)
internal class IosLocalImageStorageImpl(
    private val dispatcherProvider: DispatcherProvider,
) : LocalImageStorageDelegate {
    private val fileManager = NSFileManager.defaultManager

    private val imagesDirectory: NSURL? by lazy {
        val applicationSupport =
            fileManager
                .URLsForDirectory(NSApplicationSupportDirectory, NSUserDomainMask)
                .firstOrNull() as? NSURL ?: return@lazy null

        val directory = applicationSupport.URLByAppendingPathComponent("images") ?: return@lazy null

        if (!fileManager.fileExistsAtPath(directory.path.orEmpty())) {
            fileManager.createDirectoryAtURL(directory, true, null, null)
        }
        directory
    }

    private fun fileUrl(fileName: String): NSURL? = imagesDirectory?.URLByAppendingPathComponent(fileName)

    override suspend fun load(fileName: String): ByteArray? =
        withContext(dispatcherProvider.io) {
            val url = fileUrl(fileName) ?: return@withContext null
            val data = NSData.dataWithContentsOfURL(url) ?: return@withContext null
            data.toByteArray()
        }

    @OptIn(BetaInteropApi::class)
    override suspend fun save(
        fileName: String,
        byteArray: ByteArray,
    ): ImageStorageResult =
        withContext(dispatcherProvider.io) {
            runCatching {
                val url = fileUrl(fileName) ?: return@withContext ImageStorageResult.Failure.DirectoryNotFound
                val data = byteArray.toNSData()
                memScoped {
                    val errorPtr = alloc<ObjCObjectVar<NSError?>>()
                    val success = data.writeToURL(url, NSDataWritingAtomic, errorPtr.ptr)
                    if (success) {
                        ImageStorageResult.Success
                    } else {
                        ImageStorageResult.Failure.General(errorPtr.value?.localizedDescription)
                    }
                }
            }.getOrElse { ImageStorageResult.Failure.Exception(it) }
        }

    @OptIn(BetaInteropApi::class)
    override suspend fun delete(fileName: String): ImageStorageResult =
        withContext(dispatcherProvider.io) {
            runCatching {
                val url = fileUrl(fileName) ?: return@runCatching ImageStorageResult.Success
                if (fileManager.fileExistsAtPath(url.path.orEmpty())) {
                    memScoped {
                        val errorPtr = alloc<ObjCObjectVar<NSError?>>()
                        if (!fileManager.removeItemAtURL(url, errorPtr.ptr)) {
                            return@withContext ImageStorageResult.Failure.General(errorPtr.value?.localizedDescription)
                        }
                    }
                }
                ImageStorageResult.Success
            }.getOrElse { ImageStorageResult.Failure.Exception(it) }
        }

    private fun NSData.toByteArray(): ByteArray {
        val bytes = ByteArray(length.toInt())
        if (bytes.isNotEmpty()) {
            bytes.usePinned { getBytes(it.addressOf(0), length) }
        }
        return bytes
    }

    private fun ByteArray.toNSData(): NSData =
        if (isEmpty()) {
            NSData.data()
        } else {
            usePinned { NSData.dataWithBytes(it.addressOf(0), size.toULong()) }
        }
}
