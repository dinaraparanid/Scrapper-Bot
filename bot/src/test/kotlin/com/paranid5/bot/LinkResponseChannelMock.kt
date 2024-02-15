package com.paranid5.bot

import com.paranid5.core.bot.ScrapperBot
import com.paranid5.core.entities.link.LinkResponse
import com.paranid5.core.entities.link.LinkType
import com.paranid5.data.link.response.LinkResponseChannel

class LinkResponseChannelMock(
    private val scrapperBot: ScrapperBot
) : LinkResponseChannel {
    override suspend fun respondLinkStorage(
        userId: Long,
        link: LinkType,
        linkResponse: LinkResponse
    ): Unit = scrapperBot.acquireResponse(linkResponse)
}