package com.paranid5.bot.data.link.sources.use_cases.untrack

import com.paranid5.bot.data.link.response.LinkResponseChannel
import com.paranid5.bot.data.link.response.respondUntrackLinkNew
import com.paranid5.bot.domain.links.LinkType

suspend inline fun onUntrackNewLink(
    userId: Long,
    link: LinkType,
    linkResponseChannel: LinkResponseChannel
): Unit = linkResponseChannel.respondUntrackLinkNew(userId, link)