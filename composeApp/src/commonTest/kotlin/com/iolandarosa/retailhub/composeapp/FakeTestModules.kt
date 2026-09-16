/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.composeapp

import com.iolandarosa.retailhub.core.datastore.domain.PreferencesManager
import com.iolandarosa.retailhub.core.datastore.domain.TokenManager
import com.iolandarosa.retailhub.core.ui.images.ImagePickerController
import com.iolandarosa.retailhub.core.ui.images.ImageSource
import com.iolandarosa.retailhub.core.ui.permissions.AppPermission
import com.iolandarosa.retailhub.core.ui.permissions.AppPermissionStatus
import com.iolandarosa.retailhub.core.ui.permissions.PermissionController
import dev.mokkery.mock
import org.koin.dsl.module

val fakeTestModule =
    module {
        // overrides TokenManager and PreferencesManager to avoid creating with the real datastore instance
        single<TokenManager> { mock() }
        single<PreferencesManager> { mock() }

        // this modules use context so we mock them
        single<PermissionController> {
            object : PermissionController {
                override suspend fun checkPermission(permission: AppPermission): AppPermissionStatus =
                    AppPermissionStatus.Granted

                override suspend fun requestPermission(permission: AppPermission): AppPermissionStatus =
                    AppPermissionStatus.Granted

                override fun launchSettings() {
                    Unit
                }
            }
        }

        single<ImagePickerController> {
            object : ImagePickerController {
                override suspend fun pickImage(source: ImageSource): ByteArray? = null
            }
        }
    }
