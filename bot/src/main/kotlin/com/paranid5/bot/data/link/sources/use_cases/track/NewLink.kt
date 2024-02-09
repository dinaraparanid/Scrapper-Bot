package com.paranid5.bot.data.link.sources.use_cases.track

import com.paranid5.bot.data.link.sources.LinkDataSource
import com.paranid5.bot.data.link.response.LinkResponseChannel
import com.paranid5.bot.data.link.response.respondTrackLinkNew
import com.paranid5.bot.domain.links.LinkType

context(LinkDataSource)
suspend inline fun onTrackNewLink(
    userId: Long,
    link: LinkType,
    links: List<String>,
    linkResponseChannel: LinkResponseChannel,
) {
    patchTrackings(userId, links + link.link)
    linkResponseChannel.respondTrackLinkNew(userId, link)
}