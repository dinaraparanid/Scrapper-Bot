package com.paranid5.bot.domain.utils

import kotlinx.coroutines.channels.Channel
import kotlin.time.Duration

class AsyncCondVar {
    private val channel = Channel<Unit>(Channel.CONFLATED)

    suspend fun notify() = channel.send(Unit)

    suspend fun wait() = channel.receive()

    suspend fun wait(timeoutMs: Long) =
        channel.receiveTimeout(timeoutMs)

    suspend fun wait(timeout: Duration) =
        channel.receiveTimeout(timeout)
}