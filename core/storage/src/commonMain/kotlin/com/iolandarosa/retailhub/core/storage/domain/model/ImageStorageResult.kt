/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.storage.domain.model

sealed interface ImageStorageResult {
    data object Success : ImageStorageResult

    sealed interface Failure : ImageStorageResult {
        data object DirectoryNotFound : Failure

        data class General(
            val message: String? = null,
        ) : Failure

        data class Exception(
            val throwable: Throwable,
        ) : Failure
    }
}
