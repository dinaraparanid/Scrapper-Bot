package com.paranid5.bot.data.link.response

import com.paranid5.bot.domain.bot.ScrapperBot
import com.paranid5.bot.domain.links.LinkType
import org.springframework.stereotype.Component

@Component("response_chan_impl")
class LinkResponseChannelImpl(
    private val scrapperBot: ScrapperBot,
) : LinkResponseChannel {
    override suspend fun respondLinkStorage(
        userId: Long,
        link: LinkType,
        linkResponse: LinkResponse
    ): Unit = scrapperBot.acquireResponse(linkResponse)
}