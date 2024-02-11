package com.paranid5.bot.data.link.sources.github

import org.springframework.stereotype.Component

@Component("gh_mock")
class GitHubDataSourceMock : GitHubDataSource by GitHubDataSourceInMemory()