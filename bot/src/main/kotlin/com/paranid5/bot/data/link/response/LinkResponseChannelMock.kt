package com.paranid5.bot.data.link.response

import com.paranid5.bot.domain.links.LinkType
import org.springframework.stereotype.Component

@Component("mock")
class LinkResponseChannelMock : LinkResponseChannel {
    override suspend fun respondLinkStorage(
        userId: Long,
        link: LinkType,
        linkResponse: LinkResponse
    ): Unit = Unit
}