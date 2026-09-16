/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.storage.data

import com.iolandarosa.retailhub.core.storage.domain.LocalImageStorageDelegate
import com.iolandarosa.retailhub.core.storage.domain.model.ImageStorageResult
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LocalImageStorageImplTest {
    private val delegate = mock<LocalImageStorageDelegate>()
    private lateinit var picker: LocalImageStorageImpl

    @BeforeTest
    fun setup() {
        picker = LocalImageStorageImpl(delegate)
    }

    @Test
    fun validBytes_load_hasExpectedResult() =
        runTest {
            val fileName = "filename"
            val expectedBytes = byteArrayOf(1, 2, 3)
            everySuspend { delegate.load(any()) } returns expectedBytes

            val bytes = picker.load(fileName)

            assertEquals(expectedBytes, bytes)
            verifySuspend { delegate.load(fileName) }
        }

    @Test
    fun nullBytes_load_hasExpectedResult() =
        runTest {
            val fileName = "filename"
            everySuspend { delegate.load(any()) } returns null

            val bytes = picker.load(fileName)

            assertNull(bytes)
            verifySuspend { delegate.load(fileName) }
        }

    @Test
    fun success_delete_hasExpectedResult() =
        runTest {
            val fileName = "filename"
            val expectedResult = ImageStorageResult.Success
            everySuspend { delegate.delete(any()) } returns expectedResult

            val result = picker.delete(fileName)

            assertEquals(expectedResult, result)
            verifySuspend { delegate.delete(fileName) }
        }

    @Test
    fun success_save_hasExpectedResult() =
        runTest {
            val fileName = "filename"
            val byteArray = byteArrayOf(1, 2, 3)
            val expectedResult = ImageStorageResult.Success
            everySuspend { delegate.save(any(), any()) } returns expectedResult

            val result = picker.save(fileName, byteArray)

            assertEquals(expectedResult, result)
            verifySuspend { delegate.save(fileName, byteArray) }
        }
}
