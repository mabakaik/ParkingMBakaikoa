package com.lksnext.parkingmbakaikoa

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform