package com.paranid5.data.link.response

import com.paranid5.core.entities.link.LinkResponse
import com.paranid5.core.entities.link.LinkType

interface LinkResponseChannel {
    suspend fun respondLinkStorage(
        userId: Long,
        link: LinkType,
        linkResponse: LinkResponse,
    )
}

suspend fun LinkResponseChannel.respondTrackLinkPresent(userId: Long, link: LinkType): Unit =
    respondLinkImpl(userId, link, LinkResponse.TrackResponse::Present)

suspend fun LinkResponseChannel.respondTrackLinkNew(userId: Long, link: LinkType): Unit =
    respondLinkImpl(userId, link, LinkResponse.TrackResponse::New)

suspend fun LinkResponseChannel.respondUntrackLinkPresent(userId: Long, link: LinkType): Unit =
    respondLinkImpl(userId, link, LinkResponse.UntrackResponse::Present)

suspend fun LinkResponseChannel.respondUntrackLinkNew(userId: Long, link: LinkType): Unit =
    respondLinkImpl(userId, link, LinkResponse.UntrackResponse::New)

suspend fun LinkResponseChannel.respondLinkInvalid(userId: Long, link: LinkType): Unit =
    respondLinkImpl(userId, link, LinkResponse::Invalid)

private suspend inline fun LinkResponseChannel.respondLinkImpl(
    chatId: Long,
    link: LinkType,
    response: (Long, String) -> LinkResponse
): Unit = respondLinkStorage(
    userId = chatId,
    link = link,
    linkResponse = response(chatId, link.link),
)