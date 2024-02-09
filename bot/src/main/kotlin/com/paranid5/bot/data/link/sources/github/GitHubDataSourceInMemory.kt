package com.paranid5.bot.data.link.sources.github

import com.paranid5.bot.data.link.sources.DefaultLinkDataSourceInMemory
import com.paranid5.bot.data.link.sources.LinkDataSource
import com.paranid5.bot.domain.links.LinkType
import org.springframework.stereotype.Component

@Component("gh_memory")
class GitHubDataSourceInMemory :
    LinkDataSource by DefaultLinkDataSourceInMemory(LinkType::GitHubLink),
    GitHubDataSource