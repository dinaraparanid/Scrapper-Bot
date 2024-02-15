package com.paranid5.data.link.sources.use_cases.track

import com.paranid5.core.entities.link.LinkType
import com.paranid5.data.link.response.LinkResponseChannel
import com.paranid5.data.link.response.respondTrackLinkNew
import com.paranid5.data.link.sources.LinkDataSource

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