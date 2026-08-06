package com.iolandarosa.retailhub

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform