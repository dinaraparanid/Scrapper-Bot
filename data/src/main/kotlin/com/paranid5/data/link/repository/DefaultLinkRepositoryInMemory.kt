package com.paranid5.data.link.repository

import com.paranid5.com.paranid5.utils.extend
import com.paranid5.core.entities.link.LinkType
import com.paranid5.data.link.response.LinkResponseChannel
import com.paranid5.data.link.sources.github.GitHubDataSource
import com.paranid5.data.link.sources.stack_overflow.StackOverflowDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DefaultLinkRepositoryInMemory(
    private val githubDataSource: GitHubDataSource,
    private val stackOverflowDataSource: StackOverflowDataSource,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO),
    private val coroutineContext: CoroutineContext = Dispatchers.IO
) : LinkRepository {
    override val usersTrackingsFlow: Flow<Map<Long, List<String>>> by lazy {
        combine(
            githubDataSource.usersTrackingsFlow,
            stackOverflowDataSource.usersTrackingsFlow
        ) { gh, so ->
            gh extend so
        }
    }

    override suspend fun trackLink(userId: Long, link: LinkType): Unit =
        when (link) {
            is LinkType.GitHubLink -> githubDataSource.trackLink(userId, link.link)
            is LinkType.StackOverflowLink -> stackOverflowDataSource.trackLink(userId, link.link)
        }

    override suspend fun untrackLink(userId: Long, link: LinkType): Unit =
        when (link) {
            is LinkType.GitHubLink -> githubDataSource.untrackLink(userId, link.link)
            is LinkType.StackOverflowLink -> stackOverflowDataSource.untrackLink(userId, link.link)
        }

    override fun launchSourcesLinksMonitoring(linksResponseChannel: LinkResponseChannel) {
        coroutineScope.launch(coroutineContext) {
            githubDataSource.launchLinksStoreMonitoring(linksResponseChannel)
        }

        coroutineScope.launch(coroutineContext) {
            githubDataSource.launchLinksRemoveMonitoring(linksResponseChannel)
        }

        coroutineScope.launch(coroutineContext) {
            stackOverflowDataSource.launchLinksStoreMonitoring(linksResponseChannel)
        }

        coroutineScope.launch(coroutineContext) {
            stackOverflowDataSource.launchLinksRemoveMonitoring(linksResponseChannel)
        }
    }
}