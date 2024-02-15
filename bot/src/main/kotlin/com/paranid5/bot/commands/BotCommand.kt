package com.paranid5.bot.commands

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message

sealed interface BotCommand<T> {
    val text: String?

    suspend fun onCommand(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>,
    ): T
}