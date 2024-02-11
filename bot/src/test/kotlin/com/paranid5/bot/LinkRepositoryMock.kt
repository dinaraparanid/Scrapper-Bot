package com.paranid5.bot

import com.paranid5.bot.data.link.repository.LinkRepository
import com.paranid5.bot.data.link.response.LinkResponseChannel
import com.paranid5.bot.data.link.sources.github.GitHubDataSource
import com.paranid5.bot.data.link.sources.stack_overflow.StackOverflowDataSource
import com.paranid5.bot.domain.links.LinkType
import com.paranid5.bot.domain.utils.extend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class LinkRepositoryMock(
    private val githubDataSource: GitHubDataSource,
    private val stackOverflowDataSource: StackOverflowDataSource,
    private val testScope: TestScope
) : LinkRepository,
    CoroutineScope by CoroutineScope(UnconfinedTestDispatcher(testScope.testScheduler)) {
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
        testScope.backgroundScope.launch(UnconfinedTestDispatcher(testScope.testScheduler)) {
            githubDataSource.launchLinksStoreMonitoring(linksResponseChannel)
        }

        testScope.backgroundScope.launch(UnconfinedTestDispatcher(testScope.testScheduler)) {
            githubDataSource.launchLinksRemoveMonitoring(linksResponseChannel)
        }

        testScope.backgroundScope.launch(UnconfinedTestDispatcher(testScope.testScheduler)) {
            stackOverflowDataSource.launchLinksStoreMonitoring(linksResponseChannel)
        }

        testScope.backgroundScope.launch(UnconfinedTestDispatcher(testScope.testScheduler)) {
            stackOverflowDataSource.launchLinksRemoveMonitoring(linksResponseChannel)
        }
    }
}