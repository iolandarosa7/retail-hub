/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.storage.domain

import com.iolandarosa.retailhub.core.storage.domain.model.ImageStorageResult

internal interface ImageStorageDelegate {
    suspend fun load(fileName: String): ByteArray?

    suspend fun save(
        fileName: String,
        byteArray: ByteArray,
    ): ImageStorageResult

    suspend fun delete(fileName: String): ImageStorageResult
}
