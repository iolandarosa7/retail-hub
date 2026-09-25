/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.composeapp.navigation

import androidx.compose.runtime.mutableStateListOf
import kotlin.test.Test
import kotlin.test.assertEquals

class NavigatorTest {
    private val navigator = Navigator(mutableStateListOf(HomeRoute))

    @Test
    fun initialState_containsInitialRoute() {
        assertEquals(1, navigator.backStack.size)
        assertEquals(listOf<AppRoute>(HomeRoute), navigator.backStack)
    }

    @Test
    fun initialState_navigateToAnotherRoute_addRouteToBackStack() {
        assertEquals(1, navigator.backStack.size)

        navigator.navigate(ProfileRoute)

        assertEquals(2, navigator.backStack.size)
        assertEquals(listOf(HomeRoute, ProfileRoute), navigator.backStack)
    }

    @Test
    fun initialState_navigateToSameRoute_keepCurrentBackStack() {
        assertEquals(1, navigator.backStack.size)

        navigator.navigate(HomeRoute)

        assertEquals(1, navigator.backStack.size)
        assertEquals(listOf<AppRoute>(HomeRoute), navigator.backStack)
    }

    @Test
    fun moreThanOneRoute_pop_removesLastRoute() {
        val navigator = Navigator(mutableStateListOf(HomeRoute, ProfileRoute))

        navigator.pop()

        assertEquals(1, navigator.backStack.size)
        assertEquals(listOf<AppRoute>(HomeRoute), navigator.backStack)
    }

    @Test
    fun initialState_pop_doesNotRemoveInitialRoute() {
        assertEquals(1, navigator.backStack.size)
        assertEquals(listOf<AppRoute>(HomeRoute), navigator.backStack)

        navigator.pop()

        assertEquals(1, navigator.backStack.size)
        assertEquals(listOf<AppRoute>(HomeRoute), navigator.backStack)
    }
}
