package com.paranid5.bot.domain.bot

import com.paranid5.bot.data.link.response.LinkResponse

interface ScrapperBot {
    fun launchBot()

    suspend fun acquireResponse(response: LinkResponse)
}