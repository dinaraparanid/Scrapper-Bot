package com.paranid5.core.bot

import com.paranid5.core.entities.link.LinkResponse

interface ScrapperBot {
    fun launchBot()

    suspend fun acquireResponse(response: LinkResponse)
}