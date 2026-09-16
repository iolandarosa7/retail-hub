/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.domain.extensions

import com.iolandarosa.retailhub.core.storage.domain.model.ImageStorageResult
import com.iolandarosa.retailhub.core.ui.snackbar.SnackBarData
import com.iolandarosa.retailhub.core.ui.snackbar.SnackBarType
import retailhub.features.profile.generated.resources.Res
import retailhub.features.profile.generated.resources.error_image_delete
import retailhub.features.profile.generated.resources.error_image_directory_not_found
import retailhub.features.profile.generated.resources.error_image_save

fun ImageStorageResult.Failure.toSnackBarData(isDelete: Boolean) =
    SnackBarData(
        messageId =
            when (this) {
                ImageStorageResult.Failure.DirectoryNotFound -> {
                    Res.string.error_image_directory_not_found
                }

                else -> {
                    if (isDelete) {
                        Res.string.error_image_delete
                    } else {
                        Res.string.error_image_save
                    }
                }
            },
        message =
            when (this) {
                ImageStorageResult.Failure.DirectoryNotFound -> ""
                is ImageStorageResult.Failure.Exception -> this.throwable.message ?: ""
                is ImageStorageResult.Failure.General -> this.message ?: ""
            },
        type = SnackBarType.ERROR,
    )
