/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.di

import com.iolandarosa.retailhub.core.ui.images.ImagePickerController
import com.iolandarosa.retailhub.core.ui.images.ImagePickerControllerImpl
import com.iolandarosa.retailhub.core.ui.images.IosImagePickerControllerDelegate
import com.iolandarosa.retailhub.core.ui.permissions.IosPermissionControllerDelegate
import com.iolandarosa.retailhub.core.ui.permissions.PermissionController
import com.iolandarosa.retailhub.core.ui.permissions.PermissionControllerImpl
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformUiModule: Module =
    module {
        factory { IosPermissionControllerDelegate() }
        factory<PermissionController> { PermissionControllerImpl(get<IosPermissionControllerDelegate>()) }
        factory { IosImagePickerControllerDelegate() }
        factory<ImagePickerController> { ImagePickerControllerImpl(get<IosImagePickerControllerDelegate>()) }
    }
