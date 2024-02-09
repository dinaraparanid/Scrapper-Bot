package com.paranid5.bot.data.link.sources.use_cases.untrack

import com.paranid5.bot.data.link.response.LinkResponseChannel
import com.paranid5.bot.data.link.response.respondUntrackLinkPresent
import com.paranid5.bot.data.link.sources.LinkDataSource
import com.paranid5.bot.domain.links.LinkType

context(LinkDataSource)
suspend inline fun onUntrackPresentLink(
    userId: Long,
    link: LinkType,
    links: List<String>,
    linkResponseChannel: LinkResponseChannel,
) {
    patchTrackings(userId, links - link.link)
    linkResponseChannel.respondUntrackLinkPresent(userId, link)
}