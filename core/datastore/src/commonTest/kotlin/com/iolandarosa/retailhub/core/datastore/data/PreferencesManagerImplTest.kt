/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.datastore.data

import com.iolandarosa.retailhub.core.datastore.FakeDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PreferencesManagerImplTest {
    private lateinit var testDataStore: FakeDataStore
    private lateinit var preferencesManager: PreferencesManagerImpl

    private val permissionName = "permissionName"

    @BeforeTest
    fun setup() {
        testDataStore = FakeDataStore()
        preferencesManager = PreferencesManagerImpl(testDataStore.dataStore)
    }

    @AfterTest
    fun tearDown() {
        testDataStore.cleanup()
    }

    @Test
    fun withTokensInfo_getAuthToken_returnsExpectedValue() =
        runTest {
            assertFalse(preferencesManager.hasRequestedPermission(permissionName).first())

            preferencesManager.setPermissionRequested(permissionName)

            assertTrue(preferencesManager.hasRequestedPermission(permissionName).first())
        }

    @Test
    fun withEmptyDataStore_hasRequestedPermission_returnsFalse() =
        runTest {
            assertFalse(preferencesManager.hasRequestedPermission(permissionName).first())
        }
}
