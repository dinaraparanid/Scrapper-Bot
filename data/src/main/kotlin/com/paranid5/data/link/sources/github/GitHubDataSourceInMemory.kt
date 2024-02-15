package com.paranid5.data.link.sources.github

import com.paranid5.core.entities.link.LinkType
import com.paranid5.data.link.sources.DefaultLinkDataSourceInMemory
import com.paranid5.data.link.sources.LinkDataSource
import org.springframework.stereotype.Component

@Component
class GitHubDataSourceInMemory :
    LinkDataSource by DefaultLinkDataSourceInMemory(LinkType::GitHubLink),
    GitHubDataSource