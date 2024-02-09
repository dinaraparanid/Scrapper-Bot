package com.paranid5.bot.data.link.repository

import com.paranid5.bot.data.link.response.LinkResponseChannel
import com.paranid5.bot.data.link.sources.github.GitHubDataSource
import com.paranid5.bot.data.link.sources.stack_overflow.StackOverflowDataSource
import com.paranid5.bot.domain.links.LinkType
import com.paranid5.bot.domain.utils.extend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component("link_rep_memory")
class LinkRepositoryInMemory(
    @Qualifier("gh_memory")
    private val githubDataSource: GitHubDataSource,
    @Qualifier("so_memory")
    private val stackOverflowDataSource: StackOverflowDataSource,
) : LinkRepository,
    CoroutineScope by CoroutineScope(Dispatchers.IO) {
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
        launch(Dispatchers.IO) { githubDataSource.launchLinksStoreMonitoring(linksResponseChannel) }
        launch(Dispatchers.IO) { githubDataSource.launchLinksRemoveMonitoring(linksResponseChannel) }
        launch(Dispatchers.IO) { stackOverflowDataSource.launchLinksStoreMonitoring(linksResponseChannel) }
        launch(Dispatchers.IO) { stackOverflowDataSource.launchLinksRemoveMonitoring(linksResponseChannel) }
    }
}