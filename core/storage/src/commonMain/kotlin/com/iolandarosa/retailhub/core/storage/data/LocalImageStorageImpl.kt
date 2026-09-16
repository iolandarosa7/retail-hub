/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.storage.data

import com.iolandarosa.retailhub.core.storage.domain.LocalImageStorage
import com.iolandarosa.retailhub.core.storage.domain.LocalImageStorageDelegate
import com.iolandarosa.retailhub.core.storage.domain.model.ImageStorageResult

internal class LocalImageStorageImpl(
    private val delegate: LocalImageStorageDelegate,
) : LocalImageStorage {
    override suspend fun load(fileName: String): ByteArray? = delegate.load(fileName)

    override suspend fun save(
        fileName: String,
        byteArray: ByteArray,
    ): ImageStorageResult = delegate.save(fileName, byteArray)

    override suspend fun delete(fileName: String): ImageStorageResult = delegate.delete(fileName)
}
