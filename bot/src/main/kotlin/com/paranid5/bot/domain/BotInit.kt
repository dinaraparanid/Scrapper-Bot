package com.paranid5.bot.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class BotInit @Autowired constructor(private val bot: ScrapperBot) {
    @EventListener(ContextRefreshedEvent::class)
    fun init() = bot.launchBot()
}