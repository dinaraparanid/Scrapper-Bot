package com.paranid5.data.link.sources

import com.paranid5.data.link.response.LinkResponseChannel
import kotlinx.coroutines.flow.Flow

interface LinkDataSource {
    val usersTrackingsFlow: Flow<Map<Long, List<String>>>

    suspend fun patchTrackings(userId: Long, links: List<String>)

    suspend fun trackLink(userId: Long, link: String)

    suspend fun untrackLink(userId: Long, link: String)

    suspend fun launchLinksStoreMonitoring(linkResponseChannel: LinkResponseChannel)

    suspend fun launchLinksRemoveMonitoring(linkResponseChannel: LinkResponseChannel)
}