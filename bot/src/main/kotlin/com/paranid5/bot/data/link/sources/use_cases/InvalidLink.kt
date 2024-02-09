package com.paranid5.bot.data.link.sources.use_cases

import com.paranid5.bot.data.link.response.LinkResponseChannel
import com.paranid5.bot.data.link.response.respondLinkInvalid
import com.paranid5.bot.domain.links.LinkType

suspend inline fun onInvalidLink(
    userId: Long,
    link: LinkType,
    linkResponseChannel: LinkResponseChannel
): Unit = linkResponseChannel.respondLinkInvalid(userId, link)