package com.paranid5.bot

import com.paranid5.com.paranid5.utils.extend
import com.paranid5.core.entities.link.LinkType
import com.paranid5.data.link.repository.DefaultLinkRepositoryInMemory
import com.paranid5.data.link.repository.LinkRepository
import com.paranid5.data.link.response.LinkResponseChannel
import com.paranid5.data.link.sources.github.GitHubDataSource
import com.paranid5.data.link.sources.stack_overflow.StackOverflowDataSource
import kotlinx.coroutines.CoroutineScope
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
) : LinkRepository by DefaultLinkRepositoryInMemory(
    githubDataSource = githubDataSource,
    stackOverflowDataSource = stackOverflowDataSource,
    coroutineScope = testScope.backgroundScope,
    coroutineContext = UnconfinedTestDispatcher(testScope.testScheduler)
)