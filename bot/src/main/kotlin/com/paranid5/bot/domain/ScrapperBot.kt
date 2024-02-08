package com.paranid5.bot.domain

import com.paranid5.bot.configuration.AppConfig
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import org.springframework.stereotype.Component

@Component
class ScrapperBot(config: AppConfig) {
    private val token = config.telegramToken
    private lateinit var bot: TelegramBot

    fun launchBot() {
        bot = TelegramBot(token).apply {
            setUpdatesListener {
                it.forEach(::println)
                UpdatesListener.CONFIRMED_UPDATES_ALL
            }
        }
    }
}