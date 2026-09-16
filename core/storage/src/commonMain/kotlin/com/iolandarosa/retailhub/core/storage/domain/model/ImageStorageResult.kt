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

        data class Save(
            val message: String,
        ) : Failure

        data class Delete(
            val message: String,
        ) : Failure

        data object General : Failure

        data class Exception(
            val throwable: Throwable,
        ) : Failure
    }
}
