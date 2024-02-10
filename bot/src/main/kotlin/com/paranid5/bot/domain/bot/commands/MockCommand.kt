package com.paranid5.bot.domain.bot.commands

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message

data class MockCommand(override val text: String?) : BotCommand<Unit> {
    override suspend fun onCommand(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>
    ): Unit = Unit
}