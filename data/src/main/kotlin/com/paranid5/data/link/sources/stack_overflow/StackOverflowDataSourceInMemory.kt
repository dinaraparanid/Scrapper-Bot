package com.paranid5.data.link.sources.stack_overflow

import com.paranid5.core.entities.link.LinkType
import com.paranid5.data.link.sources.DefaultLinkDataSourceInMemory
import com.paranid5.data.link.sources.LinkDataSource
import org.springframework.stereotype.Component

@Component
class StackOverflowDataSourceInMemory :
    LinkDataSource by DefaultLinkDataSourceInMemory(LinkType::StackOverflowLink),
    StackOverflowDataSource