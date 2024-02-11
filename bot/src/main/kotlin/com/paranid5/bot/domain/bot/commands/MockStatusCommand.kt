package com.paranid5.bot.domain.bot.commands

import com.paranid5.bot.domain.links.parseLink
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message

data class MockStatusCommand(override val text: String? = null) : BotStatusCommand<Unit?> {
    override suspend fun onCommand(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>
    ): Unit? = parseLink(message.text())?.let { }

    override suspend fun onFailure(bot: TelegramBot, message: Message): Unit = Unit
}