package com.paranid5.bot.commands

import com.paranid5.bot.messages.linkListMessage
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.BotCommand
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.response.SendResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

private const val LIST_COMMAND = "/list"
private const val LIST_DESCRIPTION = "display a list of tracking links"

data object ListCommand :
    BotTextCommand<SendResponse>,
    ToTgBotCommand {
    override fun matches(command: String): Boolean =
        command == LIST_COMMAND

    override suspend fun onCommand(
        bot: TelegramBot,
        message: Message,
        userLinks: List<String>,
    ): SendResponse = sendListMessage(bot, message, userLinks).await()

    override fun toTgBotCommand(): BotCommand =
        BotCommand(LIST_COMMAND, LIST_DESCRIPTION)
}

private suspend inline fun sendListMessage(
    bot: TelegramBot,
    message: Message,
    links: List<String>,
) = coroutineScope {
    async(Dispatchers.IO) {
        sendListMessageImpl(bot, message, links)
    }
}

private fun sendListMessageImpl(bot: TelegramBot, message: Message, links: List<String>): SendResponse =
    bot.execute(linkListMessage(message, links))