/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.permissions

import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PermissionControllerImplTest {
    private val delegate = mock<PermissionControllerDelegate>()
    private lateinit var controller: PermissionControllerImpl

    @BeforeTest
    fun setup() {
        controller = PermissionControllerImpl(delegate)
    }

    @Test
    fun checkPermission_callsDelegate() =
        runTest {
            val permission = AppPermission.Camera
            val expectedStatus = AppPermissionStatus.Granted
            everySuspend { delegate.checkPermission(permission) } returns expectedStatus

            val status = controller.checkPermission(permission)

            assertEquals(expectedStatus, status)
            verifySuspend { delegate.checkPermission(permission) }
        }

    @Test
    fun requestPermission_callsDelegate() =
        runTest {
            val permission = AppPermission.Camera
            val expectedStatus = AppPermissionStatus.Granted
            everySuspend { delegate.requestPermission(permission) } returns expectedStatus

            val status = controller.requestPermission(permission)

            assertEquals(expectedStatus, status)
            verifySuspend { delegate.requestPermission(permission) }
        }

    @Test
    fun launchSettings_callsDelegate() {
        every { delegate.launchSettings() } returns Unit
        controller.launchSettings()

        verify { delegate.launchSettings() }
    }
}
