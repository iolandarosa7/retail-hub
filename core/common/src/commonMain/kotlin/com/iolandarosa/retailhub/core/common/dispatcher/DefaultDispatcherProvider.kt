/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.common.dispatcher

import kotlinx.coroutines.Dispatchers

object DefaultDispatcherProvider : DispatcherProvider {
    override val main = Dispatchers.Main
    override val io = Dispatchers.Default
    override val default = Dispatchers.Default
}
