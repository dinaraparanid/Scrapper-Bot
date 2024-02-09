package com.paranid5.bot.data.link.repository

import com.paranid5.bot.data.link.response.LinkResponseChannel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class LinkRepositoryInit @Autowired constructor(
    @Qualifier("link_rep_memory")
    private val repository: LinkRepository,
    private val linkResponseChannel: LinkResponseChannel
) {
    @EventListener(ContextRefreshedEvent::class)
    fun init(): Unit = repository.launchSourcesLinksMonitoring(linkResponseChannel)
}