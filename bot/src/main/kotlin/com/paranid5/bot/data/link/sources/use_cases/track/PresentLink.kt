package com.paranid5.bot.data.link.sources.use_cases.track

import com.paranid5.bot.data.link.response.LinkResponseChannel
import com.paranid5.bot.data.link.response.respondTrackLinkPresent
import com.paranid5.bot.domain.links.LinkType

suspend inline fun onTrackPresentLink(
    userId: Long,
    link: LinkType,
    linkResponseChannel: LinkResponseChannel
): Unit = linkResponseChannel.respondTrackLinkPresent(userId, link)