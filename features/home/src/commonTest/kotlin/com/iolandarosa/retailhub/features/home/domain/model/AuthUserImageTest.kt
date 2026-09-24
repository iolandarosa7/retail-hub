/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.home.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

class AuthUserImageTest {
    @Test
    fun defaultValues_areNull() {
        val authUserImage = AuthUserImage()

        assertNull(authUserImage.url)
        assertNull(authUserImage.bytes)
    }

    @Test
    fun customValues_areSetCorrectly() {
        val bytes = byteArrayOf(1, 2, 3)
        val authUserImage = AuthUserImage(url = "https://example.com/image.png", bytes = bytes)

        assertEquals("https://example.com/image.png", authUserImage.url)
        assertEquals(bytes, authUserImage.bytes)
    }

    @Test
    fun equals_sameInstance_returnsTrue() {
        val authUserImage = AuthUserImage(url = "https://example.com/image.png", bytes = byteArrayOf(1, 2, 3))

        assertEquals(authUserImage, authUserImage)
    }

    @Test
    fun equals_sameValues_returnsTrue() {
        val image1 = AuthUserImage(url = "https://example.com/image.png", bytes = byteArrayOf(1, 2, 3))
        val image2 = AuthUserImage(url = "https://example.com/image.png", bytes = byteArrayOf(1, 2, 3))

        assertEquals(image1, image2)
    }

    @Test
    fun equals_differentUrl_returnsFalse() {
        val image1 = AuthUserImage(url = "https://example.com/image1.png", bytes = byteArrayOf(1, 2, 3))
        val image2 = AuthUserImage(url = "https://example.com/image2.png", bytes = byteArrayOf(1, 2, 3))

        assertNotEquals(image1, image2)
    }

    @Test
    fun equals_differentBytes_returnsFalse() {
        val image1 = AuthUserImage(url = "https://example.com/image.png", bytes = byteArrayOf(1, 2, 3))
        val image2 = AuthUserImage(url = "https://example.com/image.png", bytes = byteArrayOf(4, 5, 6))

        assertNotEquals(image1, image2)
    }

    @Test
    fun equals_nullOrDifferentType_returnsFalse() {
        val image = AuthUserImage(url = "https://example.com/image.png")

        assertNotEquals(image as Any, "https://example.com/image.png")
    }

    @Test
    fun hashCode_sameValues_returnsSameHashCode() {
        val image1 = AuthUserImage(url = "https://example.com/image.png", bytes = byteArrayOf(1, 2, 3))
        val image2 = AuthUserImage(url = "https://example.com/image.png", bytes = byteArrayOf(1, 2, 3))

        assertEquals(image1.hashCode(), image2.hashCode())
    }

    @Test
    fun hashCode_nullValues_doesNotThrow() {
        val image = AuthUserImage()

        assertEquals(0, image.hashCode())
    }
}
