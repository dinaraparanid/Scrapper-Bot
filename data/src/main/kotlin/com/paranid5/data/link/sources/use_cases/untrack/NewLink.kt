package com.paranid5.data.link.sources.use_cases.untrack

import com.paranid5.core.entities.link.LinkType
import com.paranid5.data.link.response.LinkResponseChannel
import com.paranid5.data.link.response.respondUntrackLinkNew

suspend inline fun onUntrackNewLink(
    userId: Long,
    link: LinkType,
    linkResponseChannel: LinkResponseChannel
): Unit = linkResponseChannel.respondUntrackLinkNew(userId, link)