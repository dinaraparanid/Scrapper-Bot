package com.paranid5.bot.data.link.response

import com.paranid5.bot.domain.bot.ScrapperBot
import com.paranid5.bot.domain.links.LinkType
import org.springframework.stereotype.Component

@Component
class LinkResponseChannelImpl(
    private val scrapperBot: ScrapperBot,
) : LinkResponseChannel {
    override suspend fun respondLinkStorage(
        userId: Long,
        link: LinkType,
        linkResponse: LinkResponse
    ): Unit = scrapperBot.linkResponseFlow.emit(linkResponse)
}