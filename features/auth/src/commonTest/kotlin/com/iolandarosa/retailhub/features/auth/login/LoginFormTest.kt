/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.login

import kotlin.test.Test
import kotlin.test.assertEquals

class LoginFormTest {
    @Test
    fun getReturnsUsernameAndPasswordFields() {
        val fields =
            LoginForm.get(
                onValueChanged = {},
                onActionDone = {},
            )

        assertEquals(2, fields.size)
        assertEquals(LoginForm.USERNAME, fields[0].name)
        assertEquals(LoginForm.PASSWORD, fields[1].name)
    }
}
