package com.paranid5.bot

import com.paranid5.core.bot.ScrapperBot
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class BotInit @Autowired constructor(private val bot: ScrapperBot) {
    @EventListener(ContextRefreshedEvent::class)
    fun init(): Unit = bot.launchBot()
}