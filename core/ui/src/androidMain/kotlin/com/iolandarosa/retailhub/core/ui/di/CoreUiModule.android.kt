/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.di

import com.iolandarosa.retailhub.core.ui.images.ImagePickerController
import com.iolandarosa.retailhub.core.ui.images.ImagePickerControllerImpl
import com.iolandarosa.retailhub.core.ui.permissions.AndroidPermissionControllerControllerDelegate
import com.iolandarosa.retailhub.core.ui.permissions.PermissionController
import com.iolandarosa.retailhub.core.ui.permissions.PermissionControllerImpl
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformUiModule: Module =
    module {
        factory { AndroidPermissionControllerControllerDelegate(get(), get()) }
        factory<PermissionController> { PermissionControllerImpl(get<AndroidPermissionControllerControllerDelegate>()) }
        factory<ImagePickerController> {
            ImagePickerControllerImpl(
                get<AndroidPermissionControllerControllerDelegate>(),
            )
        }
    }
