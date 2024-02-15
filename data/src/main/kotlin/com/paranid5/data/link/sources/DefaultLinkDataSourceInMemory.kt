package com.paranid5.data.link.sources

import com.paranid5.core.entities.link.LinkType
import com.paranid5.data.link.UserWithLink
import com.paranid5.data.link.response.LinkResponseChannel
import com.paranid5.data.link.sources.use_cases.track.onTrackNewLink
import com.paranid5.data.link.sources.use_cases.track.onTrackPresentLink
import com.paranid5.data.link.sources.use_cases.untrack.onUntrackNewLink
import com.paranid5.data.link.sources.use_cases.untrack.onUntrackPresentLink
import kotlinx.coroutines.flow.*

class DefaultLinkDataSourceInMemory(private val linkType: (String) -> LinkType) : LinkDataSource {
    private val storeLinksFlow by lazy {
        MutableSharedFlow<UserWithLink>()
    }

    private val removeLinksFlow by lazy {
        MutableSharedFlow<UserWithLink>()
    }

    private val _usersTrackingsFlow by lazy {
        MutableStateFlow(mapOf<Long, List<String>>())
    }

    override val usersTrackingsFlow: Flow<Map<Long, List<String>>> by lazy {
        _usersTrackingsFlow
    }

    override suspend fun patchTrackings(userId: Long, links: List<String>): Unit =
        _usersTrackingsFlow.update { it + (userId to links) }

    override suspend fun trackLink(userId: Long, link: String): Unit =
        storeLinksFlow.emit(UserWithLink(userId, link))

    override suspend fun untrackLink(userId: Long, link: String): Unit =
        removeLinksFlow.emit(UserWithLink(userId, link))

    override suspend fun launchLinksStoreMonitoring(linkResponseChannel: LinkResponseChannel): Unit =
        combine(storeLinksFlow, usersTrackingsFlow) { userWithLink, links ->
            userWithLink to (links[userWithLink.userId] ?: emptyList())
        }.distinctUntilChanged { (oldUL, _), (newUL, _) ->
            oldUL == newUL
        }.map { (ul, links) ->
            Triple(ul.userId, ul.link, links)
        }.collect { (userId, link, links) ->
            when (link) {
                in links -> onTrackPresentLink(userId, linkType(link), linkResponseChannel)
                else -> onTrackNewLink(userId, linkType(link), links, linkResponseChannel)
            }
        }

    override suspend fun launchLinksRemoveMonitoring(linkResponseChannel: LinkResponseChannel): Unit =
        combine(removeLinksFlow, usersTrackingsFlow) { userWithLink, links ->
            userWithLink to (links[userWithLink.userId] ?: emptyList())
        }.distinctUntilChanged { (oldUL, _), (newUL, _) ->
            oldUL == newUL
        }.map { (ul, links) ->
            Triple(ul.userId, ul.link, links)
        }.collect { (userId, link, links) ->
            when (link) {
                in links -> onUntrackPresentLink(userId, linkType(link), links, linkResponseChannel)
                else -> onUntrackNewLink(userId, linkType(link), linkResponseChannel)
            }
        }
}