/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.composeapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer

@Composable
fun rememberNavigator(initialRoute: AppRoute): Navigator {
    val backStack =
        rememberSerializable(
            serializer = SnapshotStateListSerializer(AppRoute.serializer()),
        ) {
            mutableStateListOf(initialRoute)
        }

    // the remember here is important so the navigation object isn't recreated in every recomposition
    return remember(backStack) { Navigator(backStack) }
}
