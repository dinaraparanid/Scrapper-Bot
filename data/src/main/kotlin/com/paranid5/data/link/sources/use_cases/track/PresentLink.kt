package com.paranid5.data.link.sources.use_cases.track

import com.paranid5.core.entities.link.LinkType
import com.paranid5.data.link.response.LinkResponseChannel
import com.paranid5.data.link.response.respondTrackLinkPresent

suspend inline fun onTrackPresentLink(
    userId: Long,
    link: LinkType,
    linkResponseChannel: LinkResponseChannel
): Unit = linkResponseChannel.respondTrackLinkPresent(userId, link)