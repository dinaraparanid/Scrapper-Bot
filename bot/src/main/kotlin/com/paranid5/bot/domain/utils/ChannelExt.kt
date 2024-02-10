package com.paranid5.bot.domain.utils

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration

suspend inline fun <T> Channel<T>.receiveTimeout(duration: Duration) = runCatching {
    withTimeout(duration) { receive() }
}

suspend inline fun <T> Channel<T>.receiveTimeout(millis: Long) = runCatching {
    withTimeout(millis) { receive() }
}