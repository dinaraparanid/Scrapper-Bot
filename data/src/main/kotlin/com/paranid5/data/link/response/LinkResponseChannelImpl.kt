package com.paranid5.data.link.response

import com.paranid5.core.bot.ScrapperBot
import com.paranid5.core.entities.link.LinkResponse
import com.paranid5.core.entities.link.LinkType
import org.springframework.stereotype.Component

@Component
class LinkResponseChannelImpl(
    private val scrapperBot: ScrapperBot,
) : LinkResponseChannel {
    override suspend fun respondLinkStorage(
        userId: Long,
        link: LinkType,
        linkResponse: LinkResponse
    ): Unit = scrapperBot.acquireResponse(linkResponse)
}