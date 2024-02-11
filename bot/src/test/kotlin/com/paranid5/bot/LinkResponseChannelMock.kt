package com.paranid5.bot

import com.paranid5.bot.data.link.response.LinkResponse
import com.paranid5.bot.data.link.response.LinkResponseChannel
import com.paranid5.bot.domain.links.LinkType

class LinkResponseChannelMock(
    private val scrapperBot: ScrapperBotMock
) : LinkResponseChannel {
    override suspend fun respondLinkStorage(
        userId: Long,
        link: LinkType,
        linkResponse: LinkResponse
    ): Unit = scrapperBot.acquireResponse(linkResponse)
}