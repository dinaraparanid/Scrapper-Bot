package com.paranid5.bot.commands

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message

data class MockCommand(private val text: String) : BotTextCommand<Unit> {
    override fun matches(command: String): Boolean =
        command == text

    override suspend fun onCommand(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>
    ): Unit = Unit
}