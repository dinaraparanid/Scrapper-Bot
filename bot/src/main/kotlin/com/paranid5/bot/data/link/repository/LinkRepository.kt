package com.paranid5.bot.data.link.repository

import com.paranid5.bot.data.link.response.LinkResponseChannel
import com.paranid5.bot.domain.links.LinkType
import kotlinx.coroutines.flow.Flow

interface LinkRepository {
    val usersTrackingsFlow: Flow<Map<Long, List<String>>>

    suspend fun trackLink(userId: Long, link: LinkType)

    suspend fun untrackLink(userId: Long, link: LinkType)

    fun launchSourcesLinksMonitoring(linksResponseChannel: LinkResponseChannel)
}