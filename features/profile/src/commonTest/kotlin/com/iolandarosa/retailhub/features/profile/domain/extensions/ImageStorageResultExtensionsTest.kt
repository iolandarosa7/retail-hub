/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.domain.extensions

import com.iolandarosa.retailhub.core.storage.domain.model.ImageStorageResult
import com.iolandarosa.retailhub.core.ui.snackbar.SnackBarType
import retailhub.features.profile.generated.resources.Res
import retailhub.features.profile.generated.resources.error_image_delete
import retailhub.features.profile.generated.resources.error_image_directory_not_found
import retailhub.features.profile.generated.resources.error_image_save
import kotlin.test.Test
import kotlin.test.assertEquals

class ImageStorageResultExtensionsTest {
    @Test
    fun directoryNotFound_toSnackBarData_returnsCorrectSnackBarData() {
        val failure = ImageStorageResult.Failure.DirectoryNotFound

        val snackBarDataDelete = failure.toSnackBarData(isDelete = true)
        assertEquals(Res.string.error_image_directory_not_found, snackBarDataDelete.messageId)
        assertEquals("", snackBarDataDelete.message)
        assertEquals(SnackBarType.ERROR, snackBarDataDelete.type)

        val snackBarDataSave = failure.toSnackBarData(isDelete = false)
        assertEquals(Res.string.error_image_directory_not_found, snackBarDataSave.messageId)
        assertEquals("", snackBarDataSave.message)
        assertEquals(SnackBarType.ERROR, snackBarDataSave.type)
    }

    @Test
    fun generalFailure_toSnackBarData_whenDelete_returnsDeleteError() {
        val failure = ImageStorageResult.Failure.General(message = "Something went wrong")

        val snackBarData = failure.toSnackBarData(isDelete = true)
        assertEquals(Res.string.error_image_delete, snackBarData.messageId)
        assertEquals("Something went wrong", snackBarData.message)
        assertEquals(SnackBarType.ERROR, snackBarData.type)
    }

    @Test
    fun generalFailure_toSnackBarData_whenSave_returnsSaveError() {
        val failure = ImageStorageResult.Failure.General(message = "Save failed")

        val snackBarData = failure.toSnackBarData(isDelete = false)
        assertEquals(Res.string.error_image_save, snackBarData.messageId)
        assertEquals("Save failed", snackBarData.message)
        assertEquals(SnackBarType.ERROR, snackBarData.type)
    }

    @Test
    fun exceptionFailure_toSnackBarData_whenDelete_returnsDeleteErrorWithMessage() {
        val exception = RuntimeException("Exception details")
        val failure = ImageStorageResult.Failure.Exception(exception)

        val snackBarData = failure.toSnackBarData(isDelete = true)
        assertEquals(Res.string.error_image_delete, snackBarData.messageId)
        assertEquals("Exception details", snackBarData.message)
        assertEquals(SnackBarType.ERROR, snackBarData.type)
    }

    @Test
    fun exceptionFailure_toSnackBarData_whenSave_returnsSaveErrorWithMessage() {
        val exception = RuntimeException("Exception details")
        val failure = ImageStorageResult.Failure.Exception(exception)

        val snackBarData = failure.toSnackBarData(isDelete = false)
        assertEquals(Res.string.error_image_save, snackBarData.messageId)
        assertEquals("Exception details", snackBarData.message)
        assertEquals(SnackBarType.ERROR, snackBarData.type)
    }
}
