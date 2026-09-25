/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.composeapp.navigation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateList

@Stable
class Navigator(
    val backStack: SnapshotStateList<AppRoute>,
) {
    fun navigate(route: AppRoute) {
        if (backStack.lastOrNull() == route) {
            return
        }

        backStack.add(route)
    }

    fun pop() {
        if (backStack.size > 1) {
            backStack.removeLast()
        }
    }
}
