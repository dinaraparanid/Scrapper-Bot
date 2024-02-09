package com.paranid5.bot.data.link.sources.stack_overflow

import com.paranid5.bot.data.link.sources.DefaultLinkDataSourceInMemory
import com.paranid5.bot.data.link.sources.LinkDataSource
import com.paranid5.bot.domain.links.LinkType
import org.springframework.stereotype.Component

@Component("so_memory")
class StackOverflowDataSourceInMemory :
    LinkDataSource by DefaultLinkDataSourceInMemory(LinkType::StackOverflowLink),
    StackOverflowDataSource