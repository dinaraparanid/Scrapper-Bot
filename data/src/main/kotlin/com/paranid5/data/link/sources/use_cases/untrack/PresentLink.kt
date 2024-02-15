package com.paranid5.data.link.sources.use_cases.untrack

import com.paranid5.core.entities.link.LinkType
import com.paranid5.data.link.response.LinkResponseChannel
import com.paranid5.data.link.response.respondUntrackLinkPresent
import com.paranid5.data.link.sources.LinkDataSource

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