package com.paranid5.data.link.repository

import com.paranid5.data.link.response.LinkResponseChannel
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class LinkRepositoryInit(
    @Qualifier("linkRepositoryInMemory")
    private val repository: LinkRepository,
    @Qualifier("linkResponseChannelImpl")
    private val linkResponseChannel: LinkResponseChannel
) {
    @EventListener(ContextRefreshedEvent::class)
    fun init(): Unit = repository.launchSourcesLinksMonitoring(linkResponseChannel)
}