/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.images

import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ImagePickerControllerImplTest {
    private val delegate = mock<ImagePickerControllerDelegate>()
    private lateinit var picker: ImagePickerControllerImpl

    @BeforeTest
    fun setup() {
        picker = ImagePickerControllerImpl(delegate)
    }

    @Test
    fun pickImage_callsDelegate() =
        runTest {
            val source = ImageSource.Camera
            val expectedBytes = byteArrayOf(1, 2, 3)
            everySuspend { delegate.pickImage(source) } returns expectedBytes

            val bytes = picker.pickImage(source)

            assertEquals(expectedBytes, bytes)
            verifySuspend { delegate.pickImage(source) }
        }
}
