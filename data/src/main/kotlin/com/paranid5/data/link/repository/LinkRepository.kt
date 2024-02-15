package com.paranid5.data.link.repository

import com.paranid5.data.link.response.LinkResponseChannel
import com.paranid5.core.entities.link.LinkType
import kotlinx.coroutines.flow.Flow

interface LinkRepository {
    val usersTrackingsFlow: Flow<Map<Long, List<String>>>

    suspend fun trackLink(userId: Long, link: LinkType)

    suspend fun untrackLink(userId: Long, link: LinkType)

    fun launchSourcesLinksMonitoring(linksResponseChannel: LinkResponseChannel)
}