package com.paranid5.data.link.sources.github

import org.springframework.stereotype.Component

@Component
class GitHubDataSourceMock : GitHubDataSource by GitHubDataSourceInMemory()